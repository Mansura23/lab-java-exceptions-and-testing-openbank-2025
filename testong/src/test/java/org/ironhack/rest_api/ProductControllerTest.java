package org.ironhack.rest_api;


import org.ironhack.rest_api.controller.ProductController;
import org.ironhack.rest_api.exception.LowerBoundException;
import org.ironhack.rest_api.exception.ProductNotFoundException;
import org.ironhack.rest_api.model.Product;
import org.ironhack.rest_api.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    private final String API_KEY = "123456789";

    private Product createProduct(String name, double price, String category, int quantity) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setCategory(category);
        product.setQuantity(quantity);
        return product;
    }

    @Test
    void getProducts_returnsOk() throws Exception {

        Product p1 = createProduct("Laptop",1500,"Tech",10);
        Product p2 = createProduct("Phone",800,"Tech",5);

        when(productService.findAll()).thenReturn(List.of(p1,p2));

        mockMvc.perform(get("/api/products")
                        .header("apiKey",API_KEY))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Laptop"))
                .andExpect(jsonPath("$[1].name").value("Phone"));
    }

    @Test
    void getProductByName_existingName_returnsProduct() throws Exception {

        Product product = createProduct("Laptop",1500,"Tech",10);

        when(productService.findByName("Laptop")).thenReturn(product);

        mockMvc.perform(get("/api/products/Laptop")
                        .header("apiKey",API_KEY))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.price").value(1500))
                .andExpect(jsonPath("$.category").value("Tech"))
                .andExpect(jsonPath("$.quantity").value(10));
    }

    @Test
    void getProductByName_notFound_returns404() throws Exception {

        when(productService.findByName("TV"))
                .thenThrow(new ProductNotFoundException("Product not found"));

        mockMvc.perform(get("/api/products/TV")
                        .header("apiKey",API_KEY))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Product not found"));
    }

    @Test
    void createProduct_validRequest_returnsCreated() throws Exception {

        Product created = createProduct("Tablet",500,"Tech",3);

        when(productService.save(any(Product.class))).thenReturn(created);

        String body = """
                {
                   "name":"Tablet",
                   "price":500,
                   "category":"Tech",
                   "quantity":3
                }
                """;

        mockMvc.perform(post("/api/products")
                        .header("apiKey",API_KEY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Tablet"))
                .andExpect(jsonPath("$.price").value(500));
    }

    @Test
    void createProduct_invalidBody_returns400() throws Exception {

        String body = """
                {
                   "price":500
                }
                """;

        mockMvc.perform(post("/api/products")
                        .header("apiKey",API_KEY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").exists());
    }

    @Test
    void getProductsByCategory_returnsList() throws Exception {

        Product p1 = createProduct("Laptop",1500,"Tech",10);

        when(productService.findByCategory("Tech")).thenReturn(List.of(p1));

        mockMvc.perform(get("/api/products/category/Tech")
                        .header("apiKey",API_KEY))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Laptop"));
    }

    @Test
    void getProductsByPriceRange_validRange_returnsProducts() throws Exception {

        Product p1 = createProduct("Laptop",1500,"Tech",10);

        when(productService.findByPriceRange(1000,2000))
                .thenReturn(List.of(p1));

        mockMvc.perform(get("/api/products/price")
                        .header("apiKey",API_KEY)
                        .param("lower","1000")
                        .param("higher","2000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Laptop"));
    }

    @Test
    void getProductsByPriceRange_invalidRange_returns400() throws Exception {

        when(productService.findByPriceRange(2000,1000))
                .thenThrow(new LowerBoundException("Lower bound is greater than upper bound"));

        mockMvc.perform(get("/api/products/price")
                        .header("apiKey",API_KEY)
                        .param("lower","2000")
                        .param("higher","1000"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value("Lower bound is greater than upper bound"));
    }

    @Test
    void updateProduct_validRequest_returnsUpdated() throws Exception {

        Product updated = createProduct("Laptop",1200,"Tech",7);

        when(productService.update(eq("Laptop"),any(Product.class)))
                .thenReturn(updated);

        String body = """
                {
                   "name":"Laptop",
                   "price":1200,
                   "category":"Tech",
                   "quantity":7
                }
                """;

        mockMvc.perform(put("/api/products/Laptop")
                        .header("apiKey",API_KEY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(1200));
    }

    @Test
    void deleteProduct_existingName_returns204() throws Exception {

        doNothing().when(productService).delete("Laptop");

        mockMvc.perform(delete("/api/products/Laptop")
                        .header("apiKey",API_KEY))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteProduct_notFound_returns404() throws Exception {

        doThrow(new ProductNotFoundException("Product not found"))
                .when(productService).delete("TV");

        mockMvc.perform(delete("/api/products/TV")
                        .header("apiKey",API_KEY))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Product not found"));
    }

}
package org.ironhack.rest_api.controller;

import jakarta.validation.Valid;
import org.ironhack.rest_api.dto.request.CreateProductRequest;
import org.ironhack.rest_api.dto.request.UpdateProductRequest;
import org.ironhack.rest_api.dto.response.ProductResponse;
import org.ironhack.rest_api.dto.response.ProductSummary;
import org.ironhack.rest_api.exception.MissingKeyHeaderException;
import org.ironhack.rest_api.mapper.ProductMapper;
import org.ironhack.rest_api.model.Customer;
import org.ironhack.rest_api.model.Product;
import org.ironhack.rest_api.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final static String API_KEY = "123456789";

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    public void checkApiKey(String apiKey) {
        if (apiKey == null) {
            throw new MissingKeyHeaderException("Missing API Key");
        }
        if (!apiKey.matches(API_KEY)) {
            throw new MissingKeyHeaderException("Invalid API Key");
        }
    }

    @GetMapping
    public List<ProductSummary> getProducts(@RequestHeader String apiKey) {
        checkApiKey(apiKey);
        List<Product> products=productService.findAll();
        return ProductMapper.toSummaryList(products);
    }

    @PostMapping
    public ResponseEntity<ProductResponse> addProduct(@RequestHeader String apiKey, @Valid @RequestBody CreateProductRequest createProductRequest) {
        checkApiKey(apiKey);
        Product product = ProductMapper.toModel(createProductRequest);
        Product created = productService.save(product);
        ProductResponse productResponse = ProductMapper.toResponse(created);
        return ResponseEntity.status(HttpStatus.CREATED).body(productResponse);
    }

    @GetMapping("/category/{category}")
    public List<ProductSummary> getProductsByCategory(@RequestHeader String apiKey, @PathVariable String category) {
        checkApiKey(apiKey);
        List<Product> products=productService.findByCategory(category);
        return ProductMapper.toSummaryList(products);
    }

    @GetMapping("/{name}")
    public ProductResponse getProductsByName(@RequestHeader String apiKey, @PathVariable String name) {
        checkApiKey(apiKey);
        Product product = productService.findByName(name);
        return ProductMapper.toResponse(product);
    }

    @GetMapping("/price")
    public List<ProductSummary> getProductsByPriceRange(@RequestHeader String apiKey,
                                                 @RequestParam(required = false) double lower,
                                                 @RequestParam(required = false) double higher) {
        checkApiKey(apiKey);
        List<Product> products=productService.findByPriceRange(lower, higher);
        return ProductMapper.toSummaryList(products);
    }
    @PutMapping("/{name}")
    public ResponseEntity<ProductResponse> updateProduct(@RequestHeader String apiKey, @PathVariable String name, @Valid @RequestBody UpdateProductRequest updateProductRequest) {
        checkApiKey(apiKey);
        Product product = ProductMapper.toModel(updateProductRequest);
        Product updated = productService.update(name,product);
        ProductResponse productResponse = ProductMapper.toResponse(updated);
        return ResponseEntity.ok(productResponse);
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteProduct(@RequestHeader String apiKey, @PathVariable String name) {
        checkApiKey(apiKey);
        productService.delete(name);
        return  ResponseEntity.noContent().build();

    }


}

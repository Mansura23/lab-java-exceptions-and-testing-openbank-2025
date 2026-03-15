package org.ironhack.rest_api.mapper;

import org.ironhack.rest_api.dto.request.CreateProductRequest;
import org.ironhack.rest_api.dto.request.UpdateProductRequest;
import org.ironhack.rest_api.dto.response.ProductResponse;
import org.ironhack.rest_api.dto.response.ProductSummary;
import org.ironhack.rest_api.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductMapper {

    public static Product toModel(CreateProductRequest createProductRequest) {
        Product product=new Product();
        product.setName(createProductRequest.getName());
        product.setPrice(createProductRequest.getPrice());
        product.setQuantity(createProductRequest.getQuantity());
        product.setCategory(createProductRequest.getCategory());
        return product;
    }

    public static Product toModel(UpdateProductRequest updateProductRequest) {
        Product product=new Product();
        product.setName(updateProductRequest.getName());
        product.setPrice(updateProductRequest.getPrice());
        product.setQuantity(updateProductRequest.getQuantity());
        product.setCategory(updateProductRequest.getCategory());
        return product;
    }

    public static ProductResponse  toResponse(Product product) {
       return new ProductResponse(
               product.getName(),
               product.getPrice(),
               product.getCategory(),
               product.getQuantity()
       );

    }

    public static ProductSummary  toSummary(Product product) {
        return new ProductSummary(product.getName(), product.getPrice());
    }

    public static List<ProductSummary> toSummaryList(List<Product> products) {
        List<ProductSummary> productSummaries=new ArrayList<>();
        for (Product product:products) {
            productSummaries.add(toSummary(product));
        }
        return productSummaries;
    }
}

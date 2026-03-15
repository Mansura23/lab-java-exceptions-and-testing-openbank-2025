package org.ironhack.rest_api.dto.request;

import jakarta.validation.constraints.*;

public class UpdateProductRequest {
    @NotBlank(message = "Name is required")
    @Size(min = 3, message = "Name length must be consist of min 3 elements")
    private String name;

    @Positive(message = "Price must be greater than 0")
    private Double price;

    @NotBlank(message = "Category is required")
    private String category;

    @Positive(message = "Quantity must be greater than 0")
    private int quantity;

    public UpdateProductRequest() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}


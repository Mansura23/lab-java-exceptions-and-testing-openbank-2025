package org.ironhack.rest_api.dto.response;

public class ProductResponse {
    private final String name;
    private final double price;
    private final String category;
    private final int quantity;

    public ProductResponse(String name, double price, String category, int quantity) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public int getQuantity() {
        return quantity;
    }
}

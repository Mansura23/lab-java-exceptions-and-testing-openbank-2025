package org.ironhack.rest_api.dto.response;

public class ProductSummary {
    private final String name;
    private final double price;

    public ProductSummary(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
}

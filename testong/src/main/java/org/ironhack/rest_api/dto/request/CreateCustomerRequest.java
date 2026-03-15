package org.ironhack.rest_api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class CreateCustomerRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "That must be in email format")
    private String email;
    @Min(value = 18, message = "Age must be 18 or greater than 18")
    private int age;

    @NotBlank(message = "Address is required")
    private String address;

    public CreateCustomerRequest() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

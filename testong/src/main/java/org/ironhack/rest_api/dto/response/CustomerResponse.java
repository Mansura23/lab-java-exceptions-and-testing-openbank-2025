package org.ironhack.rest_api.dto.response;

public class CustomerResponse {

    private final String  name;
    private final String  email;
    private final String  address;
    private final int  age;

    public CustomerResponse(String name, String email, String address, int age) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public int getAge() {
        return age;
    }
}

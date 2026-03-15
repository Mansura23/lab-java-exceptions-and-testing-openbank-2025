package org.ironhack.rest_api.mapper;

import org.ironhack.rest_api.dto.request.CreateCustomerRequest;
import org.ironhack.rest_api.dto.request.UpdateCustomerRequest;
import org.ironhack.rest_api.dto.response.CustomerResponse;
import org.ironhack.rest_api.dto.response.CustomerSummary;
import org.ironhack.rest_api.model.Customer;

import java.util.ArrayList;
import java.util.List;

public class CustomerMapper {

    public static Customer toModel(CreateCustomerRequest createCustomerRequest) {
        Customer customer = new Customer();
        customer.setName(createCustomerRequest.getName());
        customer.setAddress(createCustomerRequest.getAddress());
        customer.setEmail(createCustomerRequest.getEmail());
        customer.setAge(createCustomerRequest.getAge());
        return customer;
    }
    public static Customer toModel(UpdateCustomerRequest updateCustomerRequest) {
        Customer customer = new Customer();
        customer.setName(updateCustomerRequest.getName());
        customer.setAddress(updateCustomerRequest.getAddress());
        customer.setEmail(updateCustomerRequest.getEmail());
        customer.setAge(updateCustomerRequest.getAge());
        return customer;
    }
    public static CustomerResponse toResponse(Customer customer) {
        return new CustomerResponse(customer.getName(), customer.getAddress(), customer.getEmail(), customer.getAge());
    }
    public static CustomerSummary toSummary(Customer customer) {
        return new CustomerSummary(customer.getName(), customer.getEmail());
    }
    public static List<CustomerSummary> toSummaryList(List<Customer> customers) {
        List<CustomerSummary> customerSummaries = new ArrayList<>();
        for (Customer customer : customers) {
            customerSummaries.add(toSummary(customer));
        }
        return customerSummaries;
    }
}

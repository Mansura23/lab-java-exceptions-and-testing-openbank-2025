package org.ironhack.rest_api.controller;

import jakarta.validation.Valid;
import org.ironhack.rest_api.dto.request.CreateCustomerRequest;
import org.ironhack.rest_api.dto.request.UpdateCustomerRequest;
import org.ironhack.rest_api.dto.response.CustomerResponse;
import org.ironhack.rest_api.dto.response.CustomerSummary;
import org.ironhack.rest_api.mapper.CustomerMapper;
import org.ironhack.rest_api.model.Customer;
import org.ironhack.rest_api.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<CustomerSummary> getCustomers() {
        List<Customer> customers= customerService.getCustomers();
        return CustomerMapper.toSummaryList(customers);
    }

    @GetMapping("/{email}")
    public CustomerResponse getCustomer(@PathVariable String email) {
        Customer customer=customerService.getCustomerByEmail(email);
        return CustomerMapper.toResponse(customer);
    }

    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody CreateCustomerRequest createCustomerRequest) {
        Customer customer= CustomerMapper.toModel(createCustomerRequest);
        Customer created=customerService.addCustomer(customer);
        CustomerResponse customerResponse=CustomerMapper.toResponse(created);
        return ResponseEntity.status(HttpStatus.CREATED).body(customerResponse);
    }

    @PutMapping("/{email}")
    public ResponseEntity<CustomerResponse> updateCustomer(@PathVariable String email, @Valid @RequestBody UpdateCustomerRequest updateCustomerRequest) {
        Customer customer=CustomerMapper.toModel(updateCustomerRequest);
        Customer update=customerService.updateCustomer(email,customer);
        CustomerResponse customerResponse=CustomerMapper.toResponse(update);
        return ResponseEntity.ok(customerResponse);
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable String email) {
        customerService.deleteCustomerByEmail(email);
        return ResponseEntity.noContent().build();
    }

}

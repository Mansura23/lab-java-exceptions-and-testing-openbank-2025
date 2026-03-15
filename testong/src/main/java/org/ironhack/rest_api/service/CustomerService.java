package org.ironhack.rest_api.service;

import org.ironhack.rest_api.exception.CustomerNotFound;
import org.ironhack.rest_api.model.Customer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class CustomerService {

    private final HashMap<String, Customer> customers = new HashMap<>();

    public List<Customer> getCustomers() {
        return new ArrayList<>(customers.values());
    }

    /// In real projects l dont use  if(!customers.containsKey(customer.getEmail())) {
    ///             customers.put(customer.getEmail(), customer);
    ///         }  but email can be same, that is why l use it
    public Customer addCustomer(Customer customer) {
        if(!customers.containsKey(customer.getEmail())) {
            customers.put(customer.getEmail(), customer);
        }
        return customer;
    }

    public Customer getCustomerByEmail(String email) {
        if (customers.containsKey(email)) {
            return customers.get(email);
        }
        throw new CustomerNotFound("Customer with email " + email + " not found");
    }

    public Customer updateCustomer(String email,Customer customer) {
        Customer updated=getCustomerByEmail(email);
        updated.setName(customer.getName());
        updated.setAddress(customer.getAddress());
        updated.setEmail(customer.getEmail());
        updated.setAge(customer.getAge());
        return customer;
    }

    public void deleteCustomerByEmail(String email) {
        getCustomerByEmail(email);
        customers.remove(email);
    }


}

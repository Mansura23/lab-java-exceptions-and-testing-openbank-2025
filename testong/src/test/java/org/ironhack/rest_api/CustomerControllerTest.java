package org.ironhack.rest_api;

import org.ironhack.rest_api.controller.CustomerController;
import org.ironhack.rest_api.exception.CustomerNotFound;
import org.ironhack.rest_api.model.Customer;
import org.ironhack.rest_api.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomerService customerService;

    // Utility method
    private Customer createCustomer(String name, String email, String address, int age) {
        Customer customer = new Customer();
        customer.setName(name);
        customer.setEmail(email);
        customer.setAddress(address);
        customer.setAge(age);
        return customer;
    }

    @Test
    void getCustomers_returnsOk() throws Exception {

        Customer c1 = createCustomer("John","john@mail.com","London",25);
        Customer c2 = createCustomer("Anna","anna@mail.com","Rome",30);

        when(customerService.getCustomers()).thenReturn(List.of(c1,c2));

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("John"))
                .andExpect(jsonPath("$[1].name").value("Anna"));
    }

    @Test
    void getCustomerByEmail_existingEmail_returnsCustomer() throws Exception {

        Customer customer = createCustomer("John","john@mail.com","London",25);

        when(customerService.getCustomerByEmail("john@mail.com"))
                .thenReturn(customer);

        mockMvc.perform(get("/api/customers/john@mail.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.email").value("john@mail.com"))
                .andExpect(jsonPath("$.address").value("London"))
                .andExpect(jsonPath("$.age").value(25));
    }

    @Test
    void getCustomerByEmail_notFound_returns404() throws Exception {

        when(customerService.getCustomerByEmail("missing@mail.com"))
                .thenThrow(new CustomerNotFound("Customer with email missing@mail.com not found"));

        mockMvc.perform(get("/api/customers/missing@mail.com"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createCustomer_validRequest_returnsCreated() throws Exception {

        Customer created = createCustomer("Ali","ali@mail.com","Baku",28);

        when(customerService.addCustomer(any(Customer.class))).thenReturn(created);

        String body = """
                {
                    "name":"Ali",
                    "email":"ali@mail.com",
                    "address":"Baku",
                    "age":28
                }
                """;

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Ali"))
                .andExpect(jsonPath("$.email").value("ali@mail.com"));
    }

    @Test
    void createCustomer_invalidBody_returns400() throws Exception {

        String body = """
                {
                    "email":"wrong-format",
                    "age":10
                }
                """;

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateCustomer_validRequest_returnsUpdatedCustomer() throws Exception {

        Customer updated = createCustomer("Ali","ali@mail.com","Baku",30);

        when(customerService.updateCustomer(eq("ali@mail.com"), any(Customer.class)))
                .thenReturn(updated);

        String body = """
                {
                    "name":"Ali",
                    "email":"ali@mail.com",
                    "address":"Baku",
                    "age":30
                }
                """;

        mockMvc.perform(put("/api/customers/ali@mail.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.age").value(30));
    }

    @Test
    void deleteCustomer_existingEmail_returns204() throws Exception {

        doNothing().when(customerService).deleteCustomerByEmail("ali@mail.com");

        mockMvc.perform(delete("/api/customers/ali@mail.com"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteCustomer_notFound_returns404() throws Exception {

        doThrow(new CustomerNotFound("Customer with email test@mail.com not found"))
                .when(customerService).deleteCustomerByEmail("test@mail.com");

        mockMvc.perform(delete("/api/customers/test@mail.com"))
                .andExpect(status().isNotFound());
    }
}



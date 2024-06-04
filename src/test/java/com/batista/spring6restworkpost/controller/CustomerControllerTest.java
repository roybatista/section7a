package com.batista.spring6restworkpost.controller;

import com.batista.spring6restworkpost.model.Customer;
import com.batista.spring6restworkpost.service.CustomerService;
import com.batista.spring6restworkpost.service.CustomerServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = CustomerController.class)
class CustomerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CustomerService customerService;

    @Autowired
    ObjectMapper mapper ;

    CustomerService customerServiceImpl;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Captor
    ArgumentCaptor<Customer> customerArgumentCaptor;


    @BeforeEach
    void setUp(){
        customerServiceImpl = new CustomerServiceImpl();
    }

    @Test
    void testPatchCustomer() throws Exception {

        Customer customer = customerServiceImpl.getCustomers().get(0);

        Map<String, Object> customerMap = new HashMap<>();
        customerMap.put("customerName" , "New Customer Name");

        mockMvc.perform(patch(CustomerController.CUSTOMER_PATH_ID, customer.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(customerMap)))
                .andExpect(status().isNoContent());

        verify(customerService).patchById(uuidArgumentCaptor.capture(), customerArgumentCaptor.capture());

        assertThat(uuidArgumentCaptor.getValue().equals(customer.getId()));
        assertThat(customerArgumentCaptor.getValue().equals(customerMap.get("customerName")));
    }

    @Test
    void deleteCustomer() throws Exception {

        Customer customer = customerServiceImpl.getCustomers().get(0);

        mockMvc.perform(delete(CustomerController.CUSTOMER_PATH_ID, customer.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));



        verify(customerService).deleteById(uuidArgumentCaptor.capture());

        assertThat(customer.getId().equals(uuidArgumentCaptor.getValue()));

    }

    @Test
    void updateCustomer() throws Exception {

        Customer customer = customerServiceImpl.getCustomers().get(0);

        mockMvc.perform(put(CustomerController.CUSTOMER_PATH_ID, customer.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(customer)))
                .andExpect(status().isNoContent());

        verify(customerService).updateCustomer(customer.getId(), customer);

    }

    @Test
    void testCreateNewCustomer() throws Exception {
        Customer customer = customerServiceImpl.getCustomers().get(0);
        customer.setId(null);
        customer.setVersion(null);

        given(customerService.saveCustomer(any(Customer.class))).willReturn(customerServiceImpl.getCustomers().get(1));

        mockMvc.perform(post(CustomerController.CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(customer))
                )
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"));

    }

    @Test
    void getCustomer() throws Exception {

        given(customerService.getCustomers()).willReturn(customerServiceImpl.getCustomers());

        mockMvc.perform(get(CustomerController.CUSTOMER_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));
    }

    @Test
    void getCustomerById() throws Exception {

        Customer customer = customerServiceImpl.getCustomers().get(0);

        given(customerService.getCustomer(customer.getId())).willReturn(customer);

        mockMvc.perform(get(CustomerController.CUSTOMER_PATH_ID, customer.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.customerName" , is(customer.getCustomerName())));


    }
}
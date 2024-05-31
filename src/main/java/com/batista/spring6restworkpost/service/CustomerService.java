package com.batista.spring6restworkpost.service;

import com.batista.spring6restworkpost.model.Customer;

import java.util.List;
import java.util.UUID;


public interface CustomerService {

    public Customer getCustomer(UUID id);
    public List<Customer> getCustomers();
    public Customer saveCustomer(Customer customer);
    void updateCustomer(UUID id, Customer customer);

    void deleteById(UUID id);

    void patchById(UUID id, Customer customer);
}

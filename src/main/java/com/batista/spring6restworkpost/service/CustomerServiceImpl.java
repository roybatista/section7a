package com.batista.spring6restworkpost.service;

import com.batista.spring6restworkpost.model.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    Map<UUID, Customer> customers;

    public CustomerServiceImpl() {
        customers = new HashMap<UUID, Customer>();
        Customer customer1 = Customer.builder()
                .customerName("Customer A")
                .id(UUID.randomUUID())
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        Customer customer2 = Customer.builder()
                .customerName("Customer B")
                .id(UUID.randomUUID())
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        Customer customer3 = Customer.builder()
                .customerName("Customer C")
                .id(UUID.randomUUID())
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        customers.put(customer1.getId(), customer1);
        customers.put(customer2.getId(), customer2);
        customers.put(customer3.getId(), customer3);
    }

    @Override
    public Customer getCustomer(UUID id) {
        return customers.get(id);
    }

    @Override
    public List<Customer> getCustomers() {
       return new ArrayList<>(customers.values());
    }

    @Override
    public Customer saveCustomer(Customer customer) {
        Customer customerSave = Customer.builder()
                .customerName(customer.getCustomerName())
                .id(UUID.randomUUID())
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        customers.put(customerSave.getId(), customerSave);


        return customerSave;
    }

    @Override
    public void updateCustomer(UUID id, Customer customer) {

        Customer customer2Update = getCustomer(id);

        customer2Update.setCustomerName(customer.getCustomerName());

        customers.put(customer2Update.getId(), customer2Update);

    }

    @Override
    public void deleteById(UUID id) {

        customers.remove(id);

    }

    @Override
    public void patchById(UUID id, Customer customer2Patch) {

        Customer customer = customers.get(id);

        if(customer != null) {

            if (!customer.getCustomerName().equals(customer2Patch.getCustomerName())) {
                customer.setCustomerName(customer2Patch.getCustomerName());
            }

            if (!customer.getCustomerName().equals(customer2Patch.getCustomerName())) {
                customer.setCustomerName(customer2Patch.getCustomerName());
            }

            if (!customer.getVersion().equals(customer2Patch.getVersion())) {
                customer.setVersion(customer2Patch.getVersion());
            }

            if (!customer.getCreatedDate().equals(customer2Patch.getCreatedDate())) {
                customer.setCreatedDate(customer2Patch.getCreatedDate());
            }

            if (!customer.getLastModifiedDate().equals(customer2Patch.getLastModifiedDate())) {
                customer.setLastModifiedDate(customer2Patch.getLastModifiedDate());
            }

        }



    }
}

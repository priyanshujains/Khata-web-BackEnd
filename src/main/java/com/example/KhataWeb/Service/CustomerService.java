package com.example.KhataWeb.Service;


import com.example.KhataWeb.Models.Customer;
import com.example.KhataWeb.Models.Product;

import java.util.List;

public interface CustomerService {
    List<Customer> getAllCustomers();
    Customer getCustomerById(Long custId);
    Customer addCustomer(Customer customer);
    Customer updateCustomer(Long custId, Customer updatedcustomer);
    void deleteCustomer(Long custId);
}

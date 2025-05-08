package com.example.KhataWeb.Repos;

import com.example.KhataWeb.Models.Customer;
import com.example.KhataWeb.Models.CustomerProductRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerProductRateRepo extends JpaRepository<CustomerProductRate,Long > {

    List<CustomerProductRate> findAllByCustomer(Customer customer);
}

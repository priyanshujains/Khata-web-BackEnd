package com.example.KhataWeb.Repos;

import com.example.KhataWeb.Models.Customer;
import com.example.KhataWeb.Models.CustomerProductRate;
import com.example.KhataWeb.Models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerProductRateRepo extends JpaRepository<CustomerProductRate,Long > {

    List<CustomerProductRate> findAllByCustomer(Customer customer);
    Optional<CustomerProductRate>  findByCustomerAndProduct(Customer customer, Product product);

//    select *
//from customer_product_rate c
//where c.customer_id = ?
//  and c.product_id = ?
//limit 1;
}

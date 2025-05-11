package com.example.KhataWeb.Repos;

import com.example.KhataWeb.Models.Customer;
import com.example.KhataWeb.Models.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReceiptRepo extends JpaRepository<Receipt,Long> {

    List<Receipt> findAllByCustomer(Customer customer);
}

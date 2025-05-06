package com.example.KhataWeb.Repos;

import com.example.KhataWeb.Models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepos extends JpaRepository<Customer,Long> {
}

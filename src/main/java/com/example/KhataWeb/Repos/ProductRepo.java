package com.example.KhataWeb.Repos;

import com.example.KhataWeb.Models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository<Product,Long> {


}

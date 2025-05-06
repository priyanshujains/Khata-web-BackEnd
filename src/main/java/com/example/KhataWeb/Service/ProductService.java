package com.example.KhataWeb.Service;

import com.example.KhataWeb.Models.Product;

import java.util.List;

public interface ProductService {
    Product addProduct(Product product);
    Product getProductById(Long id);
    List<Product> getAllProducts();
    Product updateProduct(Long id, Product product);
    Product updateQuantity(Long id, Long quantity);
    void deleteProduct(Long id);
}

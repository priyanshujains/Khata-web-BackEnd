package com.example.KhataWeb.Models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Product product;

    private double rate;
    private long quantity;

    private double totalPrice;


    // orderItem: product
    // 1:1
    //  M :1
}

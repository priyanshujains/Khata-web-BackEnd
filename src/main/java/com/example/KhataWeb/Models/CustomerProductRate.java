package com.example.KhataWeb.Models;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class CustomerProductRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private Product product;

    private double customRate;
}
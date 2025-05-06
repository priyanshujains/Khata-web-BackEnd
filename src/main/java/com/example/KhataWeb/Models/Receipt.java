package com.example.KhataWeb.Models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;


@Entity
@Data
public class Receipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long receiptId;

    @ManyToOne
    private Customer customer;

    @OneToMany(cascade = CascadeType.ALL)
    List<OrderItem> itemList;

    private double totalAmount;

    private boolean delivery;
    private LocalDateTime createdAt;
}

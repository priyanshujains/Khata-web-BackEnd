package com.example.KhataWeb.Dtos;

import lombok.Data;

@Data
public class ItemListDto {

    private String product;
    private double rate;
    private long quantity;
    private double totalPrice;
}

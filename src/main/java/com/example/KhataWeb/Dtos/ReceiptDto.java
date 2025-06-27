package com.example.KhataWeb.Dtos;


import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReceiptDto {

    private Long receiptId;
    private String cusName;
    List<ItemListDto> itemLists;
    private boolean delivery;
    private LocalDateTime createdAt;
    private Double totalAmount;


}

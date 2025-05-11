package com.example.KhataWeb.Dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OrderItemRequest {

    @JsonProperty("pId")
    private Long pId;
    private Long quantity;

}

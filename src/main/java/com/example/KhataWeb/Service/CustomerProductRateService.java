package com.example.KhataWeb.Service;

import com.example.KhataWeb.Dtos.ProductRate;
import com.example.KhataWeb.Models.CustomerProductRate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CustomerProductRateService {

    List<ProductRate>  getAllCustomerProductRates(Long custId);

    String addCustomerProductRate(Long custId, List<ProductRate> productRates);



}
package com.example.KhataWeb.Service;

import com.example.KhataWeb.Dtos.ProductRate;
import com.example.KhataWeb.Models.CustomerProductRate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CustomerProductRateService {



String addCustomerProductRate(Long custId, List<ProductRate> productRates);

//    String updateCustomerProductRate(Long custId, List<ProductRate> productRates);


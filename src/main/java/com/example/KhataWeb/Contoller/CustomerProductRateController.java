package com.example.KhataWeb.Contoller;

import com.example.KhataWeb.Dtos.ProductRate;
import com.example.KhataWeb.Service.CustomerProductRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rates")
public class CustomerProductRateController {

    private final CustomerProductRateService customerProductRateService;
    @Autowired
    public CustomerProductRateController(CustomerProductRateService customerProductRateService){
        this.customerProductRateService=customerProductRateService;
    }

    @GetMapping("/{custId}")
    public ResponseEntity<List<ProductRate>> getAllCustomerProductRates(@PathVariable Long custId){
        return new ResponseEntity<>(customerProductRateService.getAllCustomerProductRates(custId),HttpStatus.OK);
    }


    @PostMapping("/{custId}")
    public ResponseEntity<String> addCustomerProductRate(@PathVariable Long custId, @RequestBody List<ProductRate> productRates){
        return new ResponseEntity<>(customerProductRateService.addCustomerProductRate(custId ,productRates), HttpStatus.CREATED);
    }
//    @PutMapping("/{custId}")
//    public ResponseEntity<?> updateCustomerProductRate(@PathVariable Long custId, @RequestBody ProductRate productRates){
//        return new ResponseEntity<>(customerProductRateService.updateCustomerProductRate(custId ,productRates), HttpStatus.CREATED);
//    }







}

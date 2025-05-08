package com.example.KhataWeb.Service;

import com.example.KhataWeb.Dtos.ProductRate;
import com.example.KhataWeb.Models.Customer;
import com.example.KhataWeb.Models.CustomerProductRate;
import com.example.KhataWeb.Models.Product;
import com.example.KhataWeb.Repos.CustomerProductRateRepo;
import com.example.KhataWeb.Repos.CustomerRepos;
import com.example.KhataWeb.Repos.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class CustomerProductRateServiceImpl implements CustomerProductRateService{

    private final CustomerProductRateRepo customerProductRateRepo;
    private final ProductRepo productRepo;
    private final CustomerRepos customerRepos;

    @Autowired
    public CustomerProductRateServiceImpl(CustomerProductRateRepo customerProductRateRepo,
                                          ProductRepo productRepo,
                                          CustomerRepos customerRepos){
        this.customerProductRateRepo=customerProductRateRepo;
        this.customerRepos=customerRepos;
        this.productRepo=productRepo;
    }

    @Override
    public String addCustomerProductRate(Long custId, List<ProductRate> productRates) {

        Optional<Customer> optionalCustomer=customerRepos.findById(custId);
        if(optionalCustomer.isEmpty())throw new RuntimeException("Customer not ecxist");

        List<CustomerProductRate> customerProductRates=new ArrayList<>();


        for(ProductRate pr: productRates){

            CustomerProductRate customerProductRate=new CustomerProductRate();
            customerProductRate.setCustomer(optionalCustomer.get());
            Optional<Product> optionalProduct=productRepo.findById(pr.getPid());
            if(optionalProduct.isEmpty())throw new RuntimeException("Product not exist");
            customerProductRate.setProduct(optionalProduct.get());
            customerProductRate.setCustomRate(pr.getCustomerRate());

            customerProductRates.add(customerProductRate);

        }

        customerProductRateRepo.saveAll(customerProductRates);
        return "Rates added success" ;
    }
}

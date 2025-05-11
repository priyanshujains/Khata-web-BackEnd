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
    public List<ProductRate> getAllCustomerProductRates(Long custId) {

        Optional<Customer> customer=customerRepos.findById(custId);
        if(customer.isEmpty()) throw new RuntimeException("No customer for this Id exist");
        List<ProductRate> productRates=new ArrayList<>();
        List<CustomerProductRate>  customerProductRates=customerProductRateRepo.findAllByCustomer(customer.get());
        for(CustomerProductRate cr:customerProductRates){
            ProductRate productRate=new ProductRate();
            productRate.setPid(cr.getProduct().getId());
            productRate.setCustomerRate(cr.getCustomRate());

            productRates.add(productRate);
        }
        return productRates;
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

    @Override
    public String updateCustomerProductRate(Long custId, ProductRate productRates) {


        Optional<CustomerProductRate> customerProductRate1= customerProductRateRepo.findByCustomerIdAndPId(custId,
                productRates.getPid());
        if(customerProductRate1.isEmpty()) throw new RuntimeException("No Product with this pid  for this customer ");

        CustomerProductRate customerProductRate=customerProductRate1.get();

        customerProductRate.setCustomRate(productRates.getCustomerRate());
        return "ProductUpdated";
    }
}

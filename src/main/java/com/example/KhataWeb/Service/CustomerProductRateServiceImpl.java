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

import java.util.*;

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
    //    public String addCustomerProductRate(Long custId, List<ProductRate> productRates) {
    //
    //        Optional<Customer> optionalCustomer=customerRepos.findById(custId);
    //        if(optionalCustomer.isEmpty())throw new RuntimeException("Customer not ecxist");
    //
    //        List<CustomerProductRate> customerProductRates=new ArrayList<>();
    //
    //
    //        for(ProductRate pr: productRates){
    //
    //            CustomerProductRate customerProductRate=new CustomerProductRate();
    //            customerProductRate.setCustomer(optionalCustomer.get());
    //            Optional<Product> optionalProduct=productRepo.findById(pr.getPid());
    //            if(optionalProduct.isEmpty())throw new RuntimeException("Product not exist");
    //            customerProductRate.setProduct(optionalProduct.get());
    //            customerProductRate.setCustomRate(pr.getCustomerRate());
    //
    //            customerProductRates.add(customerProductRate);
    //
    //        }
    //
    //        customerProductRateRepo.saveAll(customerProductRates);
    //        return "Rates added success" ;
    //    }


    public String addCustomerProductRate(Long custId, List<ProductRate> productRates) {
        // Check if the customer exists
        Optional<Customer> optionalCustomer = customerRepos.findById(custId);
        if (optionalCustomer.isEmpty()) throw new RuntimeException("Customer does not exist");

        // Fetch all existing rates for the customer in one call
        List<CustomerProductRate> existingRates = customerProductRateRepo.findAllByCustomer(optionalCustomer.get());

        // Create a set to store existing product IDs
        Set<Long> existingProductIds = new HashSet<>();
        for (CustomerProductRate rate : existingRates) {
            existingProductIds.add(rate.getProduct().getId());
        }

        // Iterate through the provided product rates
        for (ProductRate pr : productRates) {
            // Check if the product ID exists in existing rates
            if (existingProductIds.contains(pr.getPid())) {
                // Update existing rate
                for (CustomerProductRate rate : existingRates) {
                    if (rate.getProduct().getId().equals(pr.getPid())) {
                        rate.setCustomRate(pr.getCustomerRate()); // Update the rate with the new value
                        break; // Exit the loop once found
                    }
                }
            } else {
                // Create a new CustomerProductRate if it doesn't exist
                CustomerProductRate customerProductRate = new CustomerProductRate();
                customerProductRate.setCustomer(optionalCustomer.get());

                // Check if the product exists
                Optional<Product> optionalProduct = productRepo.findById(pr.getPid());
                if (optionalProduct.isEmpty()) throw new RuntimeException("Product does not exist");
                customerProductRate.setProduct(optionalProduct.get());
                customerProductRate.setCustomRate(pr.getCustomerRate());

                // Save the new rate
                customerProductRateRepo.save(customerProductRate);
            }
        }

        // Save all updated rates (if any)
        customerProductRateRepo.saveAll(existingRates);
        return "Rates added/updated successfully";
    }


//    public String updateCustomerProductRate(Long custId, ProductRate productRates) {
//
//
//        Optional<CustomerProductRate> customerProductRate1= customerProductRateRepo.findByCustomerIdAndPId(custId,
//                productRates.getPid());
//        if(customerProductRate1.isEmpty()) throw new RuntimeException("No Product with this pid  for this customer ");
//
//        CustomerProductRate customerProductRate=customerProductRate1.get();
//
//        customerProductRate.setCustomRate(productRates.getCustomerRate());
//        return "ProductUpdated";
//    }
}

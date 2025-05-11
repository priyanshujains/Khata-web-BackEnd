package com.example.KhataWeb.Service;

import com.example.KhataWeb.Dtos.OrderItemRequest;
import com.example.KhataWeb.Models.*;
import com.example.KhataWeb.Repos.CustomerProductRateRepo;
import com.example.KhataWeb.Repos.CustomerRepos;
import com.example.KhataWeb.Repos.ProductRepo;
import com.example.KhataWeb.Repos.ReceiptRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ReceiptServiceImpl {


    private final CustomerRepos customerRepository;
    private final ProductRepo productRepository;
    private final ReceiptRepo receiptRepository;
    private final CustomerProductRateRepo customerProductRateRepository;
   // private final CustomerProductRateRepository customerProductRateRepository;

    @Autowired
    public ReceiptServiceImpl(CustomerRepos customerRepository,
                              ProductRepo productRepository,
                              ReceiptRepo receiptRepository,
                              CustomerProductRateRepo customerProductRateRepository){
        this.customerRepository=customerRepository;
        this.receiptRepository=receiptRepository;
        this.productRepository=productRepository;
        this.customerProductRateRepository=customerProductRateRepository;
    }


    public Receipt addReceipt(Long customerId, List<OrderItemRequest> inputItems, boolean delivery) {

        Optional<Customer> customer1 = customerRepository.findById(customerId);
        if(customer1.isEmpty())throw new RuntimeException("Customer not exist");
        Customer customer=customer1.get();


        // Fetch all custom rates for this customer
        List<CustomerProductRate> customRates = customerProductRateRepository.findAllByCustomer(customer);

        // Convert to Map<productId, rate>
        Map<Long, Double> productRateMap = new HashMap<>();
        for (CustomerProductRate rate : customRates) {
            productRateMap.put(rate.getProduct().getId(), rate.getCustomRate());
        }

        List<OrderItem> finalItemList = new ArrayList<>();
        double totalAmount = 0;

        for (OrderItemRequest itemReq : inputItems) {
            System.out.println(itemReq.getPId());
            Product product = productRepository.findById(itemReq.getPId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            long quantity = itemReq.getQuantity();
            double rate = productRateMap.getOrDefault(product.getId(), product.getBasePrice());
            double totalPrice = rate * quantity;

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setRate(rate);
            orderItem.setQuantity(quantity);
            orderItem.setTotalPrice(totalPrice);

            finalItemList.add(orderItem);
            totalAmount += totalPrice;
        }

        Receipt receipt = new Receipt();
        receipt.setCustomer(customer);
        receipt.setItemList(finalItemList);
        receipt.setTotalAmount(totalAmount);
        receipt.setDelivery(delivery);
        receipt.setCreatedAt(LocalDateTime.now());

        return receiptRepository.save(receipt);
    }

}

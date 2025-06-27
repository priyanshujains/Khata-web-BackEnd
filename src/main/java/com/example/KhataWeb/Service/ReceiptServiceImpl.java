package com.example.KhataWeb.Service;

import com.example.KhataWeb.Dtos.ItemListDto;
import com.example.KhataWeb.Dtos.OrderItemRequest;
import com.example.KhataWeb.Dtos.ReceiptDto;
import com.example.KhataWeb.Models.*;
import com.example.KhataWeb.Repos.CustomerProductRateRepo;
import com.example.KhataWeb.Repos.CustomerRepos;
import com.example.KhataWeb.Repos.ProductRepo;
import com.example.KhataWeb.Repos.ReceiptRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class ReceiptServiceImpl {


    private final CustomerRepos customerRepository;
    private final ProductRepo productRepository;
    private final ReceiptRepo receiptRepository;
    private final CustomerProductRateRepo customerProductRateRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    private final String RECEIPT_KEY_PREFIX = "receipt:";

    @Autowired
    public ReceiptServiceImpl(CustomerRepos customerRepository,
                              ProductRepo productRepository,
                              ReceiptRepo receiptRepository,
                              CustomerProductRateRepo customerProductRateRepository,
                              RedisTemplate redisTemplate){
        this.customerRepository=customerRepository;
        this.receiptRepository=receiptRepository;
        this.productRepository=productRepository;
        this.customerProductRateRepository=customerProductRateRepository;
        this.redisTemplate=redisTemplate;
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

            if(product.getQuantity()<itemReq.getQuantity())throw new RuntimeException("Quantity is Less");
            long quantity = itemReq.getQuantity();
            double rate = productRateMap.getOrDefault(product.getId(), product.getBasePrice());
            double totalPrice = rate * quantity;

            product.setQuantity(product.getQuantity()-itemReq.getQuantity());
            productRepository.save(product);

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

        Receipt r= receiptRepository.save(receipt);

        // Invalidate Redis after new receipt added
        String redisKey = RECEIPT_KEY_PREFIX + customer.getId();
        redisTemplate.delete(redisKey);
        System.out.println("🧹 Cleared receipt cache for customer " + customer.getId());

        return r;
    }

    public List<ReceiptDto> getAllReceipt(Long cusId) {

        String redisKey = RECEIPT_KEY_PREFIX + cusId;

        // Try from Redis
        Object cached = redisTemplate.opsForValue().get(redisKey);
        if (cached != null) {
            System.out.println("✅ Fetched receipts from Redis");
            return (List<ReceiptDto>) cached;
        }

        // Not in Redis → Load from DB
        Optional<Customer> optionalCustomer=customerRepository.findById(cusId);
        if(optionalCustomer.isEmpty())throw new RuntimeException("Customer not exist");

        List<Receipt> receipts= receiptRepository.findAllByCustomer(optionalCustomer.get());

        List<ReceiptDto> receiptDtos=new ArrayList<>();

        for(Receipt receipt: receipts){
            ReceiptDto receiptDto=new ReceiptDto();
            receiptDto.setReceiptId(receipt.getReceiptId());
            receiptDto.setCusName(receipt.getCustomer().getName());
            receiptDto.setDelivery(receipt.isDelivery());
            receiptDto.setTotalAmount(receipt.getTotalAmount());
            receiptDto.setCreatedAt(receipt.getCreatedAt());
            List<ItemListDto> itemListDtos=new ArrayList<>();
            for(OrderItem orderItem:receipt.getItemList()){
                ItemListDto itemListDto=new ItemListDto();
                itemListDto.setProduct(orderItem.getProduct().getName());
                itemListDto.setQuantity(orderItem.getQuantity());
                itemListDto.setRate(orderItem.getRate());
                itemListDto.setTotalPrice(orderItem.getTotalPrice());

                itemListDtos.add(itemListDto);
            }
            receiptDto.setItemLists(itemListDtos);
            receiptDtos.add(receiptDto);
        }

        // Store in Redis with TTL (e.g. 15 minutes)
        redisTemplate.opsForValue().set(redisKey, receiptDtos); // 15, TimeUnit.MINUTES);
        System.out.println("📦 Stored in Redis");

        return receiptDtos;
    }

    public void deleteAReceipt(Long rId) {
        Optional<Receipt> optionalReceipt = receiptRepository.findById(rId);
        if (optionalReceipt.isEmpty()) throw new RuntimeException("Receipt does not exist");

        Long customerId = optionalReceipt.get().getCustomer().getId();
        receiptRepository.deleteById(rId);

        redisTemplate.delete(RECEIPT_KEY_PREFIX + customerId);
        System.out.println("🧹 Cleared receipt cache for customer " + customerId);
    }

    public Receipt getSingleReceipt(Long rId) {
        Optional<Receipt> optionalReceipt=receiptRepository.findById(rId);
        if(optionalReceipt.isEmpty())throw new RuntimeException("REceipt doesnot exixt");

        return optionalReceipt.get();
    }
}

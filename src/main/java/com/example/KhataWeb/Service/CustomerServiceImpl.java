package com.example.KhataWeb.Service;

import com.example.KhataWeb.Models.Customer;
import com.example.KhataWeb.Repos.CustomerRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepos customerRepos;

    @Autowired
    public CustomerServiceImpl(CustomerRepos customerRepos){
        this.customerRepos=customerRepos;
    }

    public List<Customer> getAllCustomers(){
        return customerRepos.findAll();
    }

    public Customer getCustomerById(Long custId){
       Optional<Customer> optionalCustomer= customerRepos.findById(custId);

       if(optionalCustomer.isEmpty()){
           throw new RuntimeException("Customer not found");

       }
       return optionalCustomer.get();
    }

    public Customer addCustomer(Customer customer){
        return customerRepos.save(customer);
    }

    public Customer updateCustomer(Long custId,Customer updatedcustomer){
        Customer customer=getCustomerById(custId);

        customer.setName(updatedcustomer.getName());
        customer.setEmail(updatedcustomer.getEmail());
        customer.setAddress(updatedcustomer.getAddress());
        customer.setDescription(updatedcustomer.getDescription());
        customer.setContact(updatedcustomer.getContact());
        //customer.setDeleted(updatedcustomer.isDeleted());
        return customerRepos.save(customer);



    }

    public void deleteCustomer(Long custId){
        Customer customer=getCustomerById(custId);
         customerRepos.deleteById(custId);
         return;
    }

}

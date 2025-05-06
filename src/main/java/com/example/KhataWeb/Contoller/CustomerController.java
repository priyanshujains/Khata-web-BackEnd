
package com.example.KhataWeb.Contoller;

import com.example.KhataWeb.Models.Customer;
import com.example.KhataWeb.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/api/customer")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService){
        this.customerService=customerService;
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers(){
        List<Customer> customers=customerService.getAllCustomers();
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }


    @GetMapping("/{custId}")
    public ResponseEntity<Customer> getProductById(@PathVariable("custId") Long custId){
        try{
            Customer customer=customerService.getCustomerById(custId);
            return new ResponseEntity<>(customer,HttpStatus.OK);
        }catch(RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

   }

    @PostMapping
    public ResponseEntity<Customer> addCustomer(@RequestBody Customer customer ){
        Customer newcustomer=customerService.addCustomer(customer);
        return new ResponseEntity<>(newcustomer,HttpStatus.CREATED);
    }

    @PutMapping("/{custId}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable("custId") Long custId, @RequestBody Customer customer){
        try{
            Customer updatedCustomer=customerService.updateCustomer(custId,customer);
            return new ResponseEntity<>(updatedCustomer,HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{custId}")
    public ResponseEntity<?> deleteCustomer(@PathVariable("custId") Long custId){
        try{
            customerService.deleteCustomer(custId);
            return new ResponseEntity<>(HttpStatus.OK);

        }catch (RuntimeException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }






}


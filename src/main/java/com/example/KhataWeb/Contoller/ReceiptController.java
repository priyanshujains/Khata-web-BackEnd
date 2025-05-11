package com.example.KhataWeb.Contoller;

import com.example.KhataWeb.Dtos.OrderItemRequest;
import com.example.KhataWeb.Service.ReceiptServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/receipt")
public class ReceiptController {

    private final ReceiptServiceImpl receiptService;

    @Autowired
    public ReceiptController(ReceiptServiceImpl receiptService) {
        this.receiptService = receiptService;
    }


    @PostMapping("/user/{cusId}")
    public ResponseEntity<?> addAReceipt(@PathVariable("cusId") Long cusId, @RequestBody List<OrderItemRequest> orderItemRequests, @RequestParam
                                             boolean delivery){
        if (cusId == null) {
            throw new IllegalArgumentException("     id must not be null");
        }
                System.out.println(orderItemRequests);
                return new ResponseEntity<>(receiptService.addReceipt(cusId,orderItemRequests,delivery), HttpStatus.OK);
    }



    @GetMapping("/user/{cusId}")
    public ResponseEntity<?> getAllReceiptOfCustomer(@PathVariable Long cusId){
        return new ResponseEntity<>(receiptService.getAllReceipt(cusId),HttpStatus.OK);
    }


    @GetMapping("/{rId}")
    public ResponseEntity<?> getReceiptOfCustomer(@PathVariable Long rId){
        return new ResponseEntity<>(receiptService.getSingleReceipt(rId),HttpStatus.OK);
    }

    @DeleteMapping("/{rId}")
    public ResponseEntity<HttpStatus> deleteAReceipt(@PathVariable Long rId){
        receiptService.deleteAReceipt(rId);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}

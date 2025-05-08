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


    @PostMapping("/{cusId}")
        public ResponseEntity<?> addAReceipt(Long cusId, @RequestBody List<OrderItemRequest> orderItemRequests, @RequestParam
                                             boolean delivery){
                return new ResponseEntity<>(receiptService.addReceipt(cusId,orderItemRequests,delivery), HttpStatus.OK);
        }
}

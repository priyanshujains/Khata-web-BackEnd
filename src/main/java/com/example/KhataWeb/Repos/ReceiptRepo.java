package com.example.KhataWeb.Repos;

import com.example.KhataWeb.Models.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceiptRepo extends JpaRepository<Receipt,Long> {
}

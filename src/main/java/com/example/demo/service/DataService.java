package com.example.demo.service;

import com.example.demo.model.Item;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class DataService {

    public List<Item> generateSampleData(int count) {
        List<Item> items = new ArrayList<>();
        String[] products = {"Laptop", "Mouse", "Keyboard", "Monitor", "Desk",
                "Chair", "Printer", "Scanner", "Headphones", "Webcam"};
        BigDecimal[] prices = {BigDecimal.valueOf(999.99), BigDecimal.valueOf(29.99), BigDecimal.valueOf(79.99), BigDecimal.valueOf(299.99), BigDecimal.valueOf(199.99),
                BigDecimal.valueOf(149.99), BigDecimal.valueOf(199.99), BigDecimal.valueOf(129.99), BigDecimal.valueOf(89.99), BigDecimal.valueOf(79.99)};

        for (int i = 1; i <= count; i++) {
            int productIndex = i % products.length;
            items.add(new Item(
                    (long) i,
                    products[productIndex] + " " + UUID.randomUUID().toString().substring(0, 4),
                    prices[productIndex],
                    (int)(Math.random() * 100) + 1
            ));
        }
        return items;
    }

    public String getCurrentDate() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public String getCurrentTime() {
        return LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }
}
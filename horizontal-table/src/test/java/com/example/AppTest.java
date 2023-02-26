package com.example;


import com.example.entity.Order;
import com.example.mapper.OrderMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

/**
 * Unit test for simple App.
 */
@SpringBootTest
public class AppTest
{
    @Autowired
    private OrderMapper orderMapper;

    @Test
    public void insertTest() {
        for (int i = 0; i < 10; i++) {
            Order order = new Order();
            order.setAmount(BigDecimal.TEN);
            order.setOrderNo(String.valueOf(i));
            order.setUserId(Long.valueOf(i));
            orderMapper.insert(order);
        }
    }
}

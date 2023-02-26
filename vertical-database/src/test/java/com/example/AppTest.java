package com.example;


import com.example.entity.Order;
import com.example.entity.User;
import com.example.mapper.OrderMapper;
import com.example.mapper.UserMapper;
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
    private UserMapper userMapper;
    @Autowired
    private OrderMapper orderMapper;

    @Test
    public void insertTest() {
        User user = new User();
        user.setUname("垂直分库");
        // 主库写
        userMapper.insert(user);

        Order order = new Order();
        order.setAmount(BigDecimal.TEN);
        order.setOrderNo("order_no_1");
        order.setUserId(Long.valueOf(1));
        orderMapper.insert(order);
    }

}

package com.example;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.Order;
import com.example.entity.OrderItem;
import com.example.mapper.OrderItemMapper;
import com.example.mapper.OrderMapper;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

/**
 * Unit test for simple App.
 */
@SpringBootTest
public class AppTest
{
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;

    @Test
    public void insertTest() {
        Random random = new Random();
        /**
         * 10 个用户
         */
        for (int i = 0; i < 10; i++) {
            for (int j = 100; j > 0; j--) {
                String orderNo = "order_no_" + j;
                Order order = new Order();
                order.setOrderNo(orderNo);
                order.setUserId(Long.valueOf(i));
                BigDecimal sum = BigDecimal.ZERO;

                for (int i1 = 100; i1 > 0; i1--) {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrderNo(orderNo);
                    orderItem.setUserId(Long.valueOf(i));
                    orderItem.setPrice(new BigDecimal(random.nextInt(10000)));
                    orderItem.setCount(random.nextInt(10));
                    sum = sum.add(orderItem.getPrice().multiply(new BigDecimal(orderItem.getCount())));
                    orderItemMapper.insert(orderItem);
                }
                orderMapper.insert(order);
            }
        }
    }

    @Test
    public void selectTest() {
        Page<Order> page = new Page<>(1,10);
        Page<Order> pageResult = orderMapper.selectPage(page, new LambdaQueryWrapper<Order>()
                .eq(Order::getUserId, 1));
        System.out.println(pageResult);
    }
}

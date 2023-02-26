package com.example;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.entity.Order;
import com.example.entity.OrderItem;
import com.example.mapper.OrderItemMapper;
import com.example.mapper.OrderMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
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
        String orderNo = "order_no_" + 5;
        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserId(Long.valueOf(20));
        BigDecimal sum = BigDecimal.ZERO;

        for (int i1 = 2; i1 > 0; i1--) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderNo(orderNo);
            orderItem.setUserId(Long.valueOf(20));
            orderItem.setPrice(new BigDecimal(random.nextInt(10000)));
            orderItem.setCount(random.nextInt(10));
            sum = sum.add(orderItem.getPrice().multiply(new BigDecimal(orderItem.getCount())));
            // orderItemMapper.insert(orderItem);
        }
        order.setAmount(sum);
        orderMapper.insert(order);
    }

    @Test
    public void selectTest() {
        // 按照user_id查询，会查询影子库 按照其他条件查询会查询主库
        Order order = orderMapper.selectOne(new LambdaQueryWrapper<Order>()
                .eq(Order::getOrderNo, "order_no_4"));
        System.out.println(order);
        // 保存到影子库
        orderMapper.insert(order);
        // 查询影子库
        order = orderMapper.selectOne(new LambdaQueryWrapper<Order>()
                        .eq(Order::getUserId, 4)
                .eq(Order::getOrderNo, "order_no_4"));
        System.out.println(order);
        // 更新影子库
        orderMapper.update(null, new LambdaUpdateWrapper<Order>()
                .eq(Order::getUserId, 4)
                .eq(Order::getOrderNo, "order_no_4")
                .set(Order::getOrderNo, "order_no_4_1"));
    }
}

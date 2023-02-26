package com.example;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
    OrderMapper orderMapper;

    @Test
    public void insertTest() {
        Order order = new Order();
        order.setAmount(BigDecimal.TEN);
        order.setOrderNo("order_no_3");
        order.setPhone("123456789");
        order.setUserId(1L);
        orderMapper.insert(order);


    }

    @Test
    public void selectTest() {
        /**
         * 设置了明文列和密文列query-with-cipher-column: false
         * 通过明文查询
         * select `t_order`.`id`, `t_order`.`order_no`, `t_order`.`user_id`, `t_order`.`amount`, `t_order`.`phone` AS `phone` from t_order where phone = ? ::: [123456]
         * Order(id=1626835378491117569, orderNo=order_no_1, userId=1, amount=10.00, phone=123456, phoneCipher=null)
         * 通过密文查询
         *select `t_order`.`id`, `t_order`.`order_no`, `t_order`.`user_id`, `t_order`.`amount`, `t_order`.`phone` AS `phone` from t_order where phone = ? ::: [MyOShk4kjRnds7CZfU5NCw==]
         * 无法查询到信息
         * 设置了明文列和密文列query-with-cipher-column: true
         * 通过明文查询
         * select `t_order`.`id`, `t_order`.`order_no`, `t_order`.`user_id`, `t_order`.`amount`, `t_order`.`phone_cipher` AS `phone` from t_order where phone_cipher = ? ::: [MyOShk4kjRnds7CZfU5NCw==]
         * Order(id=1626835378491117569, orderNo=order_no_1, userId=1, amount=10.00, phone=123456, phoneCipher=null)
         * 通过密文查询
         * select `t_order`.`id`, `t_order`.`order_no`, `t_order`.`user_id`, `t_order`.`amount`, `t_order`.`phone_cipher` AS `phone` from t_order where phone_cipher = ? ::: [/zH5QKw/DCDTIqw4toaTsuKSdPtgZy+KvsEpQJ6lKmI=]
         * 无法查询到信息
         */
        Order order1 = orderMapper.selectByPhone("123456");
        System.out.println(order1);
    }


    @Test
    public void selectEncryptTest() {
        /**
         * mybatis lambda查询无效
         * 设置了密文列，query-with-cipher-column: false
         * 通过密文查询
         * select `t_order`.`id`, `t_order`.`order_no`, `t_order`.`user_id`, `t_order`.`amount`, `t_order`.`phone_cipher` AS `phone` from t_order where phone_cipher = ? ::: [MyOShk4kjRnds7CZfU5NCw==]
         * Order(id=1626835378491117569, orderNo=order_no_1, userId=1, amount=10.00, phone=MyOShk4kjRnds7CZfU5NCw==, phoneCipher=null)
         * 通过明文查询
         * select `t_order`.`id`, `t_order`.`order_no`, `t_order`.`user_id`, `t_order`.`amount`, `t_order`.`phone_cipher` AS `phone` from t_order where phone_cipher = ? ::: [123456]
         * 无法查询到信息
         * 设置了密文列，query-with-cipher-column: true
         * 通过明文查询
         * select `t_order`.`id`, `t_order`.`order_no`, `t_order`.`user_id`, `t_order`.`amount`, `t_order`.`phone_cipher` AS `phone` from t_order where phone_cipher = ? ::: [MyOShk4kjRnds7CZfU5NCw==]
         * 得到结果Order(id=1626835378491117569, orderNo=order_no_1, userId=1, amount=10.00, phone=123456, phoneCipher=null)
         * 通过密文查询
         * select `t_order`.`id`, `t_order`.`order_no`, `t_order`.`user_id`, `t_order`.`amount`, `t_order`.`phone_cipher` AS `phone` from t_order where phone_cipher = ? ::: [/zH5QKw/DCDTIqw4toaTsuKSdPtgZy+KvsEpQJ6lKmI=]
         * 无法查到信息
         */
        Order order1 = orderMapper.selectByPhone("123456");
        System.out.println(order1);
    }
}

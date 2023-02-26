package com.example.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @author lixiaoqiang
 */
@TableName(value = "t_order")
@Getter
@Setter
@ToString
public class Order {
    private Long id;
    private String orderNo;
    private Long userId;
    private BigDecimal amount;

    private String phone;

    private String phoneCipher;

    // private String phoneAssistedQuery;

}

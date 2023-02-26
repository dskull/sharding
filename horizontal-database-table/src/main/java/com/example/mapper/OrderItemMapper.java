package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.OrderItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author lixiaoqiang
 */
@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItem> {
}

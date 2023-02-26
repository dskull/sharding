package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Order;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author lixiaoqiang
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {
}

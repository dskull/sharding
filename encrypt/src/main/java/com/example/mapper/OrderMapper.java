package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author lixiaoqiang
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    /**
     *
     * @return
     */
    @Select("select * from t_order where phone = #{phone}")
    Order selectByPhone(@Param("phone") String phone);
}

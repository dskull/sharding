package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author lixiaoqiang
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    /**
     * 分页查询测试
     * @param userId
     * @param start
     * @param end
     * @return
     */

    @Select("select * from t_order where user_id = #{userId} limit #{start}, #{end}")
    List<Order> selectPageTest(@Param("userId") Long userId, @Param("start") Long start, @Param("end") Long end);
}

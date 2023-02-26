package com.example.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author lixiaoqiang
 */
@Getter
@Setter
@ToString
@TableName(value = "t_dict")
public class Dict {
    private Long id;
    private String dictType;
}

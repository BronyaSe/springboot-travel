package com.bronya.travel.Entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.JdbcType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Route {

    @NotBlank
    private Integer id;  // 路线ID

    @NotBlank
    private String name;  // 路线名称

    private String description;  // 路线描述

    @NotBlank
    private String cover;

    @TableField(value ="detailPic" ,typeHandler = JacksonTypeHandler.class,jdbcType = JdbcType.VARCHAR)
    private List<String> detailPic;

    @NotBlank
    private BigDecimal price;  // 路线价格

    @NotBlank
    private String category;  // 路线类别

    @NotBlank
    private BigDecimal rating = BigDecimal.ZERO;  // 平均评分

    private Integer duration;  // 游玩时长（天数）

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;  // 创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}

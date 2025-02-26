package com.bronya.travel.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bronya.travel.Entity.Route;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RouteMapper extends BaseMapper<Route> {


    @Select("select id,name,price,category,rating,cover,LEFT(description,55) AS description From route")
    List<Route> findByPage(Page<Route> page);

    @Select("select * from route where id=#{id}")
    @Result(column = "detailPic",typeHandler = JacksonTypeHandler.class,property = "detailPic")
    Route findById(String id);
}

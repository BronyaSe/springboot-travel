package com.bronya.travel.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bronya.travel.Entity.DTO.RouteCommentPageDTO;
import com.bronya.travel.Entity.Route;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RouteMapper extends BaseMapper<Route> {


    @Select("select id,name,price,category,rating,cover,LEFT(description,55) AS description From route")
    List<Route> findByPage(Page<Route> page);

    @Select("select * from route where id=#{id}")
    @Result(column = "detailPic",typeHandler = JacksonTypeHandler.class,property = "detailPic")
    Route findById(String id);

    @Select("select COUNT(*) from route_reviews where route_id=#{id}")
    int findratingCountByid(String id);

    @Select("select r.id,r.content,r.rating,r.createdAt,u.username,u.avatar from route_reviews r inner join users u on r.user_id=u.id where r.route_id=#{routeid}")
    List<RouteCommentPageDTO> findCommentByPage(Page<RouteCommentPageDTO> page , @Param("routeid") String routeid);

    @Insert("INSERT INTO user_favorites (user_id,location_id) values (#{id},#{routeid})")
    void addFavorite(String id, String routeid);

}

package com.bronya.travel.Service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bronya.travel.Entity.DTO.RouteCommentPageDTO;
import com.bronya.travel.Entity.Route;
import com.bronya.travel.Mapper.RouteMapper;
import com.bronya.travel.Service.RouteService;
import com.bronya.travel.Utils.ThreadLocalUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class RouteImpl implements RouteService{

    @Autowired
    private RouteMapper routeMapper;

    @Override
    @Cacheable(value = "routePageCache",key="#page.current+'_'+#page.size")
    public List<Route> RoutefindByPage(Page<Route> page){
        return routeMapper.findByPage(page);
    }

    @Override
    @Cacheable(value = "routeDetail",key = "#id")
    public Route findRouteById(String id) {
        return routeMapper.findById(id);
    }

    @Override
    public int getratingCountByid(String id) {
        return routeMapper.findratingCountByid(id);
    }


    @Override
    @Cacheable(value = "routeCommentCache",key="#routeid+'_'+#page.current+'_'+#page.size")
    public List<RouteCommentPageDTO> RouteCommentfindByPage(Page<RouteCommentPageDTO> page, String routeid) {
        return routeMapper.findCommentByPage(page,routeid);
    }

    @Override
    public void addFavorite(String routeid) {
        Claims tempclaim = ThreadLocalUtils.get();
        String id = tempclaim.get("id").toString();
        routeMapper.addFavorite(id,routeid);
    }

    @Override
    public List<Route> getRouteBySearchPage(int index, int size , String search) {
        Page<Route> page = new Page<>(index,size);
        LambdaQueryWrapper<Route> queryWrapper = new LambdaQueryWrapper<>();
        if (search != null && !search.isEmpty()) {
            queryWrapper.like(Route::getName,search);
        }
        queryWrapper.select(Route::getName,Route::getId,Route::getPrice,Route::getCategory,Route::getDuration);
        return routeMapper.selectList(page, queryWrapper);
    }
}

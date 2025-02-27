package com.bronya.travel.Service.Impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bronya.travel.Entity.Route;
import com.bronya.travel.Mapper.RouteMapper;
import com.bronya.travel.Service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class RouteImpl implements RouteService{

    @Autowired
    private RouteMapper routeMapper;

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

    ;
}

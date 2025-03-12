package com.bronya.travel.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bronya.travel.Entity.DTO.RouteCommentPageDTO;
import com.bronya.travel.Entity.Route;

import java.util.List;

public interface RouteService {
    List<Route> RoutefindByPage(Page<Route> page);
    Route findRouteById(String id);

    int getratingCountByid(String id);

    List<RouteCommentPageDTO> RouteCommentfindByPage(Page<RouteCommentPageDTO> page, String routeid);
}

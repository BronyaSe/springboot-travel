package com.bronya.travel.Controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bronya.travel.Entity.DTO.RouteCommentPageDTO;
import com.bronya.travel.Entity.Result;
import com.bronya.travel.Entity.Route;
import com.bronya.travel.Service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/route")
public class RouteController {

    @Autowired
    private RouteService routeService;

    @PostMapping("/GetPage")
        public Result<List<Route>> routeBypage(long index,long size){
            Page<Route> page = new Page<>(index,size);
            List<Route> result = routeService.RoutefindByPage(page);
            return Result.success(result);
    }

    @PostMapping("/GetRouteComment")
    public Result<List<RouteCommentPageDTO>> routeComment(long index,long size,String routeid){
        Page<RouteCommentPageDTO> page = new Page<>(index,size);
        List<RouteCommentPageDTO> result = routeService.RouteCommentfindByPage(page,routeid);
        return Result.success(result);
    }
    @GetMapping("/getdetail/{id}")
    public Result<Route> routeDetail(@PathVariable String id){
        try {
            Route item = routeService.findRouteById(id);
            if(item != null){
                return Result.success(item);
            }else {
                return Result.error("空对象");
            }
        }catch (Exception e){
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/getroutecount/{id}")
    public Result<Integer> routerating(@PathVariable String id){
     int count = routeService.getratingCountByid(id);
     return Result.success(count);
    }
}

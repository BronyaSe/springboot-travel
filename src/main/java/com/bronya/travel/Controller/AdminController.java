package com.bronya.travel.Controller;


import com.bronya.travel.Entity.Result;
import com.bronya.travel.Entity.Route;
import com.bronya.travel.Service.AdminService;
import com.bronya.travel.Service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private RouteService routeService;
    @Autowired
    private AdminService adminService;

    @PostMapping("getAllRouteByPage")
    public Result<List<Route>> getRouteByPage(@RequestParam int index, @RequestParam int size,String search) {
        List<Route> routeBySearchPage = routeService.getRouteBySearchPage(index, size, search);
        return Result.success(routeBySearchPage);
    }
    @PostMapping("updateDetail")
    public Result<String> updateDetailInfo(
            @RequestPart() Route route ,
            @RequestPart(value = "cover",required = false) MultipartFile cover,
            @RequestPart(value = "detail",required = false) MultipartFile[] detail) {
        adminService.updateRouteDetail(route,cover,detail);
        return Result.success();
    }
    @DeleteMapping("deleteDetailPic")
    public Result<String> deleteByHttpurl(@RequestParam String url,@RequestParam int id){
        adminService.deleteWithUpdateDate(id,url);
        return Result.success();
    }
}

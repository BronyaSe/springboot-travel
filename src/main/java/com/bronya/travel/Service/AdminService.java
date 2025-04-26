package com.bronya.travel.Service;

import com.bronya.travel.Entity.Route;
import org.springframework.web.multipart.MultipartFile;

public interface AdminService {

    void updateRouteDetail(Route route, MultipartFile cover, MultipartFile[] detail);
    void deleteByHttpUrl(String oldpichttpurl);
    void deleteWithUpdateDate(int id,String oldpichttpurl);
}

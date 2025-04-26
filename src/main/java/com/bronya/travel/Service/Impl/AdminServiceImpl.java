package com.bronya.travel.Service.Impl;

import com.bronya.travel.Entity.Route;
import com.bronya.travel.Mapper.RouteMapper;
import com.bronya.travel.Service.AdminService;
import com.bronya.travel.Utils.SubFileExtend;
import com.github.sardine.Sardine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private RouteMapper routeMapper;
    @Autowired
    private SubFileExtend subFileExtend;
    @Autowired
    private String webDavBaseUrl;
    @Autowired
    private Sardine sardine;
    @Autowired
    private String PicBaseUrl;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Override
    @Transactional
    public void updateRouteDetail(Route route, MultipartFile cover, MultipartFile[] detail) {

        // 当文件为空时，仅更新用户信息
        if ((cover == null || cover.isEmpty())&&(detail == null || detail.length<1)) {
            routeMapper.updateById(route);
            stringRedisTemplate.delete("routeDetail::"+route.getId());
            return;
        }
        if(!(cover==null||cover.isEmpty())){
            String coverurl = upload(cover, "cover");
            deleteByHttpUrl(route.getCover());
            route.setCover(coverurl);
        }

        List<String> newdetail = new ArrayList<>();
        if(!(detail==null||detail.length<1)){
            for(MultipartFile item:detail){
                String url = upload(item,"detail");
                newdetail.add(url);
            }
            //        List<String> olddetail = route.getDetailPic();
            newdetail.addAll(route.getDetailPic());
            route.setDetailPic(newdetail);
        }
        routeMapper.updateById(route);
        stringRedisTemplate.delete("routeDetail::"+route.getId());
    }

    public String upload(MultipartFile file,String foldpath){
        boolean fileUploaded = false;
        String uuid = UUID.randomUUID().toString();

        String newpicwebdavurl = webDavBaseUrl+foldpath+'/'+uuid+subFileExtend.subFileExtend(file);//新图片webdavurl
        try {
            sardine.put(newpicwebdavurl,file.getInputStream());
            fileUploaded = true;
//            route.setCover(PicBaseUrl+"cover/"+uuid+coverextend);
//            if(!oldurl.substring(lastSlashIndex + 1).equals("default.jpeg")){
//                sardine.delete(oldPicWebDavUrl);
//            }
            return PicBaseUrl+foldpath+'/'+uuid+subFileExtend.subFileExtend(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }catch (Exception e) {
            if(fileUploaded){
                try {
                    sardine.delete(newpicwebdavurl);
                } catch (IOException ex) {
                    throw new RuntimeException("头像删除回滚失败:"+newpicwebdavurl);
                }
            }
            throw new RuntimeException("图片上传失败", e);
        }
    }

    public void deleteByHttpUrl(String oldpichttpurl){
        int lastSlashIndex = oldpichttpurl.lastIndexOf("/");//http最后一段
        int secondLastSlashIndex = oldpichttpurl.lastIndexOf('/', lastSlashIndex - 1);
        String oldPicWebDavUrl=webDavBaseUrl+oldpichttpurl.substring(secondLastSlashIndex+1,lastSlashIndex)+'/'+oldpichttpurl.substring(lastSlashIndex + 1);//webdav的旧图片url
        try {
            if(!oldpichttpurl.substring(lastSlashIndex + 1).equals("default.jpeg")){
               sardine.delete(oldPicWebDavUrl);
            }
        } catch (IOException e) {
            throw new RuntimeException("图片删除失败:"+oldPicWebDavUrl,e);
        }
    }

    @Override
    @Transactional
    public void deleteWithUpdateDate(int id,String oldpichttpurl) {
        Route route = routeMapper.findById(String.valueOf(id));
        List<String> detailPic = route.getDetailPic();
        detailPic.remove(oldpichttpurl);
        route.setDetailPic(detailPic);
        routeMapper.updateById(route);
        deleteByHttpUrl(oldpichttpurl);
    }

}

package com.bronya.travel.Utils;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class SubFileExtend {

    public String subFileExtend(MultipartFile file) {
        // 1. 获取并验证原始文件名
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new IllegalArgumentException("文件名不能为空");
        }

        // 2. 去除路径信息（兼容Windows/Linux路径格式）
        int lastPathSeparator = Math.max(
                originalFilename.lastIndexOf('/'),
                originalFilename.lastIndexOf('\\')
        );
        String pureFilename = (lastPathSeparator != -1)
                ? originalFilename.substring(lastPathSeparator + 1)
                : originalFilename;

        // 3. 提取文件后缀
        int extensionIndex = pureFilename.lastIndexOf('.');
        if (extensionIndex == -1) {
            throw new IllegalArgumentException("文件缺少后缀名");
        }
        String fileExtension = pureFilename.substring(extensionIndex);
        return fileExtension;
    }
}

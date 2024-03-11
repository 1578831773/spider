package com.example.demo.util;

import org.springframework.beans.factory.annotation.Value;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class CommonUtil {
    @Value("${photo.file.dir}")
    private static String realPath;

    public static String getNowTime(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH:MM:ss");
        return df.format(new Date());
    }


//    public static String uploadFile(MultipartFile file) {
//        // 首先校验图片格式
//        List<String> imageType = Lists.newArrayList("jpg","jpeg", "png", "bmp", "gif");
//        // 获取文件名，带后缀
//        String originalFilename = file.getOriginalFilename();
//        // 获取文件的后缀格式
//        String fileSuffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
//        if (imageType.contains(fileSuffix)) {
//            // 只有当满足图片格式时才进来，重新赋图片名，防止出现名称重复的情况
//            // 该方法返回的为当前项目的工作目录，即在哪个地方启动的java线程
//            String dirPath = System.getProperty("user.dir");
//            String path = File.separator + "uploadImg" + File.separator + originalFilename;
//            File destFile = new File(dirPath + path);
//            if (!destFile.getParentFile().exists()) {
//                destFile.getParentFile().mkdirs();
//            }
//            try {
//                file.transferTo(destFile);
//                // 将相对路径返回给前端
//                return path;
//            } catch (IOException e) {
//                return null;
//            }
//        }
//        return null;
//    }

}

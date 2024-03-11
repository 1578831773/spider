package com.example.demo.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
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

    /**
     * 获取属性名数组
     * */
    public static List<String> getFieldName(Object o){
        Field[] fields=o.getClass().getDeclaredFields();
        List<String> fieldNames = new ArrayList<>();
        for(Field field:fields){
            if(field.getType().getName().equals("java.lang.String")) {
                fieldNames.add(field.getName());
            }

        }
        return fieldNames;
    }

    public static String uploadFile(MultipartFile file, PageContext pageContext){
        String fileName = file.getOriginalFilename();
        System.out.println(fileName);
        String filePath = "/static/img/"+fileName;
        File dest = new File(filePath);
        try {
            file.transferTo(dest);
            return "/image/headPic/"+fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
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

    public static String saveImg(MultipartFile file, String name) throws IOException {
        String filename="e:\\IdeaProjects\\myspider\\src\\main\\webapp\\"+name+".jpg";
        BufferedOutputStream out = new BufferedOutputStream(
                new FileOutputStream(new File(filename)));//保存图片到目录下
        out.write(file.getBytes());
        out.flush();
        out.close();
        return filename;

    }

    public static String uploadFile(MultipartFile file, HttpServletRequest request) throws IllegalStateException, IOException{
        if(file!=null){
            String originalFilename = file.getOriginalFilename();
            String newFileName ="";
            String pic_path;
            if ( originalFilename != null && originalFilename.length() > 0) {
                String tomcat_path = System.getProperty( "user.dir" );
                System.out.println(tomcat_path);
                pic_path = tomcat_path+"\\src"+"\\main"+"\\resources"+"\\static"+"\\img\\";
                newFileName =UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
                File newFile = new File(pic_path + newFileName);
                file.transferTo(newFile);
            }
            return newFileName;
        }else{
            return null;
        }
    }
}

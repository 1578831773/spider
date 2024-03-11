package com.example.demo.util;

import java.io.*;

/**
 * Created by liuyikai on 2016/8/13.
 */
public class FileUtil {
    private static String prePath = "/mnt/hgfs/share1/html/";

    public static String LoadContentByPath(String path) throws IOException {
        InputStream is = new FileInputStream(prePath + path + ".txt");
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        StringBuffer buffer = new StringBuffer();
        String line = "";
        while ((line = in.readLine()) != null){
            buffer.append(line);
        }
        return buffer.toString();

    }

}
package com.example.demo.util;

import java.io.FileWriter;
import java.io.IOException;

public class FileUtil {
    private static String path = "/mnt/hgfs/share1/html/";
    public static synchronized void saveAsFileWriter(String content, String name) {
        System.out.println("try to save " + name);
        FileWriter fwriter = null;
        try {
            fwriter = new FileWriter(path + name + ".txt");
            fwriter.write(content);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                fwriter.flush();
                fwriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}

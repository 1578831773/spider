package com.example.demo.parser;

import java.util.List;

/**
 * 页面解析器
 */
public interface PageParser {
    //解析地址
    List<String> parseUrls();

    //解析文章
    List<String> parseArticle();
}

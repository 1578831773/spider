package com.example.demo.mapper;

import com.example.demo.bean.CommArticle;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommMapper {

    @Insert("insert into comm_article(boot_id, url, content, spider_time) " +
            "values(#{commArticle.bootId}, #{commArticle.url}, #{commArticle.content}, #{commArticle.spiderTime})")
    @Options(useGeneratedKeys = true, keyProperty = "articleId")
    void addArticle(@Param("commArticle") CommArticle commArticle);

    @Select("select * from comm_article where boot_id = #{bootId}")
    List<CommArticle> articleList(int bootId);

    @Select("select * from comm_article where boot_id = #{bootId} and url = #{url}")
    List<CommArticle> getArticlesByUrl(int bootId, String url);

    @Select("select * from comm_article where boot_id = #{bootId} and content like concat('%', concat(#{content}, '%'))")
    List<CommArticle> getArticlesByContent(int bootId, String content);
}

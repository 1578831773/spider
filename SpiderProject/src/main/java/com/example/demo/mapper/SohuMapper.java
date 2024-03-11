package com.example.demo.mapper;

import com.example.demo.bean.SohuArticle;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SohuMapper {

    @Insert("insert into sohu_article(`boot_id`, `url`, `title`, `publish_time`, `tags`, `content`, `count`, `spider_time`) " +
            "values(#{bootId}, #{url}, #{title}, #{publishTime}, #{tags}, #{content}, #{count}, #{spiderTime})")
    @Options(useGeneratedKeys = true, keyProperty = "articleId")
    void saveArticle(SohuArticle sohuArticle);

    @Select("select * from sohu_article where boot_id = #{bootId}")
    List<SohuArticle> sohuArticleList(int bootId);

    @Select("select * from sohu_article where if(url = '', 1=1, url = #{url})")
    List<SohuArticle> getArticleListByUrl(String url);

    @Select("select * from sohu_article where content like concat('%',concat(#{content},'%'))")
    List<SohuArticle> getArticleByContent(String content);

}

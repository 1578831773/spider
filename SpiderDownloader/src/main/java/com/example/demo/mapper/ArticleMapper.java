package com.example.demo.mapper;

import com.example.demo.bean.CommArticle;
import com.example.demo.bean.SohuArticle;
import com.example.demo.bean.WeiboArticle;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ArticleMapper {

    @Insert("insert into comm_article(boot_id, url) values(#{commArticle.bootId}, #{commArticle.url})")
    @Options(useGeneratedKeys = true, keyProperty = "articleId")
    void insertCommArticle(@Param("commArticle") CommArticle commArticle);

    @Insert("insert into sohu_article(`boot_id`, `url`) " +
            "values(#{bootId}, #{url})")
    @Options(useGeneratedKeys = true, keyProperty = "articleId")
    void insertSohuArticle(SohuArticle sohuArticle);

    @Insert("insert into weibo_article(boot_id, url) " +
            "values(#{weiboArticle.bootId}, #{weiboArticle.url})")
    @Options(useGeneratedKeys = true, keyProperty = "articleId")
    void insertWeiboArticle(@Param("weiboArticle") WeiboArticle weiboArticle);
}

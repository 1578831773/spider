package com.example.demo.mapper;

import com.example.demo.bean.WeiboArticle;
import com.example.demo.bean.WeiboTopArticle;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface WeiboMapper {
    @Insert("insert into weibo_article(boot_id, nick_name, publish_time, content, comment_num, like_num, spider_time) " +
            "values(#{weiboArticle.bootId}, #{weiboArticle.nickName}, #{weiboArticle.publishTime}, #{weiboArticle.content}, #{weiboArticle.commentNum}, #{weiboArticle.likeNum}, #{weiboArticle.spiderTime})")
    @Options(useGeneratedKeys = true, keyProperty = "articleId")
    void insertArticle(@Param("weiboArticle") WeiboArticle weiboArticle);

    void insertArticleList(@Param("weiboArticleList") List<WeiboArticle> weiboArticles);

    @Insert("insert into weibo_top(url, title, intro, read_num, discuss_num, article_id) " +
            "values(#{weiboTioArticle.url}, #{weiboTioArticle.title}, #{weiboTioArticle.intro}, #{weiboTioArticle.readNum}, #{weiboTioArticle.discussNum}, #{weiboTioArticle.articleId})")
    void insertTopArticle(@Param("weiboTioArticle") WeiboTopArticle weiboTopArticle);

    List<WeiboTopArticle> getTopArticleList(int bootId);
    List<WeiboTopArticle> getTopArticleListByContent(int bootId, String content);
    List<WeiboTopArticle> getTopArticleListByNickName(int bootId, String nickName);

    @Select("select article_id, nick_name, publish_time, content, comment_num, like_num, spider_time " +
            "from weibo_article where boot_id = #{bootId}")
    List<WeiboArticle> getArticleList(int bootId);

    @Select("select article_id, nick_name, publish_time, content, comment_num, like_num, spider_time " +
            "from weibo_article where boot_id = #{bootId} and nick_name like concat('%', concat(#{nickName}, '%'))")
    List<WeiboArticle> getArticleListByNickName(int bootId, String nickName);

    @Select("select article_id, nick_name, publish_time, content, comment_num, like_num, spider_time " +
            "from weibo_article where boot_id = #{bootId} and content like concat('%', concat(#{content}, '%'))")
    List<WeiboArticle> getArticleListByContent(int bootId, String content);
}

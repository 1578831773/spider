package com.example.demo.service;

import com.example.demo.bean.WeiboArticle;
import com.example.demo.bean.WeiboTopArticle;
import com.example.demo.mapper.RecordMapper;
import com.example.demo.mapper.WeiboMapper;
import com.example.demo.util.CommonUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WeiboService {
    @Autowired
    private WeiboMapper weiboMapper;
    @Autowired
    private RecordMapper recordMapper;

    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    public void saveTopArticle(WeiboTopArticle weiboTopArticle) {
        if(checkWeiboArticle(weiboTopArticle.getWeiboArticle())){
            weiboTopArticle.getWeiboArticle().setPublishTime(CommonUtil.getNowTime());
            weiboTopArticle.getWeiboArticle().setSpiderTime(CommonUtil.getNowTime());

            weiboMapper.insertArticle(weiboTopArticle.getWeiboArticle());
            weiboTopArticle.setArticleId(weiboTopArticle.getWeiboArticle().getArticleId());
            weiboMapper.insertTopArticle(weiboTopArticle);
            recordMapper.addArticleCount(weiboTopArticle.getWeiboArticle().getBootId(), 1);
        }
    }

    public List<WeiboTopArticle> getTopWeiboArticleList(int bootId){
        if(bootId <= 0){
            return null;
        }
        return weiboMapper.getTopArticleList(bootId);
    }

    public List<WeiboTopArticle> getTopWeiboArticleListByNickName(int bootId, String nickName){
        if(bootId <= 0){
            return null;
        }
        if(nickName == null){
            nickName = "";
        }
        return weiboMapper.getTopArticleListByNickName(bootId, nickName);
    }

    public List<WeiboTopArticle> getTopWeiboArticleListByContent(int bootId, String content){
        if(bootId <= 0){
            return null;
        }
        if(content == null){
            content = "";
        }
        return weiboMapper.getTopArticleListByContent(bootId, content);
    }

    public void saveArticle(List<WeiboArticle> weiboArticles){
        weiboMapper.insertArticleList(weiboArticles);
        if(weiboArticles.size() > 0){
            recordMapper.addArticleCount(weiboArticles.get(0).getBootId(), weiboArticles.size());
        }
    }

    public List<WeiboArticle> getWeiboArticleList(int bootId){
        if(bootId <= 0){
            return null;
        }
        return weiboMapper.getArticleList(bootId);
    }

    public List<WeiboArticle> getWeiboArticleListByNickName(int bootId, String nickName){
        if(bootId <= 0){
            return null;
        }
        return weiboMapper.getArticleListByNickName(bootId, nickName);
    }

    public List<WeiboArticle> getWeiboArticleListByContent(int bootId, String content){
        if(bootId <= 0){
            return null;
        }
        return weiboMapper.getArticleListByContent(bootId, content);
    }




    public boolean checkWeiboArticle(WeiboArticle weiboArticle){
        if(weiboArticle == null){
            return false;
        }else if(!StringUtils.isNotBlank(weiboArticle.getContent())){
            return false;
        }
        return true;
    }
}

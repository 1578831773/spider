package com.example.demo.service;

import com.example.demo.bean.CommArticle;
import com.example.demo.mapper.CommMapper;
import com.example.demo.mapper.RecordMapper;
import com.example.demo.util.CommonUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommService {
    @Autowired
    private CommMapper commMapper;
    @Autowired
    private RecordMapper recordMapper;

    public void saveArticle(CommArticle commArticle){
        if(commArticle == null) {
            return;
        }else if(commArticle.getBootId() == 0){
            return;
        }else if(!StringUtils.isNotBlank(commArticle.getUrl())) {
            return;
        }
        if(commArticle.getContent() == null){
            commArticle.setContent("");
        }
        commArticle.setSpiderTime(CommonUtil.getNowTime());
        if(commArticle.getContent().length() >= 500){
            commArticle.setContent(commArticle.getContent().substring(0, 450));
        }
        commMapper.addArticle(commArticle);
        recordMapper.addArticleCount(commArticle.getBootId(), 1);
    }

    public List<CommArticle> getArticleList(int bootId){
        if(bootId <= 0){
            return null;
        }
        return commMapper.articleList(bootId);
    }

    public List<CommArticle> getArticlesByUrl(int bootId, String url){
        if(bootId <= 0){
            return null;
        }
        return commMapper.getArticlesByUrl(bootId, url);
    }

    public List<CommArticle> getArticlesByContent(int bootId, String content){
        if(bootId <= 0){
            return null;
        }
        return commMapper.getArticlesByContent(bootId, content);
    }

}

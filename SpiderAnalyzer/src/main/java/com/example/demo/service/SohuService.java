package com.example.demo.service;

import com.example.demo.bean.SohuArticle;
import com.example.demo.mapper.RecordMapper;
import com.example.demo.mapper.SohuMapper;
import com.example.demo.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SohuService {

    @Autowired
    private SohuMapper sohuMapper;
    @Autowired
    private RecordMapper recordMapper;



    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    public void saveArticle(SohuArticle sohuArticle, int articleId){
        if(sohuArticle == null || sohuArticle.getTitle() == null || sohuArticle.getTitle().equals("")){
            return;
        }
        sohuArticle.setArticleId(articleId);
        sohuArticle.setSpiderTime(CommonUtil.getNowTime());
        if(sohuArticle.getContent().length() > 500) sohuArticle.setContent(sohuArticle.getContent().substring(0,450));
        sohuMapper.updateArtcleInfo(sohuArticle);
        recordMapper.addArticleCountBySohuArticleId(articleId, 1);
    }

    public List<SohuArticle> getSohuArticle(int bootId){
        return sohuMapper.sohuArticleList(bootId);
    }

    public List<SohuArticle> getSohuArticleByUrl(String url){
        if(url == null){
            url = "";
        }
        return sohuMapper.getArticleListByUrl(url);
    }

    public List<SohuArticle> getSohuArticleByContent(String content){
        if(content == null){
            content = "";
        }
        return sohuMapper.getArticleByContent(content);
    }

    public int getRecordIdByArticleId(int articleId){
        return sohuMapper.getRecordIdByArticleId(articleId);
    }
}

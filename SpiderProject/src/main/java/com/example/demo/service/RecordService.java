package com.example.demo.service;

import com.example.demo.bean.Record;
import com.example.demo.mapper.RecordMapper;
import com.example.demo.util.CommonUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecordService {
    @Autowired
    RecordMapper recordMapper;
    @Autowired
    SohuService sohuService;
    @Autowired
    CommService commService;
    @Autowired
    WeiboService weiboService;

    @Autowired
    TemplateService templateService;

    public boolean saveRecords(Record record){
        if(record == null || record.getBootUrl() == null) {
            return false;
        }
        record.setCreateTime(CommonUtil.getNowTime());
        record.setUpdateTime(CommonUtil.getNowTime());
        recordMapper.saveRecord(record);
        templateService.addUseCount(record.getTemplateId());
        System.out.println(record.getBootId());
        if(record.getBootId() != 0) return true;
        return false;
    }

    public List<Record> getRecordList(int userId, int authority){
        if(authority >= 2){
            return recordMapper.recordListByUserId(userId);
        }else{
            return recordMapper.recordList();
        }
    }

    public String deleteRecord(int bootId, int userId){
        String url = recordMapper.getRecordByBootId(bootId, userId);
        recordMapper.deleteRecord(bootId, userId);
        return url;
    }

    public boolean ifBootUrlExists(String bootUrl){
        if(recordMapper.ifBootUrlExists(bootUrl) != 0){
            return true;
        }
        return false;
    }

    public List detailList(int bootId, int templateId){
        List rel = null;
        if(templateId == 1){
            rel = sohuService.getSohuArticle(bootId);
        }else if(templateId == 3){
            rel = weiboService.getTopWeiboArticleList(bootId);
        }else if(templateId == 4){
            rel = weiboService.getWeiboArticleList(bootId);
        }else{
            rel = commService.getArticleList(bootId);
        }
        return rel;
    }

    public int getTemplateId(int bootId){
        return recordMapper.getTemplate(bootId);
    }

    public List<Record> getRecordListByRootUrl(String bootUrl){
        if(!StringUtils.isNotBlank(bootUrl)){
            return null;
        }
        List<Record> recordList = recordMapper.getRecordByBootUrl(bootUrl);
        return recordList;
    }

    public List searchDetails(String bootId, String templateId, String content, String searchInd){
        if(!StringUtils.isNotBlank(templateId)){
            return null;
        }
        if(content == null){
            content = "";
        }
        if(templateId.equals("1")){
            if(searchInd.equals("1")){
                return sohuService.getSohuArticleByUrl(content);
            }else{
                return sohuService.getSohuArticleByContent(content);
            }
        }else if(templateId.equals("2")){
            if(searchInd.equals("1")){
                return commService.getArticlesByUrl(Integer.parseInt(bootId), content);
            }else{
                return commService.getArticlesByContent(Integer.parseInt(bootId), content);
            }
        }else if(templateId.equals("3")){
            if(searchInd.equals("1")){
                return weiboService.getTopWeiboArticleListByNickName(Integer.parseInt(bootId), content);
            }else{
                return weiboService.getTopWeiboArticleListByContent(Integer.parseInt(bootId), content);
            }
        }else if(templateId.equals("4")){
            if(searchInd.equals("1")){
                return weiboService.getWeiboArticleListByNickName(Integer.parseInt(bootId), content);
            }else{
                return weiboService.getWeiboArticleListByContent(Integer.parseInt(bootId), content);
            }
        }
        return null;
    }
}

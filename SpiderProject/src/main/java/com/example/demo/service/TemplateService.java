package com.example.demo.service;

import com.example.demo.bean.Template;
import com.example.demo.mapper.TemplateMapper;
import com.example.demo.util.CommonUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TemplateService {
    @Autowired
    private TemplateMapper templateMapper;

    public List<Template> getCanUseTemplateList(){
        return templateMapper.getCanUseTemplates();
    }

    public List<Template> getTemplateList(){
        return templateMapper.getTemplateList();
    }

    public int updateTemplateStatus(int templateId, int status){
        if(templateId <= 0){
            return -1;
        }
        if(status == 0){
            status = 1;
        }else if(status == 1){
            status = 0;
        }else{
            return -2;
        }
        templateMapper.updateTemplate(templateId, status);
        return 1;
    }

    public int insertTemplate(Template template){
        if(!checkTemplate(template)){
            return -1;
        }
        template.setAddTime(CommonUtil.getNowTime());
        templateMapper.insertTemplate(template);
        return template.getTemplateId();
    }

    private boolean checkTemplate(Template template){
        if(template == null){
            return false;
        }
        if(!StringUtils.isNotBlank(template.getTemplateName())){
            return false;
        }
        return true;
    }

    public void addUseCount(int templateId){
        if(templateId <= 0){
            return;
        }
        templateMapper.addUseTime(templateId);
    }

    public List<Double> templateUse(){
        List<Integer> useCount = templateMapper.getTemplateUsedCount();
        int sum = 0;
        for (Integer count : useCount) {
            sum += count;
        }
        List<Double> rel = new ArrayList<>();
        for(Integer count : useCount){
            rel.add(Double.valueOf(String.format("%.2f", count*1.0/sum*1.0)));
        }
        return rel;
    }
}

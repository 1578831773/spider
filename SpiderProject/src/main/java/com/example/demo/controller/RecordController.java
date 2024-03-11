package com.example.demo.controller;

import com.example.demo.bean.CommArticle;
import com.example.demo.bean.LogInfo;
import com.example.demo.bean.SohuArticle;
import com.example.demo.enums.StatusCode;
import com.example.demo.service.LogService;
import com.example.demo.service.RecordService;
import com.example.demo.service.RedisService;
import com.example.demo.util.CommonUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/record")
public class RecordController {
    @Autowired
    private RecordService recordService;
    @Autowired
    private LogService logService;


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView recordList(HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        Object authorityId = session.getAttribute("authority");
        Object userId = session.getAttribute("userId");
        if(authorityId == null){
            modelAndView.addObject("message", StatusCode.NotLogin.getMsg());
        }else{
            modelAndView.addObject("recordList",recordService.getRecordList((int)userId, (int)authorityId));
        }
        modelAndView.setViewName("forward:/jsp/recordList.jsp");
        return modelAndView;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ModelAndView deleteRecord(HttpSession session, String bootId){
        ModelAndView modelAndView = new ModelAndView();
        Object userId = session.getAttribute("userId");
        if(userId == null){
            modelAndView.addObject("message", StatusCode.NotLogin.getMsg());
        }else{
            String content = recordService.deleteRecord(Integer.parseInt(bootId), (int)userId);
            if(content != null){
                logService.insertLog(new LogInfo((int)userId, 4, "删除了源地址为"+content+"的资源", CommonUtil.getNowTime()));
            }
        }
        modelAndView.setViewName("redirect:/record/list");
        return modelAndView;
    }

    @GetMapping("/details")
    public ModelAndView retailListByBootId(String bootId, String templateId){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("details",recordService.detailList(Integer.parseInt(bootId), Integer.parseInt(templateId)));
        modelAndView.addObject("templateId",templateId);
        modelAndView.addObject("bootId", bootId);
        List<Integer> nums = RedisService.getNums(Integer.parseInt(bootId));
        modelAndView.addObject("downloadNum", nums.get(0));
        modelAndView.addObject("analyzeNum", nums.get(0));
        if(templateId.equals("1")){
            modelAndView.addObject("nameList", CommonUtil.getFieldName(new SohuArticle()));
        }else if(templateId.equals("2")){
            modelAndView.addObject("nameList", CommonUtil.getFieldName(new CommArticle()));
        }
        modelAndView.setViewName("forward:/jsp/retailList.jsp");
        return modelAndView;
    }

    @GetMapping("/search")
    public ModelAndView searchByBootUrl(String bootUrl){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("recordList",recordService.getRecordListByRootUrl(bootUrl));
        modelAndView.setViewName("forward:/jsp/recordList.jsp");
        return modelAndView;
    }

    @GetMapping("/retailSearch")
    public ModelAndView searchRetailList(String bootId, String templateId, String content, String searchInd){
        System.out.println(templateId+","+content+","+searchInd);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("details", recordService.searchDetails(bootId, templateId, content, searchInd));
        modelAndView.addObject("templateId",templateId);
        modelAndView.addObject("bootId", bootId);
        if(StringUtils.isNotBlank(content)){
            modelAndView.addObject("redContent",content);
        }
        if(templateId.equals("1")){
            modelAndView.addObject("nameList", CommonUtil.getFieldName(new SohuArticle()));
        }else if(templateId.equals("2")){
            modelAndView.addObject("nameList", CommonUtil.getFieldName(new CommArticle()));
        }
        modelAndView.setViewName("forward:/jsp/retailList.jsp");
        return modelAndView;
    }
}

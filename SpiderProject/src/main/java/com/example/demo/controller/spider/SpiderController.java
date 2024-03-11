package com.example.demo.controller.spider;

import com.example.demo.bean.CommArticle;
import com.example.demo.bean.LogInfo;
import com.example.demo.bean.Record;
import com.example.demo.bean.SohuArticle;
import com.example.demo.enums.SpiderConstants;
import com.example.demo.enums.StatusCode;
import com.example.demo.module.SpiderClient;
import com.example.demo.service.*;
import com.example.demo.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;


@RestController
@RequestMapping("/spider")
public class SpiderController {

    @Autowired
    private UserService userService;

    @Autowired
    private RecordService recordService;
    @Autowired
    private TemplateService templateService;
    @Autowired
    private LogService logService;


//    @RequestMapping(value = "/start" ,method = RequestMethod.GET)
//    public BaseResponse start(String bootUrl){
//        BaseResponse response;
//        if(bootUrl == null || bootUrl.equals("")){
//            response = new BaseResponse(StatusCode.ParamsError);
//        }else{
//            if(sohuService.ifBootUrlExists(bootUrl)){
//                response = new BaseResponse(StatusCode.BootUrlExists);
//            }else{
//                SohuRecord sohuRecord = new SohuRecord(bootUrl);
//                if(sohuService.saveRecords(sohuRecord)){
//                    RedisService.setUrlToBootId(bootUrl, sohuRecord.getBootId());
//                    response = new BaseResponse(StatusCode.Success);
//                    SpiderClient sc = new SpiderClient(bootUrl,1, sohuRecord.getBootId());
//                    sc.start();
//                }else{
//                    response = new BaseResponse(StatusCode.ParamsError);
//                }
//
//            }
//        }
//        return response;
//    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView index(HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        Object userId = session.getAttribute("userId");
        if(userId == null){
            modelAndView.addObject("message", StatusCode.NotLogin);
        }else{
            List<Integer> rel = userService.spiderIndex((int)userId);
            modelAndView.addObject("loginCount", rel.get(0));
            modelAndView.addObject("recordCount", rel.get(1));
            modelAndView.addObject("retailCount", rel.get(2));
            modelAndView.addObject("templateUse", templateService.templateUse());
        }
        modelAndView.setViewName("forward:/jsp/index.jsp");
        return modelAndView;
    }

    @RequestMapping(value = "/task" ,method = RequestMethod.GET)
    public ModelAndView spiderIndex(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("templateList", templateService.getCanUseTemplateList());
        modelAndView.setViewName("forward:/jsp/spider.jsp");
        return modelAndView;
    }

    @RequestMapping(value = "/start" ,method = RequestMethod.GET)
    public ModelAndView start(String bootUrl, String templateId, String spiderNum, HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        Object userId = session.getAttribute("userId");
        if(userId == null){
            modelAndView.addObject("message", StatusCode.NotLogin.getMsg());
        }else if(bootUrl == null || bootUrl.equals("")){
            modelAndView.addObject("message",StatusCode.ParamsError.getMsg());
        }else{
//            if(recordService.ifBootUrlExists(bootUrl)){
//                modelAndView.addObject("message",StatusCode.BootUrlExists);
//            }else{
                Record record = new Record(bootUrl, templateId);
                record.setUserId((int)userId);
                if(recordService.saveRecords(record)){
                    logService.insertLog(new LogInfo((int)userId, 4, "创建了爬虫任务编号为"+record.getBootId(), CommonUtil.getNowTime()));
                    RedisService.addFutureRecord(String.valueOf(record.getBootId()) + "_" + templateId);
                    preprocess(record.getBootId(), bootUrl + "#1");
                    RedisService.setSpiderNum(record.getBootId(), Integer.parseInt(spiderNum));
                    modelAndView.addObject("recordId", record.getBootId());
                    modelAndView.addObject("message", StatusCode.Success.getMsg());
                    modelAndView.setViewName("forward:/spider/task");
                    return modelAndView;
                }else{
                    modelAndView.addObject("message",StatusCode.ParamsError.getMsg());
                }

//            }
        }
        modelAndView.setViewName("redirect:/spider/task");
        return modelAndView;
    }

    /**
     * 预处理地址
     */
    private void preprocess(int bootId, String combUrl) {
        if(RedisService.existsInFutures(bootId, combUrl)
                || RedisService.existsInOks(bootId, combUrl)
                || RedisService.existsDownloadings(bootId, combUrl)){
            return;
        }
        if(RedisService.existsInFails(bootId, combUrl)){
            int count = RedisService.getFailCounts(bootId, combUrl);
            if(count < SpiderConstants.SPIDER_FAILURE_RETRIES){
                RedisService.addToFutures(bootId, combUrl);
                return;
            }
        }
        RedisService.addToFutures(bootId, combUrl);
    }
}

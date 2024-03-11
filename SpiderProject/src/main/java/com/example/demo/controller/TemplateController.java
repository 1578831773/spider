package com.example.demo.controller;

import com.example.demo.bean.LogInfo;
import com.example.demo.bean.Template;
import com.example.demo.enums.StatusCode;
import com.example.demo.service.LogService;
import com.example.demo.service.TemplateService;
import com.example.demo.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/template")
public class TemplateController {
    @Autowired
    private TemplateService templateService;
    @Autowired
    private LogService logService;

    @GetMapping("/list")
    public ModelAndView getTemplateList(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("templateList", templateService.getTemplateList());
        modelAndView.setViewName("forward:/jsp/templateList.jsp");
        return modelAndView;
    }

    @PostMapping("/update")
    public ModelAndView updateTemplateStatus(int templateId, int status, HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        Object authorityId = session.getAttribute("authority");
        Object userId = session.getAttribute("userId");
        if(authorityId == null){
            modelAndView.addObject("message", StatusCode.NotLogin.getMsg());
        }else{
            if((int)authorityId < 2){
                modelAndView.addObject("message", StatusCode.NoAuthority);
            }else{
                int code = templateService.updateTemplateStatus(templateId, status);
                if(code != 1){
                    modelAndView.addObject("message", StatusCode.ParamsError);
                }else{
                    String content = status == 0?"启用":"禁用";
                    logService.insertLog(new LogInfo((int)userId, 3, content+"了模板"+templateId, CommonUtil.getNowTime()));
                }
            }
        }
        modelAndView.setViewName("redirect:/template/list");
        return modelAndView;
    }

    @PostMapping("/insert")
    public ModelAndView insertTemplate(Template template, HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        Object authorityId = session.getAttribute("authority");
        Object userId = session.getAttribute("userId");
        if(authorityId == null){
            modelAndView.addObject("message", StatusCode.NotLogin.getMsg());
        }else {
            if ((int) authorityId < 2) {
                modelAndView.addObject("message", StatusCode.NoAuthority);
            } else {
                int code = templateService.insertTemplate(template);
                if (code < 0) {
                    modelAndView.addObject("message", StatusCode.ParamsError);
                }else{
                    logService.insertLog(new LogInfo((int)userId, 3, "添加了模板"+code, CommonUtil.getNowTime()));
                }
            }
        }
        modelAndView.setViewName("redirect:/template/list");
        return modelAndView;
    }

}

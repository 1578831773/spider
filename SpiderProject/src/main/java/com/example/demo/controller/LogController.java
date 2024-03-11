package com.example.demo.controller;

import com.example.demo.enums.StatusCode;
import com.example.demo.service.LogService;
import org.apache.xpath.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/log")
public class LogController {
    @Autowired
    private LogService logService;

    @GetMapping("/logList")
    public ModelAndView getLogList(HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        Object authority = session.getAttribute("authority");
        if(authority == null){
            modelAndView.addObject("message", StatusCode.NotLogin.getMsg());
        }else{
            if((int)authority != 3){
                modelAndView.addObject("message", StatusCode.NoAuthority.getMsg());
            }else{
                modelAndView.addObject("logList", logService.getLogList(0));

            }
        }
        modelAndView.setViewName("forward:/jsp/logList.jsp");
        return modelAndView;
    }

    @GetMapping("/personalLogList")
    public ModelAndView getPersonalLogList(HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        Object userId = session.getAttribute("userId");
        if(userId == null){
            modelAndView.addObject("message", StatusCode.NotLogin.getMsg());
        }else{
            modelAndView.addObject("logList", logService.getPersonalLogList((int)userId));
        }
        modelAndView.setViewName("forward:/jsp/logList.jsp");
        return modelAndView;
    }

}

package com.example.demo.controller;

import com.example.demo.bean.LogInfo;
import com.example.demo.bean.UserInfo;
import com.example.demo.enums.StatusCode;
import com.example.demo.service.LogService;
import com.example.demo.service.UserService;
import com.example.demo.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private LogService logService;


    @PostMapping("/login")
    public ModelAndView login(String account, String password, HttpSession session){
        int code = userService.login(account, password);
        ModelAndView modelAndView = new ModelAndView();
        if(code < 0){
            if(code == -1){
                modelAndView.addObject("message", StatusCode.UserAccountCannotBeNull.getMsg());
            }else if(code == -2){
                modelAndView.addObject("message", StatusCode.PasswordCannotBeNull.getMsg());
            }else if(code == -3){
                modelAndView.addObject("message", StatusCode.AccountNotExists.getMsg());
            }else if(code == -4){
                modelAndView.addObject("message", StatusCode.PasswordError.getMsg());
            }else{
                modelAndView.addObject("message", StatusCode.NoAuthority.getMsg());
            }
            modelAndView.setViewName("redirect:/jsp/login.jsp");
        }else{
            session.setAttribute("userId", code);
            logService.insertLog(new LogInfo(code, 1, "用户登录", CommonUtil.getNowTime()));
            UserInfo userInfo = userService.getUserSmallInfo(code);
            session.setAttribute("authority",userInfo.getAuthority());
            session.setAttribute("userName",userInfo.getUserName());
            session.setAttribute("headPic",userInfo.getHeadPic());
            modelAndView.setViewName("redirect:/spider/index");
        }
        return modelAndView;
    }

    @PostMapping("/register")
    public ModelAndView register(UserInfo userInfo, MultipartFile headPicFile, String rePassword, HttpServletRequest httpServletRequest) throws IOException {
        ModelAndView modelAndView = new ModelAndView();
        int code = userService.insertUserInfo(userInfo, headPicFile, rePassword, httpServletRequest);
        if(code < 0){
            if(code == -1){
                modelAndView.addObject("message", StatusCode.ParamsError.getMsg());
            }else if(code == -2){
                modelAndView.addObject("message", StatusCode.AccountAlreadyExists.getMsg());
            }else{
                modelAndView.addObject("message", StatusCode.AccountAlreadyExists.getMsg());
            }
            modelAndView.setViewName("redirect:/jsp/register.jsp");
        }else{
            modelAndView.setViewName("redirect:/jsp/login.jsp");
        }
        return modelAndView;
    }

    @PostMapping("/logout")
    public ModelAndView logout(HttpSession session){
        Object code = session.getAttribute("userId");
        session.removeAttribute("userId");
        session.removeAttribute("authority");
        if(code != null)
        logService.insertLog(new LogInfo((int)code, 1, "用户退出登陆", CommonUtil.getNowTime()));
        ModelAndView mv = new ModelAndView();
        mv.setViewName("redirect:/jsp/login.jsp");
        return mv;
    }

    @GetMapping("/list")
    public ModelAndView userAccountList(HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        Object authorityId = session.getAttribute("authority");
        Object userId = session.getAttribute("userId");
        if(authorityId == null){
            modelAndView.addObject("message",StatusCode.NotLogin.getMsg());
        }else{
            if((int)authorityId < 2){
                modelAndView.addObject("message",StatusCode.NoAuthority.getMsg());
            }else{
                modelAndView.addObject("userList",userService.getUserAccountList((int)userId));
            }
        }
        modelAndView.setViewName("forward:/jsp/userList.jsp");
        return modelAndView;
    }

    @GetMapping("/search")
    public ModelAndView searchUserAccountByUserName(String userName, HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        Object authorityId = session.getAttribute("authority");
        Object userId = session.getAttribute("userId");
        if(authorityId == null){
            modelAndView.addObject("message",StatusCode.NotLogin.getMsg());
        }else{
            if((int)authorityId < 2){
                modelAndView.addObject("message",StatusCode.NoAuthority.getMsg());
            }else{
                modelAndView.addObject("userList",userService.getUserAccountListByName(userName));
            }
        }
        modelAndView.setViewName("forward:/jsp/userList.jsp");
        return modelAndView;
    }

    @PostMapping("/authority")
    public ModelAndView updateAuthority(int userId, int authority, HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        Object authorityId = session.getAttribute("authority");
        Object uid = session.getAttribute("userId");
        if(authorityId == null){
           modelAndView.addObject("message",StatusCode.NotLogin.getMsg());
        }else{
            if((int)authorityId == 2){
                if(authority == 2){
                    modelAndView.addObject("message",StatusCode.NoAuthority.getMsg());
                }else{
                    userService.updateUserAuthority(userId, authority);
                }
            }else if((int)authorityId == 3){
                String content = authority > 0?"禁用":"启用";
                logService.insertLog(new LogInfo((int)uid, 2, content+"了编号用户"+userId+"的账号", CommonUtil.getNowTime()));
                userService.updateUserAuthority(userId, authority);
            }else{
                modelAndView.addObject("message",StatusCode.NoAuthority.getMsg());
            }
        }
        modelAndView.setViewName("redirect:/user/list");
        return modelAndView;
    }

    @PostMapping("/setManager")
    public ModelAndView setUserManager(int userId, int authority, HttpSession session){
        System.out.println(userId+","+authority);
        ModelAndView modelAndView = new ModelAndView();
        Object authorityId = session.getAttribute("authority");
        Object uid = session.getAttribute("userId");
        if(authorityId == null){
            modelAndView.addObject("message", StatusCode.NotLogin.getMsg());
        }else{
            if((int)authorityId == 3){
                String content = (authority == 1||authority == -1)? "设置":"取消";
                logService.insertLog(new LogInfo((int)uid, 2, content+"用户编号"+userId+"账号的管理员身份", CommonUtil.getNowTime()));
                userService.setUserManager(userId, authority);
            }else{
                modelAndView.addObject("message", StatusCode.NoAuthority.getMsg());
            }
        }
        modelAndView.setViewName("redirect:/user/info?userId="+userId);
        return modelAndView;
    }


    @GetMapping("/info")
    public ModelAndView getUserInfo(int userId, HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        Object loginUid = session.getAttribute("userId");
        if(loginUid == null){
            modelAndView.addObject("message", StatusCode.NotLogin.getMsg());
        }else{
            if((int)loginUid < 2){
                modelAndView.addObject("message",StatusCode.NoAuthority.getMsg());
            }else{
                modelAndView.addObject("userInfo",userService.getUserInfo(userId));
            }
        }
        modelAndView.setViewName("forward:/jsp/userInfo.jsp");
        return modelAndView;
    }

    @GetMapping("/homepage")
    public ModelAndView getUserHomePageInfo(HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        Object userId = session.getAttribute("userId");
        if(userId == null){
            modelAndView.addObject("message", StatusCode.NotLogin);
        }
        UserInfo userInfo = userService.getUserInfo((int)userId);
        modelAndView.addObject("userInfo", userInfo);
        modelAndView.setViewName("forward:/jsp/homepage.jsp");
        return modelAndView;
    }

    @PostMapping("/updateHeadPic")
    public ModelAndView updateUserHeadPic(MultipartFile headPic, HttpSession session, HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        Object userId = session.getAttribute("userId");
        if(userId == null){
            modelAndView.addObject("message", StatusCode.NotLogin);
        }
        String code = userService.updateUserHeadPic((int)userId, headPic, request);
        if(code == null){
            modelAndView.addObject("message",StatusCode.ParamsError.getMsg());
        }
        session.removeAttribute("headPic");
        session.setAttribute("headPic", code);
        modelAndView.setViewName("redirect:/user/homepage");
        return modelAndView;
    }

    @PostMapping("/updatePassword")
    public ModelAndView updateUserPassword(String password, String rePassword, HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        Object userId = session.getAttribute("userId");
        if(userId == null){
            modelAndView.addObject("message", StatusCode.NotLogin);
        }
        int code = userService.updateUserPassword((int)userId, password, rePassword);
        if(code == -1){
            modelAndView.addObject("message",StatusCode.PasswordCannotBeNull.getMsg());
        }else if(code == -2 || code == -3){
            modelAndView.addObject("message",StatusCode.RePasswordError.getMsg());
        }
        modelAndView.setViewName("redirect:/user/homepage");
        return modelAndView;
    }

    @PostMapping("/updateUserInfo")
    public ModelAndView updateUserInfo(UserInfo userInfo, HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        Object userId = session.getAttribute("userId");
        if(userId == null){
            modelAndView.addObject("message", StatusCode.NotLogin);
        }
        else{
            userInfo.setUserId((int)userId);
            if(userService.updateUserInfo(userInfo) == 0){
                modelAndView.addObject("message",StatusCode.ParamsError.getMsg());
            }else{
                session.removeAttribute("userName");
                session.setAttribute("userName", userInfo.getUserName());
            }
        }
        modelAndView.setViewName("redirect:/user/homepage");
        return modelAndView;
    }
}

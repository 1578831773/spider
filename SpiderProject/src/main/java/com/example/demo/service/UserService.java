package com.example.demo.service;

import com.example.demo.bean.UserAccount;
import com.example.demo.bean.UserInfo;
import com.example.demo.mapper.TemplateMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.util.CommonUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private TemplateMapper templateMapper;

    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    public int insertUserInfo(UserInfo userInfo, MultipartFile headPic, String password, HttpServletRequest httpServletRequest) throws IOException {
        if(!judgeUserInfo(userInfo)){
            return -1;
        }
        if(userMapper.isAccountExists(userInfo.getAccount()) != 0){
            return -2;
        }
        if(!StringUtils.isNotBlank(password) || !password.equals(userInfo.getPassword())){
            return -3;
        }
        String url = CommonUtil.uploadFile(headPic, httpServletRequest);
        if(url == null){
            return -4;
        }
        userInfo.setHeadPic(url);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode(userInfo.getPassword());
        userInfo.setPassword(encode);
        userMapper.insertUserAccount(userInfo);
        userMapper.insertUserInfo(userInfo);
        return 1;
    }

    public int login(String account, String password){
        if(!StringUtils.isNotBlank(account)){
            return -1;
        }
        if(!StringUtils.isNotBlank(password)){
            return -2;
        }
        if(userMapper.isAccountExists(account) == 0){
            return -3;
        }
        if(userMapper.getUserAuthority(account) < 0){
            return -5;
        }
        UserAccount userAccount = userMapper.login(account);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if(passwordEncoder.matches(password, userAccount.getPassword())){
            return  userAccount.getUserId();
        }
        return -4;
    }

    public UserInfo getUserSmallInfo(int userId){
        userMapper.addUserLoginCount(userId);
        return userMapper.getUserSmallInfoByUserId(userId);
    }

    public List<UserInfo> getUserAccountList(int userId){
        List<UserInfo>  userInfos = userMapper.getUserAccountList(userId);
        return userInfos;
    }

    public void updateUserAuthority(int userId, int authority){
        if(userId <= 0) return;
        userMapper.setUserAuthority(userId, -authority);
    }

    public void setUserManager(int userId, int authority){
        if(authority == 2){
            userMapper.setUserAuthority(userId, 1);
        }else if(authority == -2){
            userMapper.setUserAuthority(userId, -1);
        }else{
            userMapper.setUserAuthority(userId, 2);
        }
    }

    public List<UserInfo> getUserAccountListByName(String userName){
        if(userName == null){
            userName = "";
        }
        return userMapper.getUserAccountListByName(userName);
    }

    public UserInfo getUserInfo(int userId){
        return userMapper.getUserInfoByUserId(userId);
    }

    public String updateUserHeadPic(int userId, MultipartFile file, HttpServletRequest request){
        String headPic;
        try {
            headPic = CommonUtil.uploadFile(file, request);
        } catch (IOException e) {
            return null;
        }
        userMapper.updateUserHeadPic(userId, headPic);
        return headPic;
    }

    public int updateUserPassword(int userId, String password, String rePassword){
        if(!StringUtils.isNotBlank(password)){
            return -1;
        }else if(!StringUtils.isNotBlank(rePassword)){
            return -2;
        }else if(!password.equals(rePassword)){
            return -3;
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode(password);
        userMapper.updateUserPassword(userId, encode);
        return 1;
    }

    public int updateUserInfo(UserInfo userInfo){
        if(userInfo != null && StringUtils.isNotBlank(userInfo.getUserName())){
            userMapper.updateUserInfo(userInfo);
            return 1;
        }
        return 0;
    }

    public List<Integer> spiderIndex(int userId){
        List<Integer> rel = new ArrayList<>();
        rel.add(userMapper.getUserLoginCount(userId));
        int taskCount = userMapper.userTaskCount(userId);
        rel.add(taskCount);
        if(taskCount == 0){
            rel.add(0);
        }else{
            rel.add(userMapper.retailsCount(userId));
        }
        return rel;
    }

    private boolean judgeUserInfo(UserInfo userInfo){
        if(userInfo == null){
            return false;
        }
        return StringUtils.isNotBlank(userInfo.getAccount()) && StringUtils.isNotBlank(userInfo.getUserName())
                && StringUtils.isNotBlank(userInfo.getPassword());
    }
}

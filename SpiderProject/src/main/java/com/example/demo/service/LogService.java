package com.example.demo.service;

import com.example.demo.bean.LogInfo;
import com.example.demo.mapper.LogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogService {
    @Autowired
    private LogMapper logMapper;
    public void insertLog(LogInfo logInfo){
        logMapper.insertLog(logInfo);
    }

    public List<LogInfo> getPersonalLogList(int userId){
        return logMapper.getPersonalLogList(userId);
    }

    public List<LogInfo> getLogList(int kind){
        return logMapper.getLogList(kind);
    }
}

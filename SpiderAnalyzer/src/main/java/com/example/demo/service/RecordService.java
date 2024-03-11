package com.example.demo.service;

import com.example.demo.mapper.RecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecordService {
    @Autowired
    private RecordMapper recordMapper;
    public void updateRecordStatus(int recordId, int status){
        recordMapper.updateRecordStatus(recordId, status);
    }
}

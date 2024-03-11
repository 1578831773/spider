package com.example.demo.mapper;

import com.example.demo.bean.LogInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface LogMapper {
    @Insert("insert into log(user_id, kind, content, time) values(#{logInfo.userId}, #{logInfo.kind}, #{logInfo.content}, #{logInfo.time})")
    @Options(useGeneratedKeys = true, keyProperty = "logId")
    void insertLog(@Param("logInfo")LogInfo logInfo);

    @Select("select * from log where user_id = #{userId}")
    List<LogInfo> getPersonalLogList(int userId);
    @Select("select * from log where if(#{kind} = 0, 1=1, kind = #{kind})")
    List<LogInfo> getLogList(int kind);

}

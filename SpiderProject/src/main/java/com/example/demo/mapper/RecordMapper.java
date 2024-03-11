package com.example.demo.mapper;

import com.example.demo.bean.Record;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RecordMapper {


    @Select("select count(*) from records where boot_url = #{bootUrl}")
    int ifBootUrlExists(String bootUrl);

    @Insert("insert into records(`boot_url`, `user_id`, `create_time`, `update_time`, `count`, `template_id`) " +
            "values(#{bootUrl}, #{userId}, #{createTime}, #{updateTime}, #{count}, #{templateId})")
    @Options(useGeneratedKeys = true, keyProperty = "bootId")
    void saveRecord(Record record);

    @Update("update records set count = count + #{count} where boot_id = #{bootId}")
    void addArticleCount(int bootId, int count);

    @Select("select boot_id, user_id, boot_url, create_time, update_time, count, template_id, `status` from records where user_id = #{userId}")
    List<Record> recordListByUserId(int userId);
    @Select("select boot_id, user_id, boot_url, create_time, update_time, count, template_id , `status` from records")
    List<Record> recordList();

    @Delete("delete from records where boot_id = #{bootId} and user_id = #{userId}")
    void deleteRecord(int bootId, int userId);

    @Select("select template_id from records where boot_id = #{bootId}")
    int getTemplate(int bootId);

    @Select("select boot_id, boot_url, user_id, create_time, update_time, count, template_id, `status` from records where boot_url = #{url}")
    List<Record> getRecordByBootUrl(String url);

    @Select("select boot_url from records where boot_id = #{bootId} and user_id = #{userId}")
    String getRecordByBootId(int bootId, int userId);

    @Select("select boot_id, boot_url, user_id, create_time, update_time, count, template_id, `status` from records where user_id = #{userId}")
    List<Record> getRecordListByUserId(int userId);
}

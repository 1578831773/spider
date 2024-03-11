package com.example.demo.mapper;

import com.example.demo.bean.Template;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TemplateMapper {

    @Select("select template_id, template_name from template where status = 1")
    List<Template> getCanUseTemplates();

    @Select("select template_id, template_name, `desc`, add_time, use_count, `status` from template")
    List<Template> getTemplateList();

    @Update("update template set status = #{status} where template_id = #{templateId}")
    void updateTemplate(int templateId, int status);

    @Insert("insert into template(template_id, template_name, `desc`, add_time) " +
            "values(#{template.templateId}, #{template.templateName}, #{template.desc}, #{template.addTime})")
    @Options(useGeneratedKeys = true, keyProperty = "templateId")
    void insertTemplate(@Param("template") Template template);

    @Update("update template set use_count = use_count + 1 where template_id = #{templateId}")
    void addUseTime(int templateId);

    @Select("select use_count from template where status = 1")
    List<Integer> getTemplateUsedCount();
}

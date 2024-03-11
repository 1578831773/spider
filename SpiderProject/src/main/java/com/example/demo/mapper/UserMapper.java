package com.example.demo.mapper;

import com.example.demo.bean.UserAccount;
import com.example.demo.bean.UserInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("select count(*) from user_account where account = #{account}")
    int isAccountExists(String account);

    @Insert("insert into user_account(account, password) values(#{userInfo.account}, #{userInfo.password}) ")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    void insertUserAccount(@Param("userInfo") UserInfo userInfo);

    @Insert("insert into user_info(user_id, sex, user_name, head_pic) values(#{userInfo.userId}, #{userInfo.sex}, #{userInfo.userName}, #{userInfo.headPic})")
    void insertUserInfo(@Param("userInfo") UserInfo userInfo);

    @Select("select authority from user_account where account = #{account}")
    int getUserAuthority(String account);

    @Select("select user_id, account, password from user_account where account = #{account}")
    UserAccount login(String account);

    @Select("select a.user_name, a.head_pic, b.authority from user_info a left join user_account b " +
            "on a.user_id = b.user_id " +
            "where a.user_id = #{userId}")
    UserInfo getUserSmallInfoByUserId(int userId);

    @Select("select a.user_id, a.account, b.user_name, a.authority from user_account a left join user_info b on a.user_id = b.user_id where a.authority != 3 and a.user_id != #{userId}")
    List<UserInfo> getUserAccountList(int userId);

    @Select("select a.user_id, a.account, b.user_name, a.authority from user_account a left join user_info b on a.user_id = b.user_id " +
            "where a.authority != 3 and b.user_name like concat('%',concat(#{userName},'%'))")
    List<UserInfo> getUserAccountListByName(String userName);

    @Update("update user_account set authority = #{authority} where user_id = #{userId}")
    void setUserAuthority(int userId, int authority);

    @Select("select a.user_id, a.account, a.authority, b.user_name, b.sex, b.head_pic from user_account a left join user_info b on " +
            "a.user_id = b.user_id " +
            "where a.user_id = #{userId}")
    UserInfo getUserInfoByUserId(int userId);

    @Update("update user_account set password = #{password} where user_id = #{userId}")
    void updateUserPassword(int userId, String password);

    @Update("update user_info set head_pic = #{headPic} where user_id = #{userId}")
    void updateUserHeadPic(int userId, String headPic);

    @Update("update user_info set user_name = #{userInfo.userName}, sex = #{userInfo.sex} where user_id = #{userInfo.userId}")
    void updateUserInfo(@Param("userInfo") UserInfo userInfo);

    @Update("update user_info set login_count = login_count + 1 where user_id = #{userId}")
    void addUserLoginCount(int userId);

    @Select("select login_count from user_info where user_id = #{userId}")
    int getUserLoginCount(int userId);

    @Select("select count(*) from records where user_id = #{userId}")
    int userTaskCount(int userId);

    @Select("select sum(count) from records where user_id = #{userId} ")
    int retailsCount(int userId);
}

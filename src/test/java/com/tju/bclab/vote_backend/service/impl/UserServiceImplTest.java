package com.tju.bclab.vote_backend.service.impl; 

import com.tju.bclab.vote_backend.entity.User;
import com.tju.bclab.vote_backend.mapper.UserMapper;
import org.junit.runner.RunWith;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/** 
* UserServiceImpl Tester. 
* 
* @author 
* @since 11/30/2021
*/ 
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplTest {

    @Autowired
    private UserMapper userMapper;
    /** 
    * Method: Login(UserLoginReq userLoginReq) 
    */ 
    @Test
    public void testInsertLogin() throws ParseException {
        User user=new User();
        user.setUserId("14528326770034690");
        user.setSkey("abc");
        user.setSessionKey("abcde");
        user.setAvatarUrl("null");
        user.setCity("HeBei");
        user.setCountry("China");
        user.setGender(1);
        user.setLanguage("Chinese");
        user.setNickName("sgw");
        user.setProvince("HeBei");
        DateFormat format1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1=format1.parse("2021-11-21 21:08:09");
        Date date2=format1.parse("2022-11-30 22:09:08");
        user.setGmtCreate(date1);
        user.setGmtModified(date2);
        user.setLastVisitTime(date2);
        user.setType(0);
        user.setUserAddress("abcde");
        user.setRealName("ShiGaoWei");
        userMapper.insert(user);
    }



}

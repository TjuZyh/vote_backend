package com.tju.bclab.vote_backend.service.impl; 

import com.tju.bclab.vote_backend.entity.User;
import com.tju.bclab.vote_backend.entity.Voteboxuser;
import com.tju.bclab.vote_backend.io.aelf.schemas.KeyPairInfo;
import com.tju.bclab.vote_backend.mapper.*;
import com.tju.bclab.vote_backend.service.*;
import com.tju.bclab.vote_backend.vo.resp.VerifyHaveModified;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** 
* PdfServiceImpl Tester. 
* 
* @author 史高伟
* @since 11/30/2021
*/ 
@RunWith(SpringRunner.class)
@SpringBootTest
public class  PdfServiceImplTest {

    @Autowired
    private PdfService PdfService;

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testCreatePdf() throws Exception {
        String voteId = "1466382578049372162";
        User user=new User();
        user.setUserId("14638326770034689");
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
        String Pdf = PdfService.createPdf(voteId);
        System.out.println(Pdf);
    }

}

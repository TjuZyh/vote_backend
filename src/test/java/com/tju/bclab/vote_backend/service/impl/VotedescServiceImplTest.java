package com.tju.bclab.vote_backend.service.impl; 

import com.tju.bclab.vote_backend.entity.Votedesc;
import com.tju.bclab.vote_backend.mapper.VotedescMapper;
import com.tju.bclab.vote_backend.service.VotedescService;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.jupiter.api.Assertions.*;

/** 
* VotedescServiceImpl Tester. 
* 
* @author 张辰宇
* @since 11/30/2021
*/ 
@RunWith(SpringRunner.class)
@SpringBootTest
public class VotedescServiceImplTest {

    @Autowired
    VotedescMapper votedescMapper;

    @Autowired
    VotedescService votedescService;

    /**
    * Method: addVotedesc(Votedesc votedesc) 
    */ 
    @Test
    public void testAddVotedesc() {
        // 构建测试用例
        String voteId = "2453675705976979458";
        String imgUrl = "https://test.com/testurl.jpg";
        Votedesc expected = new Votedesc();
        expected.setVoteId(voteId);
        expected.setImgUrl(imgUrl);
        // 测试方法
        votedescService.addVotedesc(expected);

        // 查询数据库
        Votedesc actual = votedescMapper.selectById(voteId);
        assertEquals(expected.getImgUrl(), actual.getImgUrl());
        votedescMapper.deleteById(voteId);
    }


}

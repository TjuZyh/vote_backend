package com.tju.bclab.vote_backend.service.impl; 

import com.baomidou.mybatisplus.extension.service.IService;
import com.tju.bclab.vote_backend.entity.Vote;
import com.tju.bclab.vote_backend.entity.Voteboxuser;
import com.tju.bclab.vote_backend.entity.Votedesc;
import com.tju.bclab.vote_backend.mapper.*;
import com.tju.bclab.vote_backend.service.VoteboxuserService;
import com.tju.bclab.vote_backend.vo.common.UserQrcodeInfo;
import com.tju.bclab.vote_backend.vo.req.AddVoteBoxReq;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/** 
* VoteboxuserServiceImpl Tester. 
* 
* @author 史高伟
* @since 11/30/2021
*/ 
@RunWith(SpringRunner.class)
@SpringBootTest
public class VoteboxuserServiceImplTest {

    @Autowired
    private VoteboxuserService voteboxuserService;

    @Autowired
    private VoteboxuserMapper voteboxuserMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private VoteMapper voteMapper;

    @Autowired
    private VotedescMapper votedescMapper;

    @Autowired
    private VoteOptionMapper voteOptionMapper;

    @Autowired
    private BlockChainServiceImpl blockChainService;



    @Test
    public void testAddUserToBox() {
        //初始化数据
        UserQrcodeInfo addUserToBoxReq = new UserQrcodeInfo();
        String voteId = "1466387906862284801";
        addUserToBoxReq.setUserId("oloDq5Nb7miuBH-Knys6kxEMxby4");
        addUserToBoxReq.setCreateTime(111L);
        // 测试方法
        Boolean result=voteboxuserService.addUserToBox(voteId,addUserToBoxReq);
        assertTrue(result);
    }

    @Test
    public void testAddBoxVote() {
        //初始化数据
        AddVoteBoxReq addVoteBoxReq = new AddVoteBoxReq();
        addVoteBoxReq.setType(0);
        addVoteBoxReq.setTitle("sgw_Vote");
        addVoteBoxReq.setVoteDesc("A test for method AddBoxVote");
        addVoteBoxReq.setVoteType(2);
        addVoteBoxReq.setUserId("o2yRC5LwKeuvDpjZUbenSE3GoJto");
        ArrayList<String> optionList = new ArrayList<>();
        optionList.add("box");
        addVoteBoxReq.setOptionList(optionList);
        Date dateNow = new Date();
        addVoteBoxReq.setStartDate(dateNow);
        // 测试方法
        String voteId = voteboxuserService.addBoxVote(addVoteBoxReq);
        //与数据库比对
        Vote vote = voteMapper.selectById(voteId);
        String voteId_actual = vote.getVoteId();
        assertArrayEquals(voteId.toCharArray(),voteId_actual.toCharArray());
    }



}

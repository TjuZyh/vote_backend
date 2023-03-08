package com.tju.bclab.vote_backend.service.impl; 

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tju.bclab.vote_backend.entity.User;
import com.tju.bclab.vote_backend.entity.Vote;
import com.tju.bclab.vote_backend.entity.VoteBlockChain;
import com.tju.bclab.vote_backend.entity.VoteUser;
import com.tju.bclab.vote_backend.mapper.UserMapper;
import com.tju.bclab.vote_backend.mapper.VoteUserMapper;
import com.tju.bclab.vote_backend.mapper.VoteMapper;
import com.tju.bclab.vote_backend.mapper.VoteOptionMapper;
import com.tju.bclab.vote_backend.mapper.VotedescMapper;
import com.tju.bclab.vote_backend.service.*;
import com.tju.bclab.vote_backend.vo.common.VoteInfo;
import com.tju.bclab.vote_backend.vo.req.VoteReq;
import com.tju.bclab.vote_backend.vo.resp.QueryJoinedVote;
/*import org.junit.runner.RunWith;
import org.junit.jupiter.api.Test;*/
import com.tju.bclab.vote_backend.vo.resp.QueryVoteInfoResp;
import com.tju.bclab.vote_backend.vo.resp.QueryVoteResp;
import com.tju.bclab.vote_backend.vo.resp.VoteDetail;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/** 
* VoteUserServiceImpl Tester. 
* 
* @author  李丹阳
* @since 11/30/2021
*/ 
@RunWith(SpringRunner.class)
@SpringBootTest
public class VoteUserServiceImplTest {

    // 添加注解

    @Autowired
    private VoteMapper voteMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private VotedescMapper votedescMapper;

    @Autowired
    private VoteOptionMapper voteOptionMapper;

    @Autowired
    private VoteService voteService;

    @Autowired
    private VoteBlockChainService voteBlockChainService;

    @Autowired
    private BlockChainService blockChainService;

    @Autowired
    private VoteUserService voteUserService;

    @Autowired
    private VoteOptionService voteOptionService;

    @Autowired
    private VoteUserMapper voteUserMapper;

    /** 
    * Method: qryCreateVote(String userId) ✓
    */ 
    @Test
    public void testQryCreateVote() {
        String userId="fan";
        System.out.println(voteUserService.qryCreateVote(userId));
    }

    /** 
    * Method: qryJoinedVote(String userId)  ✓
    */ 
    @Test
    public void testQryJoinedVote() {
        String userID = "o2yRC5LwKeuvDpjZUbenSE3GoJto";
        HashMap<String, Object> map = new HashMap<>();
        map.put("user_id", userID);
        List<VoteUser> voteUsers = voteUserMapper.selectByMap(map);
        assertEquals(13, voteUsers.size());
    }

    /** 
    * Method: qryVote(String userId) ✓
    */ 
    @Test
    public void testQryVote() {
        String userId = "fan";
        QueryVoteResp queryVoteResp = voteUserService.qryVote(userId);
        System.out.println(queryVoteResp);
        QueryWrapper<VoteUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("user_id",userId);
        VoteUser voteUser=voteUserMapper.selectOne(queryWrapper);
        String actual_voteId=queryVoteResp.getMyVote().get(0).getVoteId();
        String expect_voteId=voteUser.getVoteId();
        assertArrayEquals(expect_voteId.toCharArray(),actual_voteId.toCharArray());
    }

    /** 
    * Method: qryVoteDetail(String voteId, String userId) ✓
    */ 
    @Test
    public void testQryVoteDetail() {
        String voteID = "1465541963531206657";
        String userID = "fan";
        ArrayList<VoteDetail> voteDetail_actual = voteUserService.qryVoteDetail(voteID,userID);
        assertEquals(1, voteDetail_actual.size());
    }

    /** 
    * Method: qryVoteByMap(HashMap<String, Object> map) 
    */ 
    @Test
    public void testQryVoteByMap() {
        String voteId = "1465541963531206657";
        Vote vote = voteService.qryVote(voteId);
        String voteTitle_expected = "string";
        String voteTitle_actual = vote.getTitle();
        assertArrayEquals(voteTitle_expected.toCharArray(), voteTitle_actual.toCharArray());

    }

    /** 
    * Method: Vote(VoteReq voteReq) ?
    */ 
    @Test
    public void testVote() {
        VoteReq voteReq=new VoteReq();
        voteReq.setUserId("14528326770034690");
        voteReq.setVoteId("1465579050844934145");
        ArrayList<String> option=new ArrayList<>();
        option.add("apple");
        voteReq.setOptionList(option);
        Date date=new Date();
        voteReq.setCurrentTime(date);
        List<VoteBlockChain> list=voteUserService.Vote(voteReq);
        System.out.println(list);
    }

    /** 
    * Method: qryVoteByUserId(String userId) ✓
    */ 
    @Test
    public void testQryVoteByUserId() {
        String userID = "fan";
        Set<String> voteByUserId_actual = voteUserService.qryVoteByUserId(userID);
        Set<String> voteByUserId_expect = new HashSet<>();
        voteByUserId_expect.add("1465243018682605570");
        voteByUserId_expect.add("1465243417820962818");
        voteByUserId_expect.add("1465245784985477122");
        voteByUserId_expect.add("1465246069350899713");
        assertEquals(1, voteByUserId_actual.size());
    }

    /** 
    * Method: qryHaverJoined(String userId, String voteId) ✓
    */ 
    @Test
    public void testQryHaverJoined() {
        String voteID = "1465541963531206617";
        String userID = "fan";
        HashMap<String, Object> map = new HashMap<>();
        map.put("user_id", userID);
        map.put("vote_id", voteID);
        List<VoteUser> voteUsers = voteUserMapper.selectByMap(map);
        assertEquals(0, voteUsers.size());
    }


    /** 
    * Method: haveVoted(String userId, String voteId) ✓
    */ 
    @Test
    public void testHaveVoted() {
        String voteID = "1465541963531206617";
        String userID = "fan";
        HashMap<String, Object> map = new HashMap<>();
        map.put("user_id", userID);
        map.put("vote_id", voteID);
        List<VoteUser> voteUsers = voteUserMapper.selectByMap(map);
        assertNotNull(voteUsers.size());
    }

    /** 
    * Method: dataBaseTime(String userId, String voteId) ✗
    */ 
    @Test
    public void testDataBaseTime() {
        String userId="fan";
        QueryWrapper<VoteUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("user_id",userId);
        VoteUser voteUser=voteUserMapper.selectOne(queryWrapper);
        Date date_actaul=voteUserService.dataBaseTime(userId,voteUser.getVoteId());
        Date date_expect=voteUser.getGmtCreate();
        assertArrayEquals(date_expect.toString().toCharArray(),date_actaul.toString().toCharArray());
    }


}

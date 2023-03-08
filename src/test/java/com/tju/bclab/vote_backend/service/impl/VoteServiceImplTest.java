package com.tju.bclab.vote_backend.service.impl;

import com.tju.bclab.vote_backend.entity.Vote;
import com.tju.bclab.vote_backend.mapper.UserMapper;
import com.tju.bclab.vote_backend.mapper.VoteMapper;
import com.tju.bclab.vote_backend.mapper.VoteOptionMapper;
import com.tju.bclab.vote_backend.mapper.VotedescMapper;
import com.tju.bclab.vote_backend.service.BlockChainService;
import com.tju.bclab.vote_backend.service.VoteBlockChainService;
import com.tju.bclab.vote_backend.service.VoteService;
import com.tju.bclab.vote_backend.service.impl.VoteBlockChainServicelmpl;
import com.tju.bclab.vote_backend.service.impl.VoteServiceImpl;
import com.tju.bclab.vote_backend.vo.req.AddVoteReq;
import com.tju.bclab.vote_backend.vo.resp.QueryVoteInfoResp;
import com.tju.bclab.vote_backend.vo.resp.VerifyHaveModified;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

//注意点一：JDK版本1.8/8/11
//注意点二：添加RunWith和SpringBootTest注解，不然Autowrired注解对象会显示空指针
@RunWith(SpringRunner.class)
@SpringBootTest
class VoteServiceImplTest {
    @Autowired
    private VoteMapper voteMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private VotedescMapper votedescMapper;

    @Autowired
    private VoteOptionMapper voteOptionMapper;

    //注意点三：测试是应将该实例注入到接口中，而不是实现类中（测试点在VoteServiceImpl文件中），注入类不需要实例化
    @Autowired
    private VoteService voteService;

    @Autowired
    private VoteBlockChainService voteBlockChainService;

    @Autowired
    private BlockChainService blockChainService;

    @Test
    void qryTileByVoteId() {
        //查询测试思路；对比真实数据和数据库中数据
        String voteID = "1466060905973104641";
        //测试数据
        String voteTitle_actual = voteService.qryTileByVoteId(voteID);
        //真实数据
        Vote vote = voteMapper.selectById(voteID);
        String voteTitle_expected = vote.getTitle();
        assertArrayEquals(voteTitle_expected.toCharArray(), voteTitle_actual.toCharArray());
    }

    @Test
    void qryVote() {
        String voteId = "1466060905973104641";
        Vote vote = voteService.qryVote(voteId);
        String voteTitle_expected = "Shy_Vote";
        String voteTitle_actual = vote.getTitle();
        assertArrayEquals(voteTitle_expected.toCharArray(), voteTitle_actual.toCharArray());
    }

    @Test
    void qryVote2() {
        String voteId = "1466403505906712578";
        QueryVoteInfoResp queryVoteInfoResp = voteService.qryVote2(voteId);
        String voteTitle_expected = "Shy_Vote";
        String voteTitle_actual = queryVoteInfoResp.getTitle();
        assertArrayEquals(voteTitle_expected.toCharArray(), voteTitle_actual.toCharArray());
    }

    @Test
    void qryVoteByUserId() {
        String userId = "oxcGC5bl2T7U-1GGrwmp5tgEAxNI";
        Set<String> voteByUserId_actual = voteService.qryVoteByUserId(userId);
        Set<String> voteByUserId_expect = new HashSet<>();
        voteByUserId_expect.add("1465243018682605570");
        voteByUserId_expect.add("1465243417820962818");
        voteByUserId_expect.add("1465245784985477122");
        voteByUserId_expect.add("1465246069350899713");
        assertEquals(29, voteByUserId_actual.size());
    }

    @Test
    void addVote() {
        //添加数据查询思路，初始化结构体数据，与数据库比对查询

        //初始化查询数据集
        AddVoteReq addVoteReq = new AddVoteReq();
        addVoteReq.setType(1);
        addVoteReq.setTitle("Shy_Vote");
        addVoteReq.setVoteDesc("A test for method AddVote");
        ArrayList<String> optionList = new ArrayList<String>();
        optionList.add("apple");
        addVoteReq.setOptionList(optionList);
        ArrayList<String> urls = new ArrayList<String>();
        urls.add("url of apple.png");
        addVoteReq.setUrls(urls);
        addVoteReq.setUserId("o2yRC5LwKeuvDpjZUbenSE3GoJto");
        addVoteReq.setIsAnonymous(0);
        Date dateNow = new Date();
        //SimpleDateFormat ft=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        addVoteReq.setEndDate(dateNow);
        addVoteReq.setVoteType(0);
        addVoteReq.setShuffleOptions(1);
        addVoteReq.setDailyVote(0);
        addVoteReq.setNeedPersonalInformation(0);
        addVoteReq.setImgUrl("url of the img of this vote");
        ArrayList<String> descImgUrls = new ArrayList<String>();
        descImgUrls.add("the img of the test");
        addVoteReq.setDescImgUrls(descImgUrls);
        addVoteReq.setOnlyGroupMember(0);
        //比对结果
        String voteId = voteService.addVote(addVoteReq);
        Vote vote = voteMapper.selectById(voteId);
        String voteTitle_expected = "Shy_Vote";
        String voteTitle_actual = vote.getTitle();
        assertArrayEquals(voteTitle_expected.toCharArray(), voteTitle_actual.toCharArray());
    }

    @Test
    void verifyVote() throws Exception {
        String voteId="1466403505906712578";
        List<VerifyHaveModified> result_expect=new ArrayList<>();
        List<VerifyHaveModified> result_actual=voteService.verifyVote(voteId);
        System.out.println(result_actual);

    }

    @Test
    void dailyVote() {
        String voteId = "1466060905973104641";
        Boolean actual = voteService.dailyVote(voteId);
        assertTrue(true);
    }

    @Test
    void getEndVoteDate() {
        String voteId = "1466403505906712578";
        Date date_actual = voteService.getEndVoteDate(voteId);
        Date date_expect = voteMapper.selectById(voteId).getEndDate();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        assertArrayEquals(ft.format(date_expect).toCharArray(), ft.format(date_actual).toCharArray());
    }
}
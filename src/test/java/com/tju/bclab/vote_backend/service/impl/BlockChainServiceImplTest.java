package com.tju.bclab.vote_backend.service.impl;

import com.tju.bclab.vote_backend.entity.Vote;
import com.tju.bclab.vote_backend.entity.VoteBlockChain;
import com.tju.bclab.vote_backend.io.aelf.schemas.KeyPairInfo;
import com.tju.bclab.vote_backend.mapper.UserMapper;
import com.tju.bclab.vote_backend.mapper.VoteBlockChainMapper;
import com.tju.bclab.vote_backend.mapper.VoteMapper;
import com.tju.bclab.vote_backend.service.BlockChainService;
import com.tju.bclab.vote_backend.service.VoteService;
import com.tju.bclab.vote_backend.vo.resp.VerifyHaveModified;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class BlockChainServiceImplTest {
    @Autowired
    private VoteBlockChainMapper voteBlockChainMapper;

    @Autowired
    private BlockChainService blockChainService;

    @Autowired
    private VoteMapper voteMapper;

    @Autowired
    private VoteService voteService;

    @Autowired
    private UserMapper userMapper;

    @Test
    void voteContract() throws Exception {
        String voteId="1465546402604580866";
        String userId="o2yRC5LwKeuvDpjZUbenSE3GoJto";
        String hash=makeHash(voteId);
        String private_key=userMapper.selectById(userId).getPrivateKey();
        VoteBlockChain voteBlockChain_actual=blockChainService.VoteContract(hash,private_key);
        System.out.println(voteBlockChain_actual);
    }

    @Test
    void voteVerify() throws Exception {
        String voteId="1465647289244766210";
        Long blockHeight=voteBlockChainMapper.selectById(voteMapper.selectById(voteId).getTransactionId()).getBlockHeight();
        List<VerifyHaveModified> list=blockChainService.voteVerify(blockHeight);
        System.out.println(list);
    }

    @Test
    void createWallet() throws Exception {
        KeyPairInfo wallet = blockChainService.createWallet();
        System.out.println(wallet);
    }

    public String makeHash(String voteId) {
        Vote vote = voteMapper.selectById(voteId);
        String string = voteId;
        string = string + vote.getUserId() + vote.getTitle() + vote.getVoteDesc() + vote.getType().toString() + vote.getGmtCreate().toString()
                + vote.getIsAnonymous().toString() + vote.getEndDate().toString() + vote.getVoteType().toString() + vote.getShuffleOptions().toString()
                + vote.getDailyVote().toString() + vote.getNeedPersonalInformation().toString() + vote.getDisplayOption().toString();
        return string;
    }
}
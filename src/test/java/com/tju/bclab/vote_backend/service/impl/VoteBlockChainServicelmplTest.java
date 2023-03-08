package com.tju.bclab.vote_backend.service.impl; 

import com.tju.bclab.vote_backend.entity.VoteBlockChain;
import com.tju.bclab.vote_backend.mapper.VoteBlockChainMapper;
import com.tju.bclab.vote_backend.service.VoteBlockChainService;
import org.junit.runner.RunWith;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.jupiter.api.Assertions.*;

/** 
* VoteBlockChainServicelmpl Tester. 
* 
* @author 
* @since 11/30/2021
*/ 
@RunWith(SpringRunner.class)
@SpringBootTest
public class VoteBlockChainServicelmplTest {
    @Autowired
    private VoteBlockChainMapper voteBlockChainMapper;

    @Autowired
    private VoteBlockChainService voteBlockChainService;

    /** 
    * Method: insert(VoteBlockChain voteBlockChain) 
    */ 
    @Test
    public void testInsert() {
        VoteBlockChain voteBlockChain=new VoteBlockChain();
        String expected="00b032bca1b46b792b4401b7f978f4e860eeb841458b2c31b26dda93995e2310";
        voteBlockChain.setTransactionId("00b032bca1b46b792b4401b7f978f4e860eeb841458b2c31b26dda93995e2310");
        voteBlockChain.setBlockHash("shy_test");
        voteBlockChain.setChainStatus("start");
        voteBlockChain.setBlockHeight(109L);
        voteBlockChainService.insert(voteBlockChain);
        assertArrayEquals(expected.toCharArray(),voteBlockChainMapper.selectById("00b032bca1b46b792b4401b7f978f4e860eeb841458b2c31b26dda93995e2310").getTransactionId().toCharArray());
    }

    /** 
    * Method: qryByTransactionId(String transactionId) 
    */ 
    @Test
    public void testQryByTransactionId() {
        String transactionId="00b032bca1b46b792b4401b7f978f4e860eeb841458b2c31b26dda93995e2399";
        VoteBlockChain voteBlockChain=voteBlockChainService.qryByTransactionId(transactionId);
        System.out.println(voteBlockChain);
    }


}

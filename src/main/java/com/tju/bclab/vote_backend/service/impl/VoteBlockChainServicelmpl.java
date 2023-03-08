package com.tju.bclab.vote_backend.service.impl;


import com.tju.bclab.vote_backend.entity.VoteBlockChain;
import com.tju.bclab.vote_backend.mapper.VoteBlockChainMapper;
import com.tju.bclab.vote_backend.service.VoteBlockChainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoteBlockChainServicelmpl implements VoteBlockChainService {

    @Autowired
     private  VoteBlockChainMapper voteBlockChainMapper;
    @Override
    public void insert(VoteBlockChain voteBlockChain){
        voteBlockChainMapper.insert(voteBlockChain);
    }
    @Override
    public VoteBlockChain qryByTransactionId(String transactionId){
        VoteBlockChain voteBlockChain = voteBlockChainMapper.selectById(transactionId);
        return voteBlockChain;
    }
}

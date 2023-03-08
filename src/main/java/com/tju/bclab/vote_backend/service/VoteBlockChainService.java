package com.tju.bclab.vote_backend.service;

import com.tju.bclab.vote_backend.entity.VoteBlockChain;

public interface VoteBlockChainService {
    void insert(VoteBlockChain voteBlockChain);
    VoteBlockChain qryByTransactionId(String voteId);
}

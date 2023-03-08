package com.tju.bclab.vote_backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tju.bclab.vote_backend.entity.Votedesc;
import com.tju.bclab.vote_backend.mapper.VotedescMapper;
import com.tju.bclab.vote_backend.service.VotedescService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VotedescServiceImpl extends ServiceImpl<VotedescMapper, Votedesc> implements VotedescService {
    @Autowired
    VotedescMapper votedescMapper;

    @Override
    public Boolean addVotedesc(Votedesc votedesc) {
        votedescMapper.insert(votedesc);
        return true;
    }
}

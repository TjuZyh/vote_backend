package com.tju.bclab.vote_backend.service.impl;

import com.tju.bclab.vote_backend.entity.VoteOption;
import com.tju.bclab.vote_backend.mapper.VoteOptionMapper;
import com.tju.bclab.vote_backend.service.VoteOptionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;


@Service
public class VoteOptionServiceImpl extends ServiceImpl<VoteOptionMapper, VoteOption> implements VoteOptionService {
    @Autowired
    VoteOptionMapper voteOptionMapper;
    @Override
    public List<VoteOption> qryVoteOptionsByVoteId(String voteId) {
        HashMap<String,Object> map = new HashMap<>();
        map.put("vote_id", voteId);
        List<VoteOption> result = voteOptionMapper.selectByMap(map);
        return result;
    }

    @Override
    public List<VoteOption> qryVoteOptionsByOptionId(String optionId) {
        HashMap<String,Object> map = new HashMap<>();
        map.put("option_id", optionId);
        return voteOptionMapper.selectByMap(map);
    }
}

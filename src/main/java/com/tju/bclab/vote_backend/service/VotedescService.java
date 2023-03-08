package com.tju.bclab.vote_backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tju.bclab.vote_backend.entity.Votedesc;

public interface VotedescService extends IService<Votedesc> {
    Boolean addVotedesc(Votedesc votedesc);
}

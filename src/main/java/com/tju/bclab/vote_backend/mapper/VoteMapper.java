package com.tju.bclab.vote_backend.mapper;

import com.tju.bclab.vote_backend.entity.Vote;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

// 这个注解是将接口的一个实现类交给spring管理。
@Repository
public interface VoteMapper extends BaseMapper<Vote> {

}

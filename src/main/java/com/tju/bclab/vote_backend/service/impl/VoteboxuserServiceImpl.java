package com.tju.bclab.vote_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tju.bclab.vote_backend.common.ResultCode;
import com.tju.bclab.vote_backend.common.SHA;
import com.tju.bclab.vote_backend.entity.*;
import com.tju.bclab.vote_backend.exception.ApiException;
import com.tju.bclab.vote_backend.mapper.*;
import com.tju.bclab.vote_backend.service.VoteboxuserService;
import com.tju.bclab.vote_backend.vo.common.UserQrcodeInfo;
import com.tju.bclab.vote_backend.vo.req.AddVoteBoxReq;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;


@Service
public class VoteboxuserServiceImpl extends ServiceImpl<VoteboxuserMapper, Voteboxuser> implements VoteboxuserService {

    @Autowired
    private VoteboxuserMapper voteboxuserMapper;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private VoteMapper voteMapper;

    @Autowired
    private VoteOptionMapper voteOptionMapper;

    @Autowired
    private BlockChainServiceImpl blockChainService;

    @Autowired
    private VoteboxTimeMapper voteboxTimeMapper;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean addUserToBox(String voteId, UserQrcodeInfo addUserToBoxReq) {
        // 对应存储即可
        Voteboxuser voteboxuser = new Voteboxuser();
        String userId = addUserToBoxReq.getUserId();
        // 建立wrapper
        QueryWrapper<Voteboxuser> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.eq("vote_id", voteId);
        System.out.println(userId);
        List<Voteboxuser> voteboxusers = voteboxuserMapper.selectList(wrapper);
        System.out.println(voteboxusers.size());
        if (voteboxusers.size() != 0) {
            throw new ApiException(ResultCode.USER_ALREADY_EXIST);
        } else {
            voteboxuser.setUserId(addUserToBoxReq.getUserId());
            voteboxuser.setVoteId(voteId);
            int insert = voteboxuserMapper.insert(voteboxuser);
        }
        return true;
    }

//    @Override
//    public void boxVoteFinish(BoxVoteFinishReq boxVoteFinishReq) {
//        String id = BoxVoteFinishReq.getUserId();
//
//        String privateKey = userMapper.selectById(id).getPrivateKey();
//    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public String addBoxVote(AddVoteBoxReq addVoteBoxReq) {
        String id = addVoteBoxReq.getUserId();
        // String uss = us.getPrivateKey();
        String privateKey = userMapper.selectById(id).getPrivateKey();
        // 存储投票表
        Vote vote = new Vote();
        BeanUtils.copyProperties(addVoteBoxReq, vote);
        // 将截止日期设置为100年后Date
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, c.get(Calendar.YEAR) + 100);
        vote.setEndDate(c.getTime());
        int insert = voteMapper.insert(vote);
        // 如果insert小于0，则导入失败
        if (insert < 0) {
            throw new ApiException(ResultCode.STORE_FAILED);
        }

        // 存储投票箱时间表
        VoteboxTime voteboxTime = new VoteboxTime();
        voteboxTime.setVoteId(vote.getVoteId());
        voteboxTime.setPlanStartTime(addVoteBoxReq.getStartDate());
        int insert1 = voteboxTimeMapper.insert(voteboxTime);

        if (insert1 < 0) {
            throw new ApiException(ResultCode.STORE_FAILED);
        }

        //存储选项表
        String voteId = vote.getVoteId();
        List<String> optionLists = addVoteBoxReq.getOptionList();
        System.out.println(optionLists);
        ;
        for (int i = 0; i < optionLists.size(); i++) {
            String option = optionLists.get(i);
            String url = "";
            VoteOption voteOption = new VoteOption()
                    .setOptionIndex(i)
                    .setOptionStr(option)
                    .setVoteId(voteId)
                    .setImgUrl(url);
            int flag = voteOptionMapper.insert(voteOption);
            if (flag < 0) {
                throw new ApiException(ResultCode.STORE_FAILED);
            }
            //保存到区块链上
            String hash = makeHash(voteId);
            QueryWrapper<VoteOption> optionQueryWrapper = new QueryWrapper<>();
            //获得optionId
            optionQueryWrapper.eq("vote_id", voteId);
            optionQueryWrapper.eq("option_index", i);
            List<VoteOption> optionList = voteOptionMapper.selectList(optionQueryWrapper);

            hash += optionList.get(0).getOptionId();
            try {
                VoteBlockChain voteBlockChain = blockChainService.VoteContract(SHA.sha_func(hash, "SHA-256"), privateKey);
                //上链后更新TransactionId字段
                Vote voteUpdate = voteMapper.selectById(voteId);
                voteUpdate.setTransactionId(voteBlockChain.getTransactionId());
                voteMapper.updateById(voteUpdate);
            } catch (Exception e) {
                throw new ApiException(ResultCode.CONTRACT_CALL_FAILED);
            }
        }
        return voteId;
    }

    @Override
    public Boolean endBoxVote(String voteId) {
        //更新当前voteId对应的投票中的投票截止时间
        Vote vote = voteMapper.selectById(voteId);
        Calendar c = Calendar.getInstance();
        vote.setEndDate(c.getTime());
        int update = voteMapper.updateById(vote);
        if (update < 0) {
            throw new ApiException(ResultCode.UPDATE_FAILED);
        }
        return true;
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

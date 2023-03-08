package com.tju.bclab.vote_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tju.bclab.vote_backend.common.ResultCode;
import com.tju.bclab.vote_backend.common.SHA;
import com.tju.bclab.vote_backend.entity.*;
import com.tju.bclab.vote_backend.exception.ApiException;
import com.tju.bclab.vote_backend.mapper.*;
import com.tju.bclab.vote_backend.service.VoteOptionService;
import com.tju.bclab.vote_backend.service.VoteService;
import com.tju.bclab.vote_backend.service.VoteUserService;
import com.tju.bclab.vote_backend.vo.common.OptionInfo;
import com.tju.bclab.vote_backend.vo.common.VoteInfo;
import com.tju.bclab.vote_backend.vo.req.VoteReq;
import com.tju.bclab.vote_backend.vo.resp.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class VoteUserServiceImpl extends ServiceImpl<VoteUserMapper, VoteUser> implements VoteUserService {
    @Autowired
    private VoteUserMapper voteUserMapper;
    @Autowired
    private VoteMapper voteMapper;

    @Autowired
    private VoteOptionMapper voteOptionMapper;

    @Autowired
    private BlockChainServiceImpl blockChainService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private VoteBlockChainMapper voteBlockChainMapper;

    @Autowired
    private VoteOptionService voteOptionService;

    @Autowired
    private VoteService voteService;

    @Override
    public QueryCreateVote qryCreateVote(String userId) {
        Set<String> myVoteId = voteService.qryVoteByUserId(userId);
        User user = userMapper.selectById(userId);
        List<VoteInfo> voteInfos = new ArrayList<VoteInfo>();
        Date curDate = new Date();
        for (String str : myVoteId) {
            VoteInfo voteInfo = new VoteInfo();
            // 当前查询到的Vote
            Vote vote = voteService.qryVote(str);
            // 判断是否过期
            Date endDate = vote.getEndDate();
            int count = curDate.compareTo(endDate);
            Boolean isExpired=count>0?true:false;
            voteInfo.setIsExpired(isExpired);
            voteInfo.setVoteId(str);
            voteInfo.setTitle(vote.getTitle());
            voteInfo.setVoteDesc(vote.getVoteDesc());
            voteInfo.setIsGroup(vote.getVoteType() <= 1);
            voteInfo.setIsImg(vote.getVoteType() == 1);
            voteInfo.setShareImgUrl(vote.getImgUrl());
            voteInfo.setCreatorHead(user.getAvatarUrl());
            voteInfos.add(voteInfo);
        }
        QueryCreateVote queryCreateVote = new QueryCreateVote();
        queryCreateVote.setMyCreateVote(voteInfos);
        return queryCreateVote;
    }

    @Override
    public QueryJoinedVote qryJoinedVote(String userId) {
        Set<String> myVoteId = qryVoteByUserId(userId);
        List<VoteInfo> voteInfos = new ArrayList<VoteInfo>();
        Date curDate = new Date();
        for (String str : myVoteId) {
            VoteInfo voteInfo = new VoteInfo();
            Vote vote = voteService.qryVote(str);
            User user = userMapper.selectById(vote.getUserId());
            voteInfo.setVoteId(str);
            Date endDate = vote.getEndDate();
            voteInfo.setIsExpired(curDate.after(endDate));
            voteInfo.setTitle(vote.getTitle());
            voteInfo.setVoteDesc(vote.getVoteDesc());
            voteInfo.setIsGroup(vote.getVoteType() <= 1);
            voteInfo.setIsImg(vote.getVoteType() == 1);
            voteInfo.setShareImgUrl(vote.getImgUrl());
            voteInfo.setCreatorHead(user.getAvatarUrl());
            voteInfos.add(voteInfo);
        }
        QueryJoinedVote queryJoinedVote = new QueryJoinedVote();
        queryJoinedVote.setMyJoinedVote(voteInfos);
        return queryJoinedVote;
    }

    @Override
    public QueryVoteResp qryVote(String userId) {
        Set<String> myVoteId = voteService.qryVoteByUserId(userId);
        Set<String> myJoinedVoteId = qryVoteByUserId(userId);

        List<VoteInfo> myVote = new ArrayList<VoteInfo>();
        List<VoteInfo> myJoinedVote = new ArrayList<VoteInfo>();
        for (String str : myVoteId) {
            VoteInfo voteInfo = new VoteInfo();
            Vote vote = voteService.qryVote(str);
            voteInfo.setVoteId(str);
            voteInfo.setTitle(vote.getTitle());
            voteInfo.setVoteDesc(vote.getVoteDesc());
            voteInfo.setIsGroup(vote.getVoteType() <= 1);
            voteInfo.setIsImg(vote.getVoteType() == 1);
            voteInfo.setShareImgUrl(vote.getImgUrl());
            myVote.add(voteInfo);
        }
        for (String str : myJoinedVoteId) {
            if (myVoteId.contains(str)) continue;
            VoteInfo voteInfo = new VoteInfo();
            Vote vote = voteService.qryVote(str);
            voteInfo.setVoteId(str);
            voteInfo.setTitle(vote.getTitle());
            voteInfo.setVoteDesc(vote.getVoteDesc());
            voteInfo.setIsGroup(vote.getVoteType() <= 1);
            voteInfo.setIsImg(vote.getVoteType() == 1);
            voteInfo.setShareImgUrl(vote.getImgUrl());
            myJoinedVote.add(voteInfo);
        }
        QueryVoteResp queryVoteResp = new QueryVoteResp();
        queryVoteResp.setMyVote(myVote);
        queryVoteResp.setMyJoinedVote(myJoinedVote);
        return queryVoteResp;
    }

    @Override
    public ArrayList<VoteDetail> qryVoteDetail(String voteId, String userId) {
        ArrayList<VoteDetail> voteDetails = new ArrayList<>();
        QueryWrapper<VoteUser> voteQueryWrapper = new QueryWrapper<>();
        voteQueryWrapper.eq("vote_id", voteId);
        voteQueryWrapper.eq("user_id", userId);
        List<VoteUser> voteUserList = voteUserMapper.selectList(voteQueryWrapper);
        for (int i = 0; i < voteUserList.size(); i++) {
            VoteDetail voteDetail = new VoteDetail();
            VoteOption voteOption = voteOptionMapper.selectById(voteUserList.get(i).getOptionId());
            VoteBlockChain voteBlockChain = voteBlockChainMapper.selectById(voteUserList.get(i).getTransactionId());
            voteDetail.setVoteOption(voteOption);
            voteDetail.setVoteBlockChain(voteBlockChain);
            voteDetails.add(voteDetail);

        }
        return voteDetails;
    }

    @Override
    public Set<String> qryVoteByMap(HashMap<String, Object> map) {
        List<VoteUser> voteUserss = voteUserMapper.selectByMap(map);
        Set<String> result = new HashSet<String>();
        for (int i = 0; i < voteUserss.size(); ++i) {
            VoteUser voteUser = voteUserss.get(i);
            result.add(voteUser.getVoteId());
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<VoteBlockChain> Vote(VoteReq voteReq) {
       List<VoteBlockChain> blockChainList = new ArrayList<VoteBlockChain>();
        Vote vote = voteMapper.selectById(voteReq.getVoteId());
        List<String> optionLists = voteReq.getOptionList();
        for (int i = 0; i < optionLists.size(); i++) {
            VoteBlockChain voteBlockChain =new VoteBlockChain();
            String option = optionLists.get(i);
            VoteUser voteUser = new VoteUser()
                    .setUserId(voteReq.getUserId())
                    .setVoteId(voteReq.getVoteId())
                    .setOptionId(option);
            int flag1 = voteUserMapper.insert(voteUser);
            if (flag1 < 0) {
                throw new ApiException(ResultCode.STORE_FAILED);
            }

            QueryWrapper<VoteUser> QueryWrapper = new QueryWrapper<>();
            //获得Id
            QueryWrapper.eq("vote_id", voteReq.getVoteId());
            QueryWrapper.eq("user_id", voteReq.getUserId());
            QueryWrapper.eq("option_id", option);
            List<VoteUser> optionList = voteUserMapper.selectList(QueryWrapper);
            String hash = makeHash(optionList.get(0).getId());
            String privateKey = userMapper.selectById(voteReq.getUserId()).getPrivateKey();

           //保存到区块链上
            try {
                voteBlockChain = blockChainService.VoteContract(SHA.sha_func(hash,"SHA-256"),privateKey);
                 //上链后更新TransactionId字段
                VoteUser voteUserUpdate = voteUserMapper.selectById(optionList.get(0).getId());
                voteUserUpdate.setTransactionId(voteBlockChain.getTransactionId());
                voteUserMapper.updateById(voteUserUpdate);
            } catch (Exception e) {
                throw new ApiException(ResultCode.CONTRACT_CALL_FAILED);
            }
            blockChainList.add(voteBlockChain);
            //更新VoteOption表count字段
            VoteOption voteOption = voteOptionMapper.selectById(option);
            voteOption.setCount(voteOption.getCount() + 1);
            int flag2 = voteOptionMapper.updateById(voteOption);
            if (flag2 < 0) {
                throw new ApiException(ResultCode.UPDATE_FAILED);
            }
        }

        //更新vote表投票人数
        vote.setSumUser(vote.getSumUser() + 1);
        //更新vote表投票数
        vote.setSumVote(vote.getSumVote() + optionLists.size());
        int flag = voteMapper.updateById(vote);
        if (flag < 0) {
            throw new ApiException(ResultCode.UPDATE_FAILED);
        }
        return blockChainList;
    }

    @Override
    public Set<String> qryVoteByUserId(String userId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("user_id", userId);
        return qryVoteByMap(map);
    }

    @Override
    public QueryHaveJoinedResp qryHaverJoined(String userId, String voteId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("vote_id", voteId);
        List<VoteUser> voteUserss = voteUserMapper.selectByMap(map);
        List<String> optionIds = new ArrayList<String>();
        for (VoteUser voteUser : voteUserss) {
            optionIds.add(voteUser.getOptionId());
        }
        QueryHaveJoinedResp queryHaveJoinedResp = new QueryHaveJoinedResp();
        if (optionIds.size() == 0) {
            queryHaveJoinedResp.setHaveVoted(false);
            return queryHaveJoinedResp;
        }
        List<OptionInfo> optionInfos = new ArrayList<OptionInfo>();
        for (String str : optionIds) {
            VoteOption voteOption = voteOptionService.qryVoteOptionsByOptionId(str).get(0);
            OptionInfo optionInfo = new OptionInfo();
            optionInfo.setOptionDesc(voteOption.getOptionStr());
            optionInfo.setOptionId(str);
            optionInfos.add(optionInfo);
        }
        queryHaveJoinedResp.setHaveVoted(true);
        queryHaveJoinedResp.setUserOptions(optionInfos);
        return queryHaveJoinedResp;
    }

    public String makeHash(String voteUserId) {
        VoteUser voteUser = voteUserMapper.selectById(voteUserId);
        String string = voteUserId;
        string = string + voteUser.getVoteId() + voteUser.getUserId() + voteUser.getOptionId() + voteUser.getGmtCreate().toString();
        String phone = voteUser.getPhone() == null ? "" : voteUser.getPhone();
        String realName = voteUser.getRealName() == null ? "" : voteUser.getRealName();
        string = string + phone + realName;
        return string;
    }

    @Override
    public Boolean haveVoted(String userId, String voteId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("vote_id", voteId);
        List<VoteUser> voteUserss = voteUserMapper.selectByMap(map);
        if (voteUserss.size() == 0) return false;
        else return true;
    }

    @Override
    public Date dataBaseTime(String userId, String voteId) {
        QueryWrapper<VoteUser> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.eq("vote_id", voteId);
        wrapper.orderByDesc("gmt_create");
        wrapper.last("limit 1");
        VoteUser voteUser = voteUserMapper.selectOne(wrapper);
        return voteUser.getGmtCreate();
    }
}

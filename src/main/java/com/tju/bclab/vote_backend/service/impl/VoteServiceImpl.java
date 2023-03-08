package com.tju.bclab.vote_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tju.bclab.vote_backend.common.ResultCode;
import com.tju.bclab.vote_backend.entity.*;
import com.tju.bclab.vote_backend.exception.ApiException;
import com.tju.bclab.vote_backend.mapper.*;
import com.tju.bclab.vote_backend.service.VoteBlockChainService;
import com.tju.bclab.vote_backend.service.VoteService;
import com.tju.bclab.vote_backend.vo.req.AddVoteReq;
import com.tju.bclab.vote_backend.vo.resp.QueryVoteInfoResp;
import com.tju.bclab.vote_backend.vo.resp.VerifyHaveModified;
import com.tju.bclab.vote_backend.vo.resp.VoteRule;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
public class VoteServiceImpl extends ServiceImpl<VoteMapper, Vote> implements VoteService {
    // 引入vote的mapper
    @Autowired
    private VoteMapper voteMapper;
    // 引入voteOption的mapper
    @Autowired
    private VoteOptionMapper voteOptionMapper;

    @Autowired
    private VotedescMapper votedescMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private VoteUserMapper voteUserMapper;

    @Autowired
    private VoteboxTimeMapper voteboxTimeMapper;

    @Autowired
    private BlockChainServiceImpl blockChainService;

    @Autowired
    private VoteBlockChainService voteBlockChainService;

    @Autowired
    private VoteService voteService;


    @Override
    public String qryTileByVoteId(String voteId) {
        Vote vote = qryVote(voteId);
        return vote.getTitle();
    }

    @Override
    public Vote qryVote(String voteId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("vote_id", voteId);
        List<Vote> votes = voteMapper.selectByMap(map);
        if (votes.size() == 0) {
            throw new ApiException(ResultCode.QUERY_FAILED);
        }
        Vote vote = votes.get(0);
        return vote;
    }

    @Override
    public QueryVoteInfoResp qryVote2(String voteId) {
        QueryVoteInfoResp queryVoteInfoResp = new QueryVoteInfoResp();
        //获取投票
        Vote vote = voteMapper.selectById(voteId);
        //获取投票发起人
        User sponsor = userMapper.selectById(vote.getUserId());
        //获取投票图片
        Votedesc votedesc = votedescMapper.selectById(voteId);
        //封装投票规则
        VoteRule voteRule = new VoteRule();
        voteRule.setVoteType(vote.getVoteType());
        voteRule.setType(vote.getType());
        voteRule.setDisplayOption(vote.getDisplayOption());
        voteRule.setEndDate(vote.getEndDate());
        voteRule.setNeedPersonalInformation(vote.getNeedPersonalInformation());
        voteRule.setDailyVote(vote.getDailyVote());
        voteRule.setShuffleOptions(vote.getShuffleOptions());
        voteRule.setOnlyGroupMember(vote.getOnlyGroupMember());
        voteRule.setIsAnonymous(vote.getIsAnonymous());

        QueryWrapper<VoteOption> optionQueryWrapper = new QueryWrapper<>();
        optionQueryWrapper.eq("vote_id", voteId);
        List<VoteOption> optionList = voteOptionMapper.selectList(optionQueryWrapper);

        QueryWrapper<VoteUser> voteQueryWrapper = new QueryWrapper<>();
        voteQueryWrapper.eq("vote_id", voteId);
        voteQueryWrapper.select("distinct user_id");
        List<VoteUser> voteUserList = voteUserMapper.selectList(voteQueryWrapper);

        List<String> voteAvatarUrls = new ArrayList<>();
        for (int i = 0; i < voteUserList.size(); i++) {
            User voter = userMapper.selectById(voteUserList.get(i).getUserId());
            voteAvatarUrls.add(voter.getAvatarUrl());
        }

        //设置投票标题
        queryVoteInfoResp.setTitle(vote.getTitle());
        //设置投票描述信息
        queryVoteInfoResp.setVoteDesc(vote.getVoteDesc());
        //设置投票图片
        if (votedesc == null)
            queryVoteInfoResp.setDsecImgUrl("");
        else
            queryVoteInfoResp.setDsecImgUrl((votedesc.getImgUrl() == null) ? "" : votedesc.getImgUrl());
        //设置投票选项
        queryVoteInfoResp.setOptionList(optionList);
        //设置投票人头像
        queryVoteInfoResp.setVoteAvatarUrls(voteAvatarUrls);
        //设置投票分享图片
        queryVoteInfoResp.setShareImgUrl((vote.getImgUrl() == null) ? "" : vote.getImgUrl());
        //设置投票规则
        queryVoteInfoResp.setRule(voteRule);
        //设置投票发起时间
        queryVoteInfoResp.setVoteCreate(vote.getGmtCreate());
        //设置发起人头像
        queryVoteInfoResp.setSponsorAvatarUrl(sponsor.getAvatarUrl());
        //设置发起人昵称
        queryVoteInfoResp.setNickName(sponsor.getNickName());
        //设置投票人数
        queryVoteInfoResp.setSumUser(vote.getSumUser());
        //设置至少可选
        queryVoteInfoResp.setLeastLimit(vote.getLeastLimit());
        //设置至多可选
        queryVoteInfoResp.setMostLimit(vote.getMostLimit());

        //设置openId
        queryVoteInfoResp.setOpenId(sponsor.getUserId());
        return queryVoteInfoResp;
    }

    @Override
    public Set<String> qryVoteByUserId(String userId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("user_id", userId);
        List<Vote> votes = voteMapper.selectByMap(map);
        Set<String> result = new HashSet<String>();
        for (int i = 0; i < votes.size(); ++i) {
            Vote vote = votes.get(i);
            result.add(vote.getVoteId());
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String addVote(AddVoteReq addVoteReq) {//传入的userid如果没在数据库中会报错

        String id = addVoteReq.getUserId();
        User us = userMapper.selectById(id);

//        String uss = us.getPrivateKey();
        String privateKey = userMapper.selectById(id).getPrivateKey();


        //存储投票表
        Vote vote = new Vote();
        BeanUtils.copyProperties(addVoteReq, vote);
        int insert = voteMapper.insert(vote);
        // 如果insert小于0，则导入失败
        if (insert < 0) {
            throw new ApiException(ResultCode.STORE_FAILED);
        }

        // 存储投票描述表
        ArrayList<String> descImgUrls = addVoteReq.getDescImgUrls();
        for (String descImgUrl : descImgUrls) {
            Votedesc votedesc = new Votedesc();
            votedesc.setVoteId(vote.getVoteId());
            votedesc.setImgUrl(descImgUrl);
            int insert1 = votedescMapper.insert(votedesc);
            if (insert1 < 0) {
                throw new ApiException(ResultCode.STORE_FAILED);
            }
        }

        //存储选项表
        String voteId = vote.getVoteId();
        List<String> optionLists = addVoteReq.getOptionList();
        List<String> urls = addVoteReq.getUrls();
        System.out.println(optionLists);
        System.out.println(urls);
        for (int i = 0; i < optionLists.size(); i++) {
            String option = optionLists.get(i);
            String url = urls.get(i);
            VoteOption voteOption = new VoteOption()
                    .setOptionIndex(i)
                    .setOptionStr(option)
                    .setVoteId(voteId)
                    .setImgUrl(url);
            int flag = voteOptionMapper.insert(voteOption);
            if (flag < 0) {
                throw new ApiException(ResultCode.STORE_FAILED);
            }
            // 保存到区块链上
            String hash = makeHash(voteId);
            QueryWrapper<VoteOption> optionQueryWrapper = new QueryWrapper<>();
            //获得optionId
            optionQueryWrapper.eq("vote_id", voteId);
            optionQueryWrapper.eq("option_index", i);
            List<VoteOption> optionList = voteOptionMapper.selectList(optionQueryWrapper);

            hash += optionList.get(0).getOptionId();
            try {
                VoteBlockChain voteBlockChain = blockChainService.VoteContract(hash, privateKey);
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


    public List<VerifyHaveModified> verifyVote(String voteId) throws Exception {
        Vote vote = voteService.qryVote(voteId);
        VoteBlockChain voteBlockChain = voteBlockChainService.qryByTransactionId(vote.getTransactionId());
        Long blockHeight = voteBlockChain.getBlockHeight();
        List<VerifyHaveModified> hashArr = blockChainService.voteVerify(blockHeight);
        List<VerifyHaveModified> result = compareHash(hashArr);
        return result;
    }

    public List<VerifyHaveModified> compareHash(List<VerifyHaveModified> hashlist) {

        for (int i = 0; i < hashlist.size(); i++) {
            int record = 0;
            for (int j = 0; j < hashlist.size(); j++) {
                if (hashlist.get(i).getBlockHash().equals(hashlist.get(j).getBlockHash())) {
                    record++;
                }
            }
            hashlist.get(i).setCountRecord(record);
        }
        for (int k = 0; k < hashlist.size(); k++) {
            if (hashlist.get(k).getCountRecord() < 3) hashlist.get(k).setHaveModified(true);
            else hashlist.get(k).setHaveModified(false);
        }
        return hashlist;

    }

    @Override
    public Boolean dailyVote(String voteId) {
        Vote vote = voteMapper.selectById(voteId);
        int dailyVote = vote.getDailyVote();
        if (dailyVote == 1) return true;//是每日一投
        else return false;
    }

    @Override
    public Date getEndVoteDate(String voteId) {
        Vote vote = voteMapper.selectById(voteId);
        return vote.getEndDate();
    }

    /**
     * 判断投票箱投票是否已经超过当前开始时间
     *
     * @param voteId
     * @return {@link Boolean}
     * @author Zhang QiHang.
     * @date 2021/12/22 20:06
     */
    @Override
    public Boolean afterStartTime(String voteId) {
        QueryWrapper<VoteboxTime> voteboxTimeQueryWrapper = new QueryWrapper<>();
        voteboxTimeQueryWrapper.eq("vote_id", voteId);
        List<VoteboxTime> voteboxTimeList = voteboxTimeMapper.selectList(voteboxTimeQueryWrapper);
        if (voteboxTimeList.size() != 0) {
            Date startTime = voteboxTimeList.get(0).getPlanStartTime();
            Date nowTime = new Date();
            if (nowTime.after(startTime)) {
                return true;
            }
        }
        return false;
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

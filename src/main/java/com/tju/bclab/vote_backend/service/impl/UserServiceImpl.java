package com.tju.bclab.vote_backend.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tju.bclab.vote_backend.common.ResultCode;
import com.tju.bclab.vote_backend.common.WeChatUtil;
import com.tju.bclab.vote_backend.entity.User;
import com.tju.bclab.vote_backend.entity.VoteBlockChain;
import com.tju.bclab.vote_backend.exception.ApiException;
import com.tju.bclab.vote_backend.io.aelf.schemas.KeyPairInfo;
import com.tju.bclab.vote_backend.io.aelf.utils.QRCodePostToOss;
import com.tju.bclab.vote_backend.mapper.UserMapper;
import com.tju.bclab.vote_backend.service.UserService;
import com.tju.bclab.vote_backend.vo.common.UserQrcodeInfo;
import com.tju.bclab.vote_backend.vo.req.UserLoginReq;
import com.tju.bclab.vote_backend.vo.resp.UserLoginResp;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;


@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    UserMapper userMapper;
    @Autowired
    BlockChainServiceImpl blockChainService;
    @Autowired
    private QRCodePostToOss qRCodeUtil;

    @Value("${wx.appId}")
    private String appId;

    @Value("${wx.secret}")
    private String secret;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserLoginResp Login(UserLoginReq userLoginReq) {

        //1.接收小程序前端发送的code
        String code = userLoginReq.getCode();
        //2.携带appid,code,appsecret请求微信官方服务器，获取对应参数
        JSONObject SessionKeyOpenId = WeChatUtil.getSessionKeyOrOpenId(appId, secret, code);
        String openId = SessionKeyOpenId.getString("openid");
        String sessionKey = SessionKeyOpenId.getString("session_key");

        // 3.校验签名，小程序发送的签名要和服务器端生成的签名对应
        // 取得用户非敏感性息
        JSONObject rawDataJson = JSON.parseObject(userLoginReq.getRawData());
        String signature2 = DigestUtils.sha1Hex(userLoginReq.getRawData() + sessionKey);
        String signature = userLoginReq.getSignature();
        if (!signature.equals(signature2)) {
            throw new ApiException(ResultCode.SIGN_CHECKED_FAILED);
        }
        // 4. 返回新的sKey，由UUID生成
        String sKey = UUID.randomUUID().toString();
        // 5.判断用户是否为新用户，是：存入表；否：更新登陆时间（TODO）
        User user = this.userMapper.selectById(openId);
        if (user == null) {
            // 获取rawData中的用户信息
            String nickName = rawDataJson.getString("nickName");
            String avatarUrl = rawDataJson.getString("avatarUrl");
            String gender = rawDataJson.getString("gender");
            String city = rawDataJson.getString("city");
            String country = rawDataJson.getString("country");
            String province = rawDataJson.getString("province");
            // 对用户进行上链
            KeyPairInfo wallet = blockChainService.createWallet();
            try {
                VoteBlockChain flag = blockChainService.AddUserContract(wallet.getPrivateKey());
            } catch (Exception e) {
                throw new ApiException(ResultCode.CONTRACT_CALL_FAILED);
            }


            // 创建新用户
            User newUser = new User()
                    .setUserAddress(wallet.getAddress())
                    .setPrivateKey(wallet.getPrivateKey())
                    .setType(1)
                    .setUserId(openId)
                    .setSkey(sKey)
                    .setLastVisitTime(new Date())
                    .setSessionKey(sessionKey)
                    .setCity(city)
                    .setNickName(nickName)
                    .setAvatarUrl(avatarUrl)
                    .setGender(Integer.parseInt(gender))
                    .setCountry(country)
                    .setProvince(province);
            int insert = this.userMapper.insert(newUser);
            if (insert < 0) {
                throw new ApiException(ResultCode.STORE_FAILED);
            }
        } else {
            // 已存在，更新用户登录时间
            user.setLastVisitTime(new Date());
            // 重新设置sKey
            user.setSkey(sKey);
            this.userMapper.updateById(user);
        }

        // 6. 把新的sKey返回前端
        UserLoginResp userLoginResp = new UserLoginResp(sKey, openId);
        return userLoginResp;
    }

    @Override
    public String getQrCode(String userId) {
        //保存的图片全路径
        Long curTime = System.currentTimeMillis();
        String fileName = userId + "-" + "-" + curTime;
        User selectedUser = this.userMapper.selectById(userId);

        UserQrcodeInfo userQrcodeInfo = new UserQrcodeInfo();

        // 身份二维码，只存储userId和时间戳
        userQrcodeInfo.setUserId(userId);
        userQrcodeInfo.setCreateTime(curTime);
        String resultJson = JSONObject.toJSONString(userQrcodeInfo);

        try {
            String userQrcodePath = qRCodeUtil.encode(resultJson, fileName, false);
            return userQrcodePath;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

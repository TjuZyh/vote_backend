package com.tju.bclab.vote_backend.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.google.protobuf.ByteString;
import com.tju.bclab.vote_backend.common.ResultCode;
import com.tju.bclab.vote_backend.common.SHA;
import com.tju.bclab.vote_backend.entity.VoteBlockChain;
import com.tju.bclab.vote_backend.entity.VoteUser;
import com.tju.bclab.vote_backend.exception.ApiException;
import com.tju.bclab.vote_backend.io.aelf.protobuf.generated.Client;
import com.tju.bclab.vote_backend.io.aelf.protobuf.generated.Core;
import com.tju.bclab.vote_backend.io.aelf.protobuf.generated.DataStoreContractOuterClass;
import com.tju.bclab.vote_backend.io.aelf.schemas.KeyPairInfo;
import com.tju.bclab.vote_backend.io.aelf.schemas.SendTransactionInput;
import com.tju.bclab.vote_backend.io.aelf.schemas.SendTransactionOutput;
import com.tju.bclab.vote_backend.io.aelf.schemas.TransactionResultDto;
import com.tju.bclab.vote_backend.io.aelf.sdk.AElfClient;
import com.tju.bclab.vote_backend.io.aelf.utils.ByteArrayHelper;
import com.tju.bclab.vote_backend.mapper.VoteBlockChainMapper;
import com.tju.bclab.vote_backend.mapper.VoteUserMapper;
import com.tju.bclab.vote_backend.service.BlockChainService;
import com.tju.bclab.vote_backend.vo.resp.VerifyHaveModified;
import com.tju.bclab.vote_backend.vo.resp.VerifyVote;
import com.tju.bclab.vote_backend.vo.resp.VerifyVoteResp;
import lombok.extern.slf4j.Slf4j;
import org.bitcoinj.core.Base58;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class BlockChainServiceImpl implements BlockChainService {
    @Autowired
    private  VoteBlockChainMapper voteBlockChainMapper;

    @Autowired
    private VoteUserMapper voteUserMapper;

    private AElfClient client;
    private AElfClient client2;
    private AElfClient client3;
    private AElfClient client4;
    private String dataStoreContractAddress;
    private List<AElfClient> clients;

    public BlockChainServiceImpl(){
        this.client=new AElfClient("http://18.163.40.216:8000");
        this.dataStoreContractAddress="225ajURvev5rgX8HnMJ8GjbPnRxUrCHoD7HUjhWQqewEJ5GAv1";
        this.client2 = new AElfClient("http://18.163.40.216:8000");
        this.client3 = new AElfClient("http://18.163.40.216:8000");
        this.client4 = new AElfClient("http://18.163.40.216:8000");
        this.clients = Lists.newArrayList(client,client2,client3,client4);
    }

    @Override
    public VoteBlockChain InitialContract(String hash, String privateKey) throws Exception {
        // 通过节点 privateKey 获取节点地址，该地址即为合约的 owner
        String ownerAddress = client.getAddressFromPrivateKey(privateKey);
        Client.Address.Builder owner = Client.Address.newBuilder();
        owner.setValue(ByteString.copyFrom(Base58.decodeChecked(ownerAddress)));

        // 构建合约调用时需要传递的参数
        // 具体定义见 io.aelf 包中的 proto 文件
        DataStoreContractOuterClass.HashStoreInput.Builder datastoreinput = DataStoreContractOuterClass.HashStoreInput.newBuilder();
        // 对不同字段设置相应值
        datastoreinput.setValue(hash);
        DataStoreContractOuterClass.HashStoreInput datastoreObj = datastoreinput.build();
        // 构建调用智能合约方法对应的参数
        Core.Transaction.Builder transactionDataStore = client.generateTransaction(ownerAddress, dataStoreContractAddress, "InitiationHashStore", datastoreObj.toByteArray());
        Core.Transaction transactionDataStoreObj = transactionDataStore.build();
        // 用 owner 地址对该交易签名
        String signature = client.signTransaction(privateKey, transactionDataStoreObj);
        transactionDataStore.setSignature(ByteString.copyFrom(ByteArrayHelper.hexToByteArray(signature)));
        transactionDataStoreObj = transactionDataStore.build();

        // 发送交易，该逻辑主要对应合约中的set方法
        SendTransactionInput sendTransactionInputObj = new SendTransactionInput();
        sendTransactionInputObj.setRawTransaction(Hex.toHexString(transactionDataStoreObj.toByteArray()));
        SendTransactionOutput sendResult = client.sendTransaction(sendTransactionInputObj);
        TransactionResultDto transactionResult;
        // 循环查询接口，根据id获得交易执行结果
        while (true) {
            transactionResult = client.getTransactionResult(sendResult.getTransactionId());
            if ("MINED".equals(transactionResult.getStatus())) {
                VoteBlockChain voteBlockChain = new VoteBlockChain();
                voteBlockChain.setTransactionId(transactionResult.getTransactionId());
                voteBlockChain.setBlockHeight(transactionResult.getBlockNumber());
                voteBlockChain.setBlockHash(transactionResult.getBlockHash());
                voteBlockChain.setChainStatus(transactionResult.getStatus());
                voteBlockChainMapper.insert(voteBlockChain);
                // 当状态为MINED表示执行成功，直接返回

                return  voteBlockChain ;
            } else if ("PENDING".equals(transactionResult.getStatus())) {
                // 当状态为PENDING表示还未获取到结果，等待
                Thread.sleep(300);
            } else {
                // 若其他结果则抛出异常
                throw new ApiException(ResultCode.CONTRACT_CALL_FAILED);
            }
        }

    }

    @Override
    public VoteBlockChain VoteContract(String hash, String privateKey) throws Exception {
        // 通过节点 privateKey 获取节点地址，该地址即为合约的 owner
        String ownerAddress = client.getAddressFromPrivateKey(privateKey);
        Client.Address.Builder owner = Client.Address.newBuilder();
        owner.setValue(ByteString.copyFrom(Base58.decodeChecked(ownerAddress)));

        // 构建合约调用时需要传递的参数
        // 具体定义见 io.aelf 包中的 proto 文件
        DataStoreContractOuterClass.HashStoreInput.Builder datastoreinput = DataStoreContractOuterClass.HashStoreInput.newBuilder();
        // 对不同字段设置相应值
        datastoreinput.setValue(hash);
        DataStoreContractOuterClass.HashStoreInput datastoreObj = datastoreinput.build();
        // 构建调用智能合约方法对应的参数
        Core.Transaction.Builder transactionDataStore = client.generateTransaction(ownerAddress, dataStoreContractAddress, "VoteHashStore", datastoreObj.toByteArray());
        Core.Transaction transactionDataStoreObj = transactionDataStore.build();
        // 用 owner 地址对该交易签名
        String signature = client.signTransaction(privateKey, transactionDataStoreObj);
        transactionDataStore.setSignature(ByteString.copyFrom(ByteArrayHelper.hexToByteArray(signature)));
        transactionDataStoreObj = transactionDataStore.build();

        // 发送交易，该逻辑主要对应合约中的set方法
        SendTransactionInput sendTransactionInputObj = new SendTransactionInput();
        sendTransactionInputObj.setRawTransaction(Hex.toHexString(transactionDataStoreObj.toByteArray()));
        SendTransactionOutput sendResult = client.sendTransaction(sendTransactionInputObj);
        TransactionResultDto transactionResult;
        // 循环查询接口，根据id获得交易执行结果
        while (true) {
            transactionResult = client.getTransactionResult(sendResult.getTransactionId());
            if ("MINED".equals(transactionResult.getStatus())) {
                VoteBlockChain voteBlockChain = new VoteBlockChain();
                voteBlockChain.setTransactionId(transactionResult.getTransactionId());
                voteBlockChain.setBlockHeight(transactionResult.getBlockNumber());
                voteBlockChain.setBlockHash(transactionResult.getBlockHash());
                voteBlockChain.setChainStatus(transactionResult.getStatus());
                voteBlockChainMapper.insert(voteBlockChain);
                // 当状态为MINED表示执行成功，直接返回

                return  voteBlockChain ;
            } else if ("PENDING".equals(transactionResult.getStatus())) {
                // 当状态为PENDING表示还未获取到结果，等待
                Thread.sleep(300);
            } else {
                // 若其他结果则抛出异常
                throw new ApiException(ResultCode.CONTRACT_CALL_FAILED);
            }
        }
    }

    @Override
    public VoteBlockChain ResultContract(String hash, String privateKey) throws Exception {
        // 通过节点 privateKey 获取节点地址，该地址即为合约的 owner
        String ownerAddress = client.getAddressFromPrivateKey(privateKey);
        Client.Address.Builder owner = Client.Address.newBuilder();
        owner.setValue(ByteString.copyFrom(Base58.decodeChecked(ownerAddress)));

        // 构建合约调用时需要传递的参数
        // 具体定义见 io.aelf 包中的 proto 文件
        DataStoreContractOuterClass.HashStoreInput.Builder datastoreinput = DataStoreContractOuterClass.HashStoreInput.newBuilder();
        // 对不同字段设置相应值
        datastoreinput.setValue(hash);
        DataStoreContractOuterClass.HashStoreInput datastoreObj = datastoreinput.build();
        // 构建调用智能合约方法对应的参数
        Core.Transaction.Builder transactionDataStore = client.generateTransaction(ownerAddress, dataStoreContractAddress, "ResultHashStore", datastoreObj.toByteArray());
        Core.Transaction transactionDataStoreObj = transactionDataStore.build();
        // 用 owner 地址对该交易签名
        String signature = client.signTransaction(privateKey, transactionDataStoreObj);
        transactionDataStore.setSignature(ByteString.copyFrom(ByteArrayHelper.hexToByteArray(signature)));
        transactionDataStoreObj = transactionDataStore.build();

        // 发送交易，该逻辑主要对应合约中的set方法
        SendTransactionInput sendTransactionInputObj = new SendTransactionInput();
        sendTransactionInputObj.setRawTransaction(Hex.toHexString(transactionDataStoreObj.toByteArray()));
        SendTransactionOutput sendResult = client.sendTransaction(sendTransactionInputObj);
        TransactionResultDto transactionResult;
        // 循环查询接口，根据id获得交易执行结果
        while (true) {
            transactionResult = client.getTransactionResult(sendResult.getTransactionId());
            if ("MINED".equals(transactionResult.getStatus())) {
                VoteBlockChain voteBlockChain = new VoteBlockChain();
                voteBlockChain.setTransactionId(transactionResult.getTransactionId());
                voteBlockChain.setBlockHeight(transactionResult.getBlockNumber());
                voteBlockChain.setBlockHash(transactionResult.getBlockHash());
                voteBlockChain.setChainStatus(transactionResult.getStatus());
                voteBlockChainMapper.insert(voteBlockChain);
                // 当状态为MINED表示执行成功，直接返回

                return  voteBlockChain ;
            } else if ("PENDING".equals(transactionResult.getStatus())) {
                // 当状态为PENDING表示还未获取到结果，等待
                Thread.sleep(300);
            } else {
                // 若其他结果则抛出异常
                throw new ApiException(ResultCode.CONTRACT_CALL_FAILED);
            }
        }

    }


    // 该方法暂时不用
    @Override
    public List<VerifyHaveModified> voteVerify( Long blockHeight) throws Exception{
        List<VerifyHaveModified> list = new ArrayList<>();
        String hashArr[] = new String[clients.size()];
        for(int i = 0;i < clients.size(); i ++){
            VerifyHaveModified obj = new VerifyHaveModified();
            obj.setClientIp(clients.get(i).AElfClientUrl );
            String blockHash = clients.get(i).getBlockByHeight(blockHeight).getBlockHash();
            obj.setBlockHash(blockHash);
            list.add(obj);
        }
        return list;
    }

    @Override
    public KeyPairInfo createWallet() {
        try {
            return client.generateKeyPairInfo();
        } catch (Exception e) {
            throw new ApiException(ResultCode.CREATE_WALLET_FAILED);
        }
    }


    @Override
    public VoteBlockChain InitializeContract(String privateKey) throws Exception {
        // 通过节点 privateKey 获取节点地址，该地址即为合约的 owner
        String ownerAddress = client.getAddressFromPrivateKey(privateKey);
        Client.Address.Builder owner = Client.Address.newBuilder();
        owner.setValue(ByteString.copyFrom(Base58.decodeChecked(ownerAddress)));

        // 构建合约调用时需要传递的参数
        // 具体定义见 io.aelf 包中的 proto 文件
        DataStoreContractOuterClass.InitializeInput.Builder initializeInput = DataStoreContractOuterClass.InitializeInput.newBuilder();
        // 对不同字段设置相应值
        initializeInput.setFlag("0");
        DataStoreContractOuterClass.InitializeInput initializeObj = initializeInput.build();

        /// 构建调用智能合约方法对应的参数
        Core.Transaction.Builder transactionDataStore = client.generateTransaction(ownerAddress, dataStoreContractAddress, "Initialize", initializeObj.toByteArray());
        Core.Transaction transactionDataStoreObj = transactionDataStore.build();
        // 用 owner 地址对该交易签名
        String signature = client.signTransaction(privateKey, transactionDataStoreObj);
        transactionDataStore.setSignature(ByteString.copyFrom(ByteArrayHelper.hexToByteArray(signature)));
        transactionDataStoreObj = transactionDataStore.build();

        // 发送交易，该逻辑主要对应合约中的set方法
        SendTransactionInput sendTransactionInputObj = new SendTransactionInput();
        sendTransactionInputObj.setRawTransaction(Hex.toHexString(transactionDataStoreObj.toByteArray()));
        SendTransactionOutput sendResult = client.sendTransaction(sendTransactionInputObj);
        TransactionResultDto transactionResult;
        // 循环查询接口，根据id获得交易执行结果
        while (true) {
            transactionResult = client.getTransactionResult(sendResult.getTransactionId());
            if ("MINED".equals(transactionResult.getStatus())) {
                VoteBlockChain voteBlockChain = new VoteBlockChain();
                voteBlockChain.setTransactionId(transactionResult.getTransactionId());
                voteBlockChain.setBlockHeight(transactionResult.getBlockNumber());
                voteBlockChain.setBlockHash(transactionResult.getBlockHash());
                voteBlockChain.setChainStatus(transactionResult.getStatus());
                voteBlockChainMapper.insert(voteBlockChain);
                // 当状态为MINED表示执行成功，直接返回

                return  voteBlockChain ;
            } else if ("PENDING".equals(transactionResult.getStatus())) {
                // 当状态为PENDING表示还未获取到结果，等待
                Thread.sleep(300);
            } else {
                // 若其他结果则抛出异常
                throw new ApiException(ResultCode.CONTRACT_CALL_FAILED);
            }
        }
    }

    @Override
    public VoteBlockChain AddUserContract(String privateKey) throws Exception {
        // 通过节点 privateKey 获取节点地址，该地址即为合约的 owner
        String ownerAddress = client.getAddressFromPrivateKey(privateKey);
        Client.Address.Builder owner = Client.Address.newBuilder();
        owner.setValue(ByteString.copyFrom(Base58.decodeChecked(ownerAddress)));

        // 构建合约调用时需要传递的参数
        // 具体定义见 io.aelf 包中的 proto 文件
        Client.Address.Builder user = Client.Address.newBuilder();
        user.setValue(ByteString.copyFrom(Base58.decodeChecked(ownerAddress)));
        Client.Address userObj = user.build();

        /// 构建调用智能合约方法对应的参数
        Core.Transaction.Builder transactionDataStore = client.generateTransaction(ownerAddress, dataStoreContractAddress, "AddUser", userObj.toByteArray());
        Core.Transaction transactionDataStoreObj = transactionDataStore.build();
        // 用 owner 地址对该交易签名
        String signature = client.signTransaction(privateKey, transactionDataStoreObj);
        transactionDataStore.setSignature(ByteString.copyFrom(ByteArrayHelper.hexToByteArray(signature)));
        transactionDataStoreObj = transactionDataStore.build();

        // 发送交易，该逻辑主要对应合约中的set方法
        SendTransactionInput sendTransactionInputObj = new SendTransactionInput();
        sendTransactionInputObj.setRawTransaction(Hex.toHexString(transactionDataStoreObj.toByteArray()));
        SendTransactionOutput sendResult = client.sendTransaction(sendTransactionInputObj);
        TransactionResultDto transactionResult;
        // 循环查询接口，根据id获得交易执行结果
        while (true) {
            transactionResult = client.getTransactionResult(sendResult.getTransactionId());
            if ("MINED".equals(transactionResult.getStatus())) {
                VoteBlockChain voteBlockChain = new VoteBlockChain();
                voteBlockChain.setTransactionId(transactionResult.getTransactionId());
                voteBlockChain.setBlockHeight(transactionResult.getBlockNumber());
                voteBlockChain.setBlockHash(transactionResult.getBlockHash());
                voteBlockChain.setChainStatus(transactionResult.getStatus());
                voteBlockChainMapper.insert(voteBlockChain);
                // 当状态为MINED表示执行成功，直接返回

                return  voteBlockChain ;
            } else if ("PENDING".equals(transactionResult.getStatus())) {
                // 当状态为PENDING表示还未获取到结果，等待
                Thread.sleep(300);
            } else {
                // 若其他结果则抛出异常
                throw new ApiException(ResultCode.CONTRACT_CALL_FAILED);
            }
        }
    }

    @Override
    public VerifyVoteResp queryMultiNodes(String transactionId) throws Exception {
        //获取数据库里的数据
        QueryWrapper<VoteUser> voteQueryWrapper = new QueryWrapper<>();
        voteQueryWrapper.eq("transaction_id", transactionId);
        List<VoteUser> optionList = voteUserMapper.selectList(voteQueryWrapper);
        VoteUser voteUser = optionList.get(0);
        String voteUserId = voteUser.getId();
        String hash = makeHash(voteUserId);
        hash= SHA.sha_func(hash, "SHA-256");

        List<VerifyVote> rlt = new ArrayList<>();
        for(int i=0;i<clients.size();i++){
            TransactionResultDto transactionResult=clients.get(i).getTransactionResult(transactionId);
            String value = JSONObject.parseObject(transactionResult.getTransaction().getParams()).getString("value");
            VerifyVote verifyVote =new VerifyVote();
            verifyVote.setDataBaseHash(hash);
            verifyVote.setChainHash(value);
            verifyVote.setEqual(hash.equals(value));
            rlt.add(verifyVote);
        }
        VerifyVoteResp verifyVoteResp = new VerifyVoteResp();
        verifyVoteResp.setVerifyVoteList(rlt);
        return verifyVoteResp;
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
}

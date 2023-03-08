package com.tju.bclab.vote_backend.common;
import lombok.Getter;

@Getter
public enum ResultCode {
    //
    SUCCESS(0, "成功"),
    FAIL(1, "失败"),

    ENTITY_NOT_NULL(100000, "实体不能为空"),
    PARAMETER_ERROR(100001, "传入参数不能为空"),

    CONTRACT_CALL_FAILED(110001, "合约调用失败"),
    STORE_FAILED(110001, "未成功存储前端数据"),
    CREATE_WALLET_FAILED(110002, "创建钱包失败"),
    UPDATE_FAILED(110003, "数据库更新信息失败"),
    VOTE_TIME(110004, "投票已过期"),
    QUERY_FAILED(110005, "数据库查询信息失败, 信息不存在"),
    OPENFILE_FAILED(110006, "文件打开失败"),
    HAVE_VOTED(110007,"已投过票或投票为开始"),
    ENTITY_NULL(100010, "实体为空"),
    BID_EXIST(5000, "当前招标信息已存在"),
    SIGN_CHECKED_FAILED(100002,"签名校验失败"),
    USER_ALREADY_EXIST(100003, "用户已存在"),
    GLOBAL_EXCEPTION(999999, "全局异常");




    private Integer code;
    private String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}


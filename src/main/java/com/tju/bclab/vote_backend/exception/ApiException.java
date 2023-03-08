package com.tju.bclab.vote_backend.exception;

import com.tju.bclab.vote_backend.common.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ApiException extends RuntimeException{
    public static final ApiException EX_GLOBAL = new ApiException(ResultCode.GLOBAL_EXCEPTION);
    /**
     * 异常响应码、响应信息
     */
    private ResultCode apiErrorCode;

    /**
     * 其他返回信息
     */
    private Object msg;

    public ApiException(ResultCode apiErrorCode) {
        super(apiErrorCode.getMessage());
        this.apiErrorCode = apiErrorCode;
    }

}
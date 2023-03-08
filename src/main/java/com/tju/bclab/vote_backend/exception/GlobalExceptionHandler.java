package com.tju.bclab.vote_backend.exception;

import com.tju.bclab.vote_backend.common.R;
import com.tju.bclab.vote_backend.common.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 处理全局异常(统一的常见异常)
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public R<Object> error(Exception e){
        log.error(e.getMessage(), e);
        return R.error(ResultCode.GLOBAL_EXCEPTION);
    }

    /**
     * api异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = ApiException.class)
    @ResponseBody
    public R<Object> error(ApiException e){
        log.error(e.getMessage(), e);
        return R.error(e.getApiErrorCode());
    }

}
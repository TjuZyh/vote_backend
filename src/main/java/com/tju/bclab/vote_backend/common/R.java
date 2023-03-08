package com.tju.bclab.vote_backend.common;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class R<T> {
    @ApiModelProperty(value = "是否成功")
    private Boolean success;

    @ApiModelProperty(value = "返回码")
    private Integer code;

    @ApiModelProperty(value = "返回消息")
    private String message;

    // 返回数据库利用的是Map类型，因此data其实不是固定的，可以一直进行push
    @ApiModelProperty(value = "返回数据")
    private T data;

    // 私有化构造方法，因此别人无法随意调用，只能利用我写的方法进行调用。

    /**
     * 无参构造器 - 请求成功，无返回data
     */
    private R() {
        this.code = ResultCode.SUCCESS.getCode();
        this.success = true;
    }

    /**
     * 有参构造器 - 请求成功，返回data
     * @param data data
     */
    private R(T data) {
        this.code = ResultCode.SUCCESS.getCode();
        this.success = true;
        this.data = data;
    }

    /**
     * 有参构造器 - 请求失败
     * @param resultCode resultCode
     */
    private R(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.success = false;
        this.message = resultCode.getMessage();
    }


    public static<T> R<T> ok(){
        return new R<>();
    }

    public static<T> R<T> ok(T data){
        return new R<>(data);
    }

    public static<T> R<T> error(ResultCode resultCode) {
        return new R<>(resultCode);
    }

    public static<T> R<T> error() {
        return new R<>(ResultCode.FAIL);
    }
}

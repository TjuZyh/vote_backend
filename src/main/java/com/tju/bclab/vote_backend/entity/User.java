package com.tju.bclab.vote_backend.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="User对象", description="用户表")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户唯一id，对应微信的openid，固定为28位")
    @TableId(value = "user_id", type = IdType.INPUT)
    private String userId;

    @ApiModelProperty(value = "用户的密钥，用于登录对接微信官方接口")
    private String skey;

    @ApiModelProperty(value = "用户的密钥，用于登录对接微信官方接口")
    private String sessionKey;

    @ApiModelProperty(value = "用户头像")
    private String avatarUrl;

    @ApiModelProperty(value = "用户所在城市")
    private String city;

    @ApiModelProperty(value = "用户所在国家")
    private String country;

    @ApiModelProperty(value = "男 1，女 0")
    private Integer gender;

    @ApiModelProperty(value = "用户使用语言")
    private String language;

    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    @ApiModelProperty(value = "用户所在省份")
    private String province;

    @ApiModelProperty(value = "创建时间")
    //TableField为自动填充的注解，当满足条件时，会自动对属性进行填充，比如更新
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;

    @ApiModelProperty(value = "最近登陆时间")
    private Date LastVisitTime;

    @ApiModelProperty(value = "0普通用户 1管理员")
    private Integer type;

    @ApiModelProperty(value = "用户生成的唯一钱包地址")
    private String userAddress;

    @ApiModelProperty(value = "用户私钥")
    private String privateKey;

    @ApiModelProperty(value = "未知")
    private String mnemonic;

    @ApiModelProperty(value = "未登入 1，已登入 0")
    private Integer logIn;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "手机号")
    private String phone;

}

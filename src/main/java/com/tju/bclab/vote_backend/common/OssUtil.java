package com.tju.bclab.vote_backend.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OssUtil {
    public static String END_POINT;
    public static String KEY_ID;
    public static String KEY_SECRET;
    public static String BUCKET_NAME;

    @Value("${aliyun.oss.file.endpoint}")
    public void setEndPoint(String endPoint) {
        END_POINT = endPoint;
    }

    @Value("${aliyun.oss.file.keyid}")
    public void setKeyId(String keyId) {
        KEY_ID = keyId;
    }

    @Value("${aliyun.oss.file.keysecret}")
    public void setKeySecret(String keySecret) {
        KEY_SECRET = keySecret;
    }

    @Value("${aliyun.oss.file.bucketname}")
    public void setBucketName(String bucketName) {
        BUCKET_NAME = bucketName;
    }
}

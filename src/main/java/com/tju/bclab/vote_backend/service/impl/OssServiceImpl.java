package com.tju.bclab.vote_backend.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.tju.bclab.vote_backend.common.OssUtil;
import com.tju.bclab.vote_backend.common.ResultCode;
import com.tju.bclab.vote_backend.exception.ApiException;
import com.tju.bclab.vote_backend.service.OssService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {

    private String endpoint = OssUtil.END_POINT;
    private String accessKeyId = OssUtil.KEY_ID;
    private String accessKeySecret = OssUtil.KEY_SECRET;
    private String bucketName = OssUtil.BUCKET_NAME;

    public String uploadImg(MultipartFile file) {


        try{
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

            // 填写本地文件的完整路径。如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件流。
            InputStream inputStream = file.getInputStream();
            String uuid= UUID.randomUUID().toString().replaceAll("-","");
            String fileName = uuid+".jpg";
            // 依次填写Bucket名称（例如examplebucket）和Object完整路径（例如exampledir/exampleobject.txt）。Object完整路径中不能包含Bucket名称。
            ossClient.putObject(bucketName, fileName, inputStream);

            // 关闭OSSClient。
            ossClient.shutdown();

            String url="https://"+bucketName+"."+endpoint+"/"+fileName;

            return url;
        }catch(Exception e){
            throw new ApiException(ResultCode.OPENFILE_FAILED);
        }

    }

    public String uploadPdf(MultipartFile file) {


        try{
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

            // 填写本地文件的完整路径。如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件流。
            InputStream inputStream = file.getInputStream();
            String uuid= UUID.randomUUID().toString().replaceAll("-","");
            String fileName = uuid+".pdf";
            // 依次填写Bucket名称（例如examplebucket）和Object完整路径（例如exampledir/exampleobject.txt）。Object完整路径中不能包含Bucket名称。
            ossClient.putObject(bucketName, fileName, inputStream);

            // 关闭OSSClient。
            ossClient.shutdown();

            String url="https://"+bucketName+"."+endpoint+"/"+fileName;

            return url;
        }catch(Exception e){
            throw new ApiException(ResultCode.OPENFILE_FAILED);
        }

    }
}

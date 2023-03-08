package com.tju.bclab.vote_backend.io.aelf.utils;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
//import com.sasitron.valueadded.common.AliyunOssProperties;
//import com.sasitron.valueadded.common.QrCodeProperties;
import com.tju.bclab.vote_backend.common.OssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

@Slf4j
@Component
public class QRCodePostToOss {

    private static final String CHARSET = "utf-8";
    private static final String FORMAT_NAME = "JPG";
    String endpoint = OssUtil.END_POINT;
    String accessKeyId = OssUtil.KEY_ID;
    String accessKeySecret = OssUtil.KEY_SECRET;
    String bucketName = OssUtil.BUCKET_NAME;

    /**
     * 当文件夹不存在时，mkdirs会自动创建多层目录，区别于mkdir．(mkdir如果父目录不存在则会抛出异常)
     *
     * @param destPath 存放目录
     * @author lanyuan
     * Email: mmm333zzz520@163.com
     * @date 2013-12-11 上午10:16:36
     */
    public boolean mkdirs(String destPath) {
        File file = new File(destPath);
        //当文件夹不存在时，mkdirs会自动创建多层目录，区别于mkdir．(mkdir如果父目录不存在则会抛出异常)
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
            return true;
        }
        return false;
    }


    /**
     * 生成二维码(内嵌LOGO)
     *
     * @param content      内容
     * @param imgPath      LOGO地址
     * @param destPath     存放目录
     * @param needCompress 是否压缩LOGO
     * @param flage        设置是否保存本地（true:本地保存，flage不保存）
     * @throws Exception
     * @return 阿里OSS图片路径
     */
    public String encode(String content, String imgPath, String destPath,
                         boolean needCompress, String fileName, boolean flage) throws Exception {
        QRCodeUtil qRCodeUtils = new QRCodeUtil();
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = qRCodeUtils.createImage(content,
                    needCompress);
        } catch (IOException ex) {
            ex.getMessage();
        } finally {
            if (bufferedImage != null) {
                bufferedImage.getGraphics().dispose();
            }
        }
        String file = fileName + ".jpg";
        //若路径不为空则存储本地当前路径中
        if (flage) {
            mkdirs(destPath);
            boolean write = ImageIO.write(bufferedImage, FORMAT_NAME, new File(destPath + "/" + file));
            if (write) {
                System.out.println(destPath + file);
                return destPath + file;
            }
        }
        /**
         * 上传阿里OSS
         */
        //BufferedImage 转换为 InputStream
        InputStream inputStream = this.bufferedImageToInputStream(bufferedImage);
        try {
            //2 创建OssClient对象
            OSS ossClient = new OSSClientBuilder().build("http://" + endpoint, accessKeyId, accessKeySecret);
            //3 获取文件信息，为了上传
            // meta设置请求头
            ObjectMetadata meta = new ObjectMetadata();
            //此处若不设置上传类型则默认application/octet-stream，若为默认则在访问文件时，会导致下载操作。
            //在浏览器中，会弹出下载保存的对话框
            meta.setContentType("image/jpg");
            //4 设置知道文件夹
            ossClient.putObject(bucketName, fileName, inputStream, meta);
            //5 关闭ossClient
            ossClient.shutdown();
            //6 返回上传之后地址，拼接地址
            String uploadUrl = "https://" + bucketName + "." + endpoint +  "/" +fileName;
            return uploadUrl;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            inputStream.close();
        }
    }

    /**
     * 将BufferedImage转换为InputStream
     *
     * @param image
     * @return
     */
    public InputStream bufferedImageToInputStream(BufferedImage image) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", os);
            InputStream input = new ByteArrayInputStream(os.toByteArray());
            return input;
        } catch (IOException e) {
            log.error("提示:", e);
        }
        return null;
    }
    /*
     *
     * @param content  二维码内容内容
     * @param fileName 文件地址
     * @throws Exception
     */
    public String encode(String content, String fileName, boolean flage)
            throws Exception {
        return this.encode(content, null, null, true, fileName, flage);
    }


}


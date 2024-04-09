package com.example.my_theatre.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.ObjectMetadata;
import com.example.my_theatre.entity.enums.ErrorCode;
import com.example.my_theatre.entity.po.Order;
import com.example.my_theatre.exception.BusinessException;
import com.google.zxing.WriterException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

@Data
@AllArgsConstructor
@Slf4j
public class AliOssUtil {

    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;

    /**
     * 上传图片至阿里云
     * @param file
     * @return
     * @throws BusinessException
     */
    public String upload(MultipartFile file) throws BusinessException {

        //获取原始文件名
        String originalFilename = file.getOriginalFilename();
        //截取原始文件名的后缀
        String extensionobje= originalFilename.substring(originalFilename.lastIndexOf("."));
        //利用UUID生成文件名
        String objectName = UUID.randomUUID().toString() + extensionobje;

        byte[] bytes= new byte[0];
        try {
            bytes = file.getBytes();
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.UPLOAD_IS_FAIL);
        }


        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            // 创建PutObject请求。
            ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(bytes));
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }

        //文件访问路径规则 https://BucketName.Endpoint/ObjectName
        StringBuilder stringBuilder = new StringBuilder("https://");
        stringBuilder
                .append(bucketName)
                .append(".")
                .append(endpoint)
                .append("/")
                .append(objectName);

        log.info("文件上传到:{}", stringBuilder.toString());

        return stringBuilder.toString();
    }

    /**
     * 生成二维码并且上传到 阿里云OSS中
     */
    public  String  CreateQRandUpload(HashMap<String, Object> order) throws IOException, WriterException {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // 生成二维码图片
        CodeImageUtil.writeToStream(order, out, 300, 300);

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        URL qrCodeUrl;
        try {
            // 生成一个随机的文件名
            String fileName = cn.hutool.core.lang.UUID.randomUUID().toString() + ".jpg";

            // 将二维码写入内存流
            InputStream inputStream = new ByteArrayInputStream(out.toByteArray());

            // 创建上传Object的Metadata
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(out.size()); // 设置上传内容长度

            // 上传文件流
            ossClient.putObject(bucketName, fileName, inputStream, metadata);

            // 生成的二维码上传至阿里云OSS成功后，生成URL并设置过期时间
            Date expiration = new Date(System.currentTimeMillis() + 3600 * 1000); // 设置URL过期时间为1小时
            qrCodeUrl = ossClient.generatePresignedUrl(bucketName, fileName, expiration);

            // 打印生成的URL
            System.out.println("生成的二维码已上传至阿里云OSS，URL为：" + qrCodeUrl.toString());
        } finally {
            // 关闭OSS客户端
            ossClient.shutdown();
        }
        return qrCodeUrl.toString();

    }
}

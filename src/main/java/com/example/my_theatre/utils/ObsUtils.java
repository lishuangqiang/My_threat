package com.example.my_theatre.utils;

import com.obs.services.ObsClient;
import com.obs.services.model.DeleteObjectResult;
import com.obs.services.model.ObsObject;
import com.obs.services.model.PutObjectResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

@Component
public class ObsUtils {


    private static ObsClient obsClient;


    public static String bucketName;

    private static String parentDir;

    //静态属性注入
    @Autowired
    public void setObsClient(ObsClient obsClient) {
        ObsUtils.obsClient = obsClient;
    }

    @Value("${hwyun.obs.bucketName}")
    public void setBucketName(String bucketName) {
        ObsUtils.bucketName = bucketName;
    }
    @Value("${hwyun.obs.parentDir}")
    public void setParentDir(String parentDir) {
        ObsUtils.parentDir = parentDir;
    }

    /**
     * 上传文件
     *
     * @param multipartFile 文件
     * @param objectKey     文件名，如果桶中有文件夹的话，如往test文件上传test.txt文件，那么objectKey就是test/test.txt
     * @throws Exception
     */
    public static PutObjectResult uploadFile(MultipartFile multipartFile, String objectKey) throws Exception {
        InputStream inputStream = multipartFile.getInputStream();
        PutObjectResult putObjectResult = obsClient.putObject(bucketName, parentDir+"/"+objectKey, inputStream);
        inputStream.close();
        //obsClient.close();
        return putObjectResult;
    }

    /**
     * 上传文件
     *
     * @param file      文件
     * @param objectKey 文件名，如果桶中有文件夹的话，如往test文件上传test.txt文件，那么objectKey就是test/test.txt
     * @throws Exception
     */
    public static PutObjectResult uploadFileByFile(File file, String objectKey) throws Exception {
        InputStream inputStream = new FileInputStream(file);
        PutObjectResult putObjectResult = obsClient.putObject(bucketName, parentDir+"/"+objectKey, inputStream);
        inputStream.close();
       // obsClient.close();
        return putObjectResult;
    }


    /**
     * 文件下载
     * @param objectKey 文件名
     * @return
     */
    public static ObsObject downFile(String objectKey){
        ObsObject obsObject = obsClient.getObject(bucketName, parentDir+"/"+objectKey);
       // InputStream ins = obsObject.getObjectContent();

        return obsObject;
    }

    /**
     * 文件下载
     * @param bucketName 桶
     * @param objectKey 文件名
     * @return
     */
    public static ObsObject downFile(String bucketName,String objectKey){
        ObsObject obsObject = obsClient.getObject(bucketName, objectKey);
       // InputStream ins = obsObject.getObjectContent();

        return obsObject;
    }
    /**
     * 删除文件
     *
     * @param objectKey 文件名，如果桶中有文件夹的话，如往test文件上传test.txt文件，那么objectKey就是test/test.txt
     * @throws Exception
     */
    public static DeleteObjectResult deleteFile(String objectKey) throws Exception {
        DeleteObjectResult deleteObjectResult = obsClient.deleteObject(bucketName, objectKey);
        //obsClient.close();
        return deleteObjectResult;
    }


}

package com.example.my_theatre.controller.admin;

import com.example.my_theatre.common.BaseResponse;
import com.example.my_theatre.common.ResultUtils;

import com.example.my_theatre.entity.enums.ErrorCode;
import com.example.my_theatre.exception.BusinessException;
import com.example.my_theatre.utils.AliOssUtil;
import com.example.my_theatre.utils.ObsUtils;
import com.obs.services.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/test")
public class test {

    //上传图片
    @Resource
    private ObsUtils obsUtils;
    @Resource
    private AliOssUtil aliOssUtil;

    /**
     * 基于华为云OBS实现图片上传
     * @param multipartFile
     * @param fileName
     * @return
     * @throws Exception
     */
    @PostMapping ("/huawei")
    public BaseResponse<String> test(@RequestParam("file") MultipartFile multipartFile, @RequestParam("fileName") String fileName) throws Exception {
        {

            PutObjectResult putObjectResult = obsUtils.uploadFile(multipartFile, fileName);
            String url =putObjectResult.getObjectUrl();
            return  ResultUtils.success(url);
        }
    }

    /**
     * 基于阿里云OSS实现图片上传
     * @param file
     * @return
     * @throws IOException
     */
        @PostMapping("/upload")
        public BaseResponse<String> upload(MultipartFile file)  {
            log.info("文件上传：{}", file);
            //判空：
            if(file == null)
            {
                return ResultUtils.error(5001, "文件不能为空");
            }

            //在这里的名字我们使用UUID生成，避免因为重名产生图片文件覆盖问题
            try {
                //返回一个指向文件的URL
                String filePath = aliOssUtil.upload(file);
                return ResultUtils.success(filePath);
            } catch (BusinessException e) {
                return ResultUtils.error(ErrorCode.UPLOAD_IS_FAIL, e.getMessage());
            }
        }
}

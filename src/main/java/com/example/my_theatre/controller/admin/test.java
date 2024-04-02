package com.example.my_theatre.controller.admin;

import com.example.my_theatre.common.BaseResponse;
import com.example.my_theatre.common.ResultUtils;
import com.example.my_theatre.utils.ObsUtils;
import com.obs.services.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
@Slf4j
@RequestMapping("/test")
public class test {

    //上传图片
    @Resource
    private ObsUtils obsUtils;
    @PostMapping ("/huawei")
    public BaseResponse<String> test(@RequestParam("file") MultipartFile multipartFile, @RequestParam("fileName") String fileName) throws Exception {
        {

            PutObjectResult putObjectResult = ObsUtils.uploadFile(multipartFile, fileName);
            String url =putObjectResult.getObjectUrl();
            return  ResultUtils.success(url);
        }
    }
}

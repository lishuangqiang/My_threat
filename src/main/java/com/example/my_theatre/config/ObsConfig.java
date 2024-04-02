package com.example.my_theatre.config;

import com.obs.services.ObsClient;
import com.obs.services.exception.ObsException;
import lombok.Data;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
public class ObsConfig {
    @Value("${hwyun.obs.accessKey}")
    private String ak;

    @Value("${hwyun.obs.securityKey}")
    private String sk;

    @Value("${hwyun.obs.endPoint}")
    private String endPoint;

    @Bean
    public ObsClient getObsClient(){
        ObsClient obsClient = new ObsClient(ak, sk, endPoint);
        return obsClient;
    }

}

package com.example.my_theatre.service.impl;

import com.example.my_theatre.entity.constants.Constants;
import com.example.my_theatre.entity.constants.EmailConstant;

import com.example.my_theatre.service.EmailCodeService;
import com.example.my_theatre.utils.StringTools;

import jakarta.mail.MessagingException;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;


@Service
public class EmailCodeServiceImpl implements EmailCodeService {

    @Resource
    private JavaMailSender javaMailSender;

    @Override
    public void sendEmailCode(String email) {
        //生成随机数，调用私有方法sendEmailCode进行发送
        String code = StringTools.getRandomNumber(Constants.LENGTH_5);
        sendEmailCode(email, code);

    }

    //私有方法
    private void sendEmailCode(String toEmail, String code) {
        try {
            //true 代表支持复杂的类型
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(javaMailSender.createMimeMessage(),true);
            //邮件发信人
            mimeMessageHelper.setFrom(EmailConstant.SendMiler);
            //邮件收信人
            mimeMessageHelper.setTo(toEmail);
            //邮件主题
            mimeMessageHelper.setSubject(EmailConstant.subjective);
            //邮件内容
            mimeMessageHelper.setText(EmailConstant.text+code);
            //邮件发送时间
            mimeMessageHelper.setSentDate(new Date());
            //发送邮件
            javaMailSender.send(mimeMessageHelper.getMimeMessage());
            System.out.println("发送邮件成功：" +EmailConstant.SendMiler+"===>"+toEmail);

        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("发送邮件失败："+e.getMessage());
        }

    }
}

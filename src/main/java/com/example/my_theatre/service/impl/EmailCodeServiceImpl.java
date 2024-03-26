package com.example.my_theatre.service.impl;

import com.example.my_theatre.entity.constants.Constants;
import com.example.my_theatre.entity.constants.EmailConstant;


import com.example.my_theatre.entity.constants.UserConstants;
import com.example.my_theatre.entity.dto.Userinfo;
import com.example.my_theatre.entity.enums.ErrorCode;
import com.example.my_theatre.entity.po.User;
import com.example.my_theatre.exception.BusinessException;
import com.example.my_theatre.mapper.UserMapper;
import com.example.my_theatre.service.EmailCodeService;
import com.example.my_theatre.utils.RedisUtil;
import com.example.my_theatre.utils.StringTools;

import com.example.my_theatre.utils.VerifyRegexUtils;
import jakarta.mail.MessagingException;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;


@Service
public class EmailCodeServiceImpl implements EmailCodeService {

    @Resource
    private JavaMailSender javaMailSender;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    UserMapper userMapper;


    /**
     * 发送验证码
     * @param email
     */
    @Override
    public void sendEmailCode(String email) {
        //生成随机数，调用私有方法sendEmailCode进行发送
        String code = StringTools.getRandomNumber(Constants.LENGTH_5);
        sendEmailCode(email, code);
        //存入Reids,设置过期时间为五分钟
        redisUtil.set(email, code, 60 * 5);
    }

    //私有方法
    private void sendEmailCode(String toEmail, String code) {
        try {
            //true 代表支持复杂的类型
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(javaMailSender.createMimeMessage(), true);
            //邮件发信人
            mimeMessageHelper.setFrom(EmailConstant.SendMiler);
            //邮件收信人
            mimeMessageHelper.setTo(toEmail);
            //邮件主题
            mimeMessageHelper.setText(EmailConstant.subjective);
            // 对邮箱进行正则表达式判断：
            //邮件内容
            mimeMessageHelper.setText(EmailConstant.text + code+EmailConstant.tail);
            //邮件发送时间
            mimeMessageHelper.setSentDate(new Date());
            //发送邮件
            javaMailSender.send(mimeMessageHelper.getMimeMessage());
            System.out.println("发送邮件成功：" + EmailConstant.SendMiler + "===>" + toEmail);

        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("发送邮件失败：" + e.getMessage());
        }

    }

    /**
     * 用户注册
     * @param Email
     * @param username
     * @param password
     * @param code
     */
    @Transactional(rollbackFor = Exception.class)
    public void register(String Email, String username, String password, String code) {
        //先查询邮箱是否重复注册过
        User ifuser = userMapper.findUserByemail(Email);
        if (ifuser != null) {
           throw new BusinessException(ErrorCode.OPERATION_ERROR, "此邮箱已注册");
        }
        //对用户名进行正则校验
        if (!VerifyRegexUtils.VerifyName(username)) {
            throw new BusinessException(ErrorCode.ACCOUNT_ERROR, "用户名格式不正确，请检查输入格式");
        }
        //校验验证码：
         String rightCode = (String) redisUtil.get(Email);
        if(code.equals(rightCode)) {
            //删除验证码
            redisUtil.del(Email);
            // 注册表：将用户名存入数据库
            User user = new User();
            user.setUserName(username);
            user.setUserAccount(Email);
            user.setUserPassword(password);
            user.setUserStatus(UserConstants.User_start);
            user.setMaxBBook(10);
            user.setNowBBook(0);
            user.setCreateTime(new Date());
            user.setUpdateTime(new Date());
            userMapper.register(user);
        }
        else
        {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "验证码错误或失效,请重新输入或获取");
        }
    }

    @Override
    public void deleteUserSelf(String email, String password) {
        //先检查用户密码是否正确：
        User userinfo = userMapper.findUserByemail(email);
        if(!userinfo.getUserPassword().equals(StringTools.encodeByMD5(password)))
        {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "用户密码不正确，请重新输入或获取");
        }
        userMapper.deleteUserByEmail(email);
    }

    public static void main(String[] args) {
        System.out.println(StringTools.encodeByMD5("123456"));
    }
}


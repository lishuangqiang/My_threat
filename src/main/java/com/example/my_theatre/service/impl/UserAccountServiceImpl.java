package com.example.my_theatre.service.impl;

import com.example.my_theatre.entity.constants.Constants;
import com.example.my_theatre.entity.constants.EmailConstant;


import com.example.my_theatre.entity.constants.JwtClaimsConstant;
import com.example.my_theatre.entity.constants.UserConstants;
import com.example.my_theatre.entity.dto.Userinfo;
import com.example.my_theatre.entity.enums.ErrorCode;
import com.example.my_theatre.entity.po.User;
import com.example.my_theatre.entity.vo.UserinfoVo;
import com.example.my_theatre.exception.BusinessException;
import com.example.my_theatre.mapper.UserMapper;
import com.example.my_theatre.properties.JwtProperties;
import com.example.my_theatre.service.UserAccountService;
import com.example.my_theatre.utils.JwtUtil;
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
import java.util.HashMap;
import java.util.Map;


@Service
public class UserAccountServiceImpl implements UserAccountService {

    @Resource
    private JavaMailSender javaMailSender;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    UserMapper userMapper;
    @Resource
    private JwtProperties jwtProperties;


    /**
     * 发送验证码
     *
     * @param email
     */
    @Override
    public void sendEmailCode(String email, String type) throws MessagingException {
        //生成随机数，调用私有方法sendEmailCode进行发送
        String code = StringTools.getRandomNumber(Constants.LENGTH_5);
        sendEmailCode(email, code, type);
        //存入Reids,设置过期时间为五分钟
        if (type.equals(EmailConstant.regiser)) {
            redisUtil.set(EmailConstant.regiser + email, code, 60 * 5);
        } else if (type.equals(EmailConstant.forget)) {
            redisUtil.set(EmailConstant.forget + email, code, 60 * 5);
        } else if (type.equals(EmailConstant.logout)) {
            redisUtil.set(EmailConstant.logout + email, code, 60 * 5);
        } else if (type.equals(EmailConstant.login)) {
            redisUtil.set(EmailConstant.login + email, code, 60 * 5);

        }

    }


    //私有方法
    private void sendEmailCode(String toEmail, String code, String type) throws MessagingException {
        try {
            //true 代表支持复杂的类型
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(javaMailSender.createMimeMessage(), true);
            //邮件发信人
            mimeMessageHelper.setFrom(EmailConstant.SendMiler);
            //邮件收信人
            mimeMessageHelper.setTo(toEmail);
            //邮件主题
            mimeMessageHelper.setText(EmailConstant.subjective);
            //判断类型
            if (type.equals(EmailConstant.regiser)) {
                //邮件头部

                //邮件内容
                mimeMessageHelper.setText(EmailConstant.regiser_text + code + EmailConstant.tail);
                //邮件发送时间
                mimeMessageHelper.setSentDate(new Date());
                //发送邮件
                javaMailSender.send(mimeMessageHelper.getMimeMessage());
                System.out.println("发送邮件成功：" + EmailConstant.SendMiler + "===>" + toEmail);
            } else if (type.equals(EmailConstant.forget)) {
                //邮件内容
                mimeMessageHelper.setText(EmailConstant.forget_text + code + EmailConstant.tail);
                //邮件发送时间
                mimeMessageHelper.setSentDate(new Date());
                //发送邮件
                javaMailSender.send(mimeMessageHelper.getMimeMessage());
                System.out.println("发送邮件成功：" + EmailConstant.SendMiler + "===>" + toEmail);
            } else if (type.equals(EmailConstant.logout)) {
                //邮件内容
                mimeMessageHelper.setText(EmailConstant.logout_text + code + EmailConstant.tail);
                //邮件发送时间
                mimeMessageHelper.setSentDate(new Date());
                //发送邮件
                javaMailSender.send(mimeMessageHelper.getMimeMessage());
                System.out.println("发送邮件成功：" + EmailConstant.SendMiler + "===>" + toEmail);
            } else if (type.equals(EmailConstant.login)) {
                //邮件内容
                mimeMessageHelper.setText(EmailConstant.login_text + code + EmailConstant.tail);
                //邮件发送时间
                mimeMessageHelper.setSentDate(new Date());
                //发送邮件
                javaMailSender.send(mimeMessageHelper.getMimeMessage());
                System.out.println("发送邮件成功：" + EmailConstant.SendMiler + "===>" + toEmail);

            }

        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("发送邮件失败：" + e.getMessage());
        }

    }

    /**
     * 用户注册
     *
     * @param Email
     * @param username
     * @param password
     * @param code
     */
    @Transactional(rollbackFor = Exception.class)
    public void register(String Email, String username, String password, String code) throws BusinessException {
        //先查询邮箱是否重复注册过
        User ifuser = userMapper.findUserByemail(Email);
        if (ifuser != null) {
            throw new BusinessException(ErrorCode.EMAIL_ERROR, "此邮箱已注册");
        }
        //对用户名进行正则校验

        if (!VerifyRegexUtils.VerifyName(username)) {
            throw new BusinessException(ErrorCode.ACCOUNT_ERROR, "用户名格式不正确，请检查输入格式");
        }
        //校验验证码：
        String rightCode = (String) redisUtil.get(EmailConstant.regiser + Email);
        if (code.equals(rightCode)) {
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
        } else {
            throw new BusinessException(ErrorCode.CODE_ERROR, "验证码错误或失效,请重新输入或获取");
        }
    }

    /**
     * 用户注销
     *
     * @param email
     * @param password
     * @param code
     * @throws BusinessException
     */
    @Override
    public void deleteUserSelf(String email, String password, String code) throws BusinessException {
        //检查用户验证码是否错误
        String rightCode = (String) redisUtil.get(EmailConstant.logout + email);
        if (!code.equals(rightCode)) {
            throw new BusinessException(ErrorCode.CODE_ERROR, "验证码错误或失效,请重新输入或获取");
        }
        redisUtil.del(EmailConstant.logout + email);

        //先检查用户密码是否正确：
        User userinfo = userMapper.findUserByemail(email);
        if (!userinfo.getUserPassword().equals(StringTools.encodeByMD5(password))) {
            throw new BusinessException(ErrorCode.CODE_ERROR, "用户密码不正确，请重新输入或获取");
        }
        userMapper.deleteUserByEmail(email);
        //todo
        //被拦截器拦截之后，返回错误界面方便前端进行跳转
    }

    @Override
    public UserinfoVo login(String email, String password) throws BusinessException {
        //对密码字符串进行MD5加密
        String userintoPassword = StringTools.encodeByMD5(password);
        //根据用户邮箱搜索用户密码
        User userinfo = userMapper.findUserByemail(email);

        //校验用户状态
        if (userinfo.getUserStatus() == UserConstants.User_prohibit) {
            throw new BusinessException(ErrorCode.STATUS_ERROR, "此账号已被禁用，请联系管理员");
        }
        //校验用户密码
        if (!userinfo.getUserPassword().equals(userintoPassword)) {
            throw new BusinessException(ErrorCode.PASSWPRD_ERROR, "用户密码不正确，请重新输入或获取");
        }

        //写入token
        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ACCOUNT, userinfo.getUserAccount());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);


        //员工登录成功之后向前端返回的数据，我们采用自定义结构封装
        UserinfoVo userLoginVO = new UserinfoVo();

        userLoginVO.setEmail(userinfo.getUserAccount());
        userLoginVO.setToken(token);
        userLoginVO.setUserName(userinfo.getUserName());

        return userLoginVO;
    }

    /**
     * 用户重置密码
     *
     * @param email ,code
     * @param code
     */
    @Override
    public void forgetPassword(String email, String password, String code) throws BusinessException {
        //校验验证码
        String rightcode = (String) redisUtil.get(EmailConstant.forget + email);
        if (!code.equals(rightcode)) {
            throw new BusinessException(ErrorCode.CODE_ERROR, "验证码错误或失效,请重新输入或获取");
        }
        //删除当前验证码：
        redisUtil.del(EmailConstant.forget + email);
        //更新用户密码
        userMapper.updatePasswordByemail(email, password);

    }

    @Override
    public UserinfoVo loginByCode(String email, String code) throws BusinessException{
        //校验用户邮箱



        //校验验证码
        String rightcode = (String) redisUtil.get(EmailConstant.login + email);
        if(!code.equals(rightcode))
        {
            throw new BusinessException(ErrorCode.CODE_ERROR, "验证码错误或失效,请重新输入或获取");
        }

        User user = userMapper.findUserByemail(email);

        //写入token
        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ACCOUNT, user.getUserAccount());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);
        //包装返回给前端的用户实体
        UserinfoVo userinfoVo = new UserinfoVo();
        userinfoVo.setEmail(user.getUserAccount());
        userinfoVo.setUserName(user.getUserName());
        userinfoVo.setToken(token);

        return userinfoVo;

    }


    public static void main(String[] args) {
        System.out.println(StringTools.encodeByMD5("11111"));
    }
}


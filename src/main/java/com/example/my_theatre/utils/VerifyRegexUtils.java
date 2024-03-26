package com.example.my_theatre.utils;

public class VerifyRegexUtils {

    //邮箱验证码正则表达式()
    static final String REGEX_EMAIL = "^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$";
    //用户名称正则表达式(由数字、26个英文字母或者下划线组成的字符串)
    static final String REGEX_NAME = "^\\w+$";

    //对邮箱进行正则表达式判断：
    public static final Boolean VerifyEmail(String email) {
        if (email.matches(REGEX_EMAIL)) {
            return true;
        } else {
            return false;
        }
    }

    //对用户名称进行正则表达判断
    public static final Boolean VerifyName(String name) {
        if (name.matches(REGEX_NAME)) {
            return true;
        } else {
            return false;
        }
    }
}

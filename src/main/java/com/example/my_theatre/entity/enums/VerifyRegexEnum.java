package com.example.my_theatre.entity.enums;

public enum VerifyRegexEnum {
    NO("", "不校验"),
    PASSWORD("^\\w+$", "由数字、26个英文字母或者下划线组成的字符串"),
    EMAIL("^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$", "邮箱"),
    USER_NAME("^[a-zA-Z0-9_\\u4e00-\\u9fa5]+$", "数字，字母，中文，下划线");

    private String regex;
    private String desc;

    VerifyRegexEnum(String regex, String desc) {
        this.regex = regex;
        this.desc = desc;
    }

    public String getRegex() {
        return regex;
    }

    public String getDesc() {
        return desc;
    }
}
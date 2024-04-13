package com.example.my_theatre.entity.enums;

/**
 * 自定义错误码

 */
public enum ErrorCode {

    SUCCESS(0, "ok"),
    PARAMS_ERROR(40000, "请求参数错误"),
    NOT_LOGIN_ERROR(40100, "未登录"),
    NO_AUTH_ERROR(40101, "无权限"),
    TOO_MANY_REQUEST(40402,"请求过于频繁"),
    NOT_FOUND_ERROR(40400, "请求数据不存在"),
    FORBIDDEN_ERROR(40300, "禁止访问"),
    ACCOUNT_ERROR(50000, "邮箱账号格式异常"),
    EMAIL_ERROR(50001, "邮箱已经注册"),
    SYSTEM_ERROR(50002,"内部出错"),
    CODE_ERROR(50003,"验证码错误或过期"),
    PASSWPRD_ERROR(50004,"密码错误"),
    STATUS_ERROR(50005,"用户已被禁用"),
    SERVER_ERROR(50006,"添加管理员失败"),
    NO_MAIN_OPERATION(50007,"非法权限操作" ),
    FILM_FAIL(50008,"上传电影失败"),
    PICTURE_IS_NULL(50009,"图片为空"),
    UPLOAD_IS_FAIL(50010,"图片上传失败"),
    TIME_IS_FAIL(50011,"上映时间冲突"),
    FILM_NOT_EXIST(50012,"上映电影不存在" ),
    ORDER_IS_NULL(50013,"订单为空" ),
    THREAT_IS_NULL(50014,"当前场次无电影"),
    SET_IS_FULL(50015,"当前场次已满员" ),
    SET_IS_USED(50016,"当前座位已经售出" ),
    CANCEL_ORDER_FAIL(50017,"删除订单失败" ),
    ORDER_NOT_EXIST(50018,"订单不存在" );


    /**
     * 状态码
     */
    private final int code;

    /**
     * 信息
     */
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}

package com.wong.reservation.constant;

/**
 * @author Wongbuer
 * @createDate 2024/4/11
 */
public class SystemConstant {
    /**
     * 短信验证码redis存储前缀
     */
    public static final String SMS_LOGIN_PREFIX = "reservation:sms:login:";
    /**
     * 图片验证码redis存储前缀
     */
    public static final String CAPTCHA_PREFIX = "reservation:captcha";
    /**
     * 订单创建后超时前缀
     */
    public static final String ORDER_CREATED_PREFIX = "reservation:order:created:";
    /**
     * 订单接受后超时前缀
     */
    public static final String ORDER_ACCEPTED_PREFIX = "reservation:order:accepted:";
    /**
     * 订单评价超时前缀
     */
    public static final String ORDER_EVALUATE_PREFIX = "reservation:order:evaluate:";
    /**
     * 订单创建锁前缀
     */
    public static final String ORDER_LOCK_CREATE_PREFIX = "reservation:order:lock:create:";
    /**
     * 订单接受锁前缀
     */
    public static final String ORDER_LOCK_ACCEPT_PREFIX = "reservation:order:lock:accept:";
    /**
     * 订单支付锁前缀
     */
    public static final String ORDER_LOCK_PAY_PREFIX = "reservation:order:lock:pay:";
    /**
     * 订单评价锁前缀
     */
    public static final String ORDER_LOCK_EVALUATE_PREFIX = "reservation:order:lock:evaluate:";
    /**
     * 二维码密钥
     */
    public static final String QR_CODE_SHA256_SECRET = "wongbuer's reservation system wongbuer's reservation system wongbuer's reservation system wongbuer's reservation system";
}

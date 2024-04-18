package com.wong.reservation.constant;

/**
 * @author Wongbuer
 * @createDate 2024/4/17
 */
public class OrderStatusConstant {
    /**
     * 订单已创建
     */
    public static final int CREATED = 0;
    /**
     * 订单已接受
     */
    public static final int ACCEPTED = 1;
    /**
     * 创建后超时(规定时间内未被接受)-终止态
     */
    public static final int TIMEOUT_CREATED = 2;
    /**
     * 订单支付超时-终止态
     */
    public static final int TIMEOUT_PAYMENT = 3;
    /**
     * 订单已支付
     */
    public static final int PAID = 4;
    /**
     * 订单已退款-终止态
     */
    public static final int REFUNDED = 5;
    /**
     * 订单使用中
     */
    public static final int PENDING = 6;
    /**
     * 订单已使用(未评价)
     */
    public static final int NOT_EVALUATED = 7;
    /**
     * 订单已评价-终止态
     */
    public static final int EVALUATED = 8;
    /**
     * 订单超时未评价-终止态
     */
    public static final int TIMEOUT_EVALUATED = 9;

    public boolean isTerminal(int status) {
        return switch (status) {
            case TIMEOUT_CREATED, TIMEOUT_PAYMENT, REFUNDED, EVALUATED, TIMEOUT_EVALUATED -> true;
            default -> false;
        };
    }

    public int getNextStatus(int status) {
        return switch (status) {
            case CREATED -> ACCEPTED;
            case ACCEPTED -> PAID;
            case PAID -> PENDING;
            case PENDING -> NOT_EVALUATED;
            case NOT_EVALUATED -> EVALUATED;
            default -> status;
        };
    }
}

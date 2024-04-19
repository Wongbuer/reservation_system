package com.wong.reservation.constant;

import java.util.List;

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

    public static boolean isTerminal(int status) {
        return switch (status) {
            case TIMEOUT_CREATED, TIMEOUT_PAYMENT, REFUNDED, EVALUATED, TIMEOUT_EVALUATED -> true;
            default -> false;
        };
    }

    public static int getNextStatus(int status) {
        return switch (status) {
            case CREATED -> ACCEPTED;
            case ACCEPTED -> PAID;
            case PAID -> PENDING;
            case PENDING -> NOT_EVALUATED;
            case NOT_EVALUATED -> EVALUATED;
            default -> status;
        };
    }

    public static String getStatusName(int status) {
        return switch (status) {
            case CREATED -> "已创建";
            case ACCEPTED -> "已接受";
            case TIMEOUT_CREATED -> "已超时(未接受)";
            case TIMEOUT_PAYMENT -> "已超时(未付款)";
            case PAID -> "已支付";
            case REFUNDED -> "已退款";
            case PENDING -> "使用中";
            case NOT_EVALUATED -> "已使用(未评价)";
            case EVALUATED -> "已评价";
            case TIMEOUT_EVALUATED -> "已超时(未评价)";
            default -> throw new IllegalStateException("Unexpected value: " + status);
        };
    }

    public static List<Integer> getTerminalStatus() {
        return List.of(TIMEOUT_CREATED, TIMEOUT_PAYMENT, REFUNDED, EVALUATED, TIMEOUT_EVALUATED);
    }

    public static List<Integer> getNotYetPendingStatus() {
        return List.of(ACCEPTED, PAID);
    }
}

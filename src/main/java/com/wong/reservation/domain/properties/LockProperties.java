package com.wong.reservation.domain.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Wongbuer
 * @createDate 2024/4/19
 */
@Data
@Component
@ConfigurationProperties(prefix = "order.lock")
public class LockProperties {
    private String lockType = "local";
    private String lockRemoveType = "scheduled";
    private int lockRemoveSize = 20;
}

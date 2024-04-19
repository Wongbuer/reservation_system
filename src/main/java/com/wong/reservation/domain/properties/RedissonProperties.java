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
@ConfigurationProperties(prefix = "redisson")
public class RedissonProperties {
    private String serverMode;
    private String address;
    private String password;
    private int database;
}

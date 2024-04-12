package com.wong.reservation;


import com.wong.reservation.service.AddressService;
import com.wong.reservation.service.SocialUserAuthService;
import com.wong.reservation.service.SocialUserService;
import com.wong.reservation.service.UserService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ReservationSystemApplicationTests {

    @Resource
    private UserService userService;
    @Resource
    private SocialUserService socialUserService;
    @Resource
    private SocialUserAuthService socialUserAuthService;
    @Resource
    private AddressService addressService;

    @Test
    void contextLoads() {
        userService.list().forEach(System.out::println);
        socialUserService.list().forEach(System.out::println);
        socialUserAuthService.list().forEach(System.out::println);
        addressService.list().forEach(System.out::println);
    }

}

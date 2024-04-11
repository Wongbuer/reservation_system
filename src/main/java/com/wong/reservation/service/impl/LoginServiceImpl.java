package com.wong.reservation.service.impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.PhoneUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wong.reservation.domain.dto.LoginDTO;
import com.wong.reservation.domain.dto.Result;
import com.wong.reservation.domain.dto.SignUpDTO;
import com.wong.reservation.domain.entity.User;
import com.wong.reservation.service.CaptchaService;
import com.wong.reservation.service.LoginService;
import com.wong.reservation.service.UserService;
import com.wong.reservation.utils.RedisUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.request.AuthGithubRequest;
import me.zhyd.oauth.request.AuthRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

import static com.wong.reservation.constant.RedisConstant.SMS_LOGIN_PREFIX;

/**
 * @author Wongbuer
 */
@Service
public class LoginServiceImpl implements LoginService {
    private static final String DEFAULT_AVATAR_PREFIX = "https://api.multiavatar.com/";
    private static final String DEFAULT_AVATAR_SUFFIX = ".png";
    private static final String PASSWORD_SALT = "wongbuer";

    private static final Integer MAX_RETRY_TIMES = 3;
    private static final Map<Long, Object> RETRY_TIMES_MAP = new HashMap<>();

    @Resource
    private UserService userService;
    @Resource
    private CaptchaService captchaService;
    @Resource
    private RedisUtils redisUtils;

    @Override
    public Result<User> userSignUp(SignUpDTO signUpDTO) {
        // 判断所含参数为空
        if (!StringUtils.hasText(signUpDTO.getUsername()) || !StringUtils.hasText(signUpDTO.getPassword()) || !StringUtils.hasText(signUpDTO.getPhone())) {
            return Result.fail(400, "参数错误");
        }
        // 判断手机号是否正确
        if (!PhoneUtil.isPhone(signUpDTO.getPhone())) {
            return Result.fail(400, "手机号格式错误");
        }
        // 判断数据库中是否已有相同用户名或手机号
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        lqw
                .eq(User::getUsername, signUpDTO.getUsername())
                .or().eq(User::getPhone, signUpDTO.getPhone());
        if (userService.exists(lqw)) {
            return Result.fail(400, "用户名或手机号已存在");
        }
        User user = new User();
        user.setAvatar(DEFAULT_AVATAR_PREFIX + signUpDTO.getUsername() + DEFAULT_AVATAR_SUFFIX);
        user.setUsername(signUpDTO.getUsername());
        user.setPassword(SaSecureUtil.sha256(signUpDTO.getPassword() + PASSWORD_SALT));
        userService.save(user);
        return Result.success(user);
        // TODO: 跳转至对应的callback
    }

    @Override
    public Result<Object> userLogin(HttpServletRequest request, LoginDTO loginDTO) {
        // 判断参数是否正确
        if (!StringUtils.hasText(loginDTO.getAccount()) || !StringUtils.hasText(loginDTO.getPassword()) || !StringUtils.hasText(loginDTO.getCode())) {
            return Result.fail(400, "参数错误");
        }
        // 是否为验证码登录
        User user;
        if ("sms".equals(loginDTO.getType())) {
            // 手机验证码是否正确
            if (!PhoneUtil.isMobile(loginDTO.getAccount())) {
                return Result.fail(400, "手机号格式错误");
            }
            // 判断该用户是否存在
            user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getPhone, loginDTO.getAccount()));
            if (ObjectUtils.isEmpty(user)) {
                return Result.fail(400, "用户未注册");
            }
            // 判断手机验证码是否正确
            String code = (String) redisUtils.get(SMS_LOGIN_PREFIX + loginDTO.getAccount());
            if (!code.equals(loginDTO.getCode())) {
                return Result.fail(400, "手机验证码错误");
            }
            // 判断是否已登录
            if (StpUtil.isLogin()) {
                // 清除短信验证码
                redisUtils.del(SMS_LOGIN_PREFIX + loginDTO.getAccount());
                // 去除密码字段, 避免序列化
                user.setPassword(null);
                return Result.success("已登录", user);
            }
            // 用户登录
            StpUtil.login(user.getId());
            // 清除短信验证码
            redisUtils.del(SMS_LOGIN_PREFIX + loginDTO.getAccount());
            // 去除密码字段, 避免序列化
            user.setPassword(null);
            return Result.success("登录成功", user);
            // TODO: 写入Login日志
            // TODO: 跳转至对应的callback
        }
        // 判断图片验证码是否正确
        if (!captchaService.verifyCaptcha(request, loginDTO.getCode())) {
            return Result.fail(400, "验证码错误");
        }
        // 根据用户名获取用户
        user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, loginDTO.getAccount()));
        // 判断用户是否存在
        if (ObjectUtils.isEmpty(user)) {
            return Result.fail(400, "用户名或密码错误");
        }
        // 判断用户是否被封禁
        if (StpUtil.isDisable(user.getId())) {
            return Result.fail(400, "登录失败次数过多, 请稍后再试");
        }
        // 判断密码是否匹配
        if (!user.getPassword().equals(SaSecureUtil.sha256(loginDTO.getPassword() + PASSWORD_SALT))) {
            // 登录次数是否达到最大测试数
            Integer tryTimes = (Integer) RETRY_TIMES_MAP.get(user.getId());
            if (tryTimes == null) {
                RETRY_TIMES_MAP.put(user.getId(), 1);
                tryTimes = 1;
            } else {
                RETRY_TIMES_MAP.put(user.getId(), ++tryTimes);
            }
            // 判断是否大于重试次数
            if (tryTimes > MAX_RETRY_TIMES) {
                RETRY_TIMES_MAP.remove(user.getId());
                // 封禁5分钟
                StpUtil.disable(user.getId(), 60 * 5);
                return Result.fail(400, "登录失败次数过多, 请稍后再试");
            }
            // 返回错误信息
            return Result.fail(400, "用户名或密码错误");
        }

        // 判断用户是否已经登录
        if (StpUtil.isLogin(user.getId())) {
            // 去除密码字段, 避免序列化
            user.setPassword(null);
            return Result.success("已登录", user);
        }
        // 用户登录
        StpUtil.login(user.getId());
        // TODO: 写入Login日志
        // 去除密码字段, 避免序列化
        user.setPassword(null);
        return Result.success("登录成功", user);
        // TODO: 跳转至对应的callback
    }

    @Override
    public AuthRequest getAuthRequest(String source) {
        return switch (source) {
            case "github" -> new AuthGithubRequest(AuthConfig.builder()
                    .clientId("c8f009eb3415ce6f3797")
                    .clientSecret("7d9556e587c45686550e1f6cb7c34c3ded79a8ac")
                    .redirectUri("http://localhost:8080/oauth/callback/github")
                    .build());
            case "gitee" -> new AuthGithubRequest(AuthConfig.builder()
                    .clientId("c8009eb3415ce6f3797")
                    .clientSecret("7d9556e587c45686550e1f6cb7c34c3ded79a8ac")
                    .redirectUri("http://localhost:8080/oauth/callback/github")
                    .build());
            default -> throw new IllegalStateException("Unexpected value: " + source);
        };
    }
}

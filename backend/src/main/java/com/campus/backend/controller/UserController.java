package com.campus.backend.controller;

import com.campus.backend.common.Result;
import com.campus.backend.common.annotation.ApiVersion;
import com.campus.backend.config.JwtUtil;
import com.campus.backend.dto.UserLoginDTO;
import com.campus.backend.dto.UserRegisterDTO;
import com.campus.backend.dto.UserVO;
import com.campus.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@Tag(name = "用户管理", description = "用户注册、登录、信息管理相关接口")
@ApiVersion(value = "v1", supportedVersions = {"v1", "v2"}, description = "用户管理API")
@Slf4j
public class UserController {

    @Autowired(required = false)
    private UserService userService;

    @Autowired(required = false)
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "新用户注册接口")
    public Result<Map<String, Object>> register(@Valid @RequestBody UserRegisterDTO registerDTO) {
        checkServices();
        UserVO userVO = userService.register(registerDTO);

        // 生成JWT令牌
        String token = jwtUtil.generateToken(userVO.getId(), userVO.getUsername());

        Map<String, Object> result = new HashMap<>();
        result.put("user", userVO);
        result.put("token", token);

        return Result.success(result);
    }

    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户登录接口")
    public Result<Map<String, Object>> login(@Valid @RequestBody UserLoginDTO loginDTO) {
        checkServices();
        UserVO userVO = userService.login(loginDTO.getUsername(), loginDTO.getPassword());

        // 生成JWT令牌
        String token = jwtUtil.generateToken(userVO.getId(), userVO.getUsername());

        Map<String, Object> result = new HashMap<>();
        result.put("user", userVO);
        result.put("token", token);

        return Result.success(result);
    }

    @GetMapping("/info")
    @Operation(summary = "获取用户信息", description = "获取当前登录用户信息")
    public Result<UserVO> getUserInfo() {
        checkServices();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // 通过用户名获取用户信息
        UserVO userVO = userService.getUserInfoByUsername(username);
        return Result.success(userVO);
    }

    private void checkServices() {
        if (userService == null || jwtUtil == null) {
            log.error("UserService or JwtUtil is null! Database connection may have failed.");
            throw new RuntimeException("Service unavailable - check database connection");
        }
    }
}

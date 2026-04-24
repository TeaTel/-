package com.campus.backend.controller;

import com.campus.backend.common.Result;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserApi {

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> body) {
        Map<String, Object> result = new HashMap<>();
        result.put("token", "test-token-123");
        result.put("user", body.get("username"));
        return Result.success("login", result);
    }

    @PostMapping("/register")
    public Result<Map<String, Object>> register(@RequestBody Map<String, String> body) {
        Map<String, Object> result = new HashMap<>();
        result.put("token", "test-token-456");
        result.put("user", body.get("username"));
        return Result.success("registered", result);
    }
}
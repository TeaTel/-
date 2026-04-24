package com.campus.backend.controller;

import com.campus.backend.common.Result;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/categories")
public class CategoryApi {

    @GetMapping("/tree")
    public Result<String> getCategoryTree() {
        return Result.success("categories", "tree-works");
    }
}
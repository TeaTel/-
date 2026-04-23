package com.campus.backend.controller;

import com.campus.backend.common.Result;
import com.campus.backend.dto.CategoryCreateDTO;
import com.campus.backend.dto.CategoryUpdateDTO;
import com.campus.backend.dto.CategoryVO;
import com.campus.backend.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "分类管理", description = "商品分类管理接口")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @Operation(summary = "创建分类", description = "创建新的商品分类")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<CategoryVO> createCategory(@Valid @RequestBody CategoryCreateDTO createDTO) {
        return categoryService.createCategory(createDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新分类", description = "更新指定ID的分类信息")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<CategoryVO> updateCategory(
            @Parameter(description = "分类ID") @PathVariable Long id,
            @Valid @RequestBody CategoryUpdateDTO updateDTO) {
        return categoryService.updateCategory(id, updateDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除分类", description = "删除指定ID的分类")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> deleteCategory(
            @Parameter(description = "分类ID") @PathVariable Long id) {
        return categoryService.deleteCategory(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取分类详情", description = "根据ID获取分类详情")
    public Result<CategoryVO> getCategoryById(
            @Parameter(description = "分类ID") @PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    @GetMapping
    @Operation(summary = "获取所有分类", description = "获取所有分类列表")
    public Result<List<CategoryVO>> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/roots")
    @Operation(summary = "获取根分类", description = "获取所有根分类（无父分类的分类）")
    public Result<List<CategoryVO>> getRootCategories() {
        return categoryService.getRootCategories();
    }

    @GetMapping("/parent/{parentId}")
    @Operation(summary = "获取子分类", description = "根据父分类ID获取子分类列表")
    public Result<List<CategoryVO>> getCategoriesByParentId(
            @Parameter(description = "父分类ID") @PathVariable Long parentId) {
        return categoryService.getCategoriesByParentId(parentId);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "根据状态获取分类", description = "根据状态获取分类列表")
    public Result<List<CategoryVO>> getCategoriesByStatus(
            @Parameter(description = "状态：0-禁用，1-启用") @PathVariable Integer status) {
        return categoryService.getCategoriesByStatus(status);
    }

    @GetMapping("/tree")
    @Operation(summary = "获取分类树", description = "获取树形结构的分类列表")
    public Result<List<CategoryVO>> getCategoryTree() {
        return categoryService.getCategoryTree();
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "更新分类状态", description = "更新指定分类的状态")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> updateCategoryStatus(
            @Parameter(description = "分类ID") @PathVariable Long id,
            @Parameter(description = "状态：0-禁用，1-启用") @RequestParam Integer status) {
        return categoryService.updateCategoryStatus(id, status);
    }
}
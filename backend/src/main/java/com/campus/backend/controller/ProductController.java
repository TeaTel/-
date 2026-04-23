package com.campus.backend.controller;

import com.campus.backend.common.Result;
import com.campus.backend.dto.ProductCreateDTO;
import com.campus.backend.dto.ProductQueryDTO;
import com.campus.backend.dto.ProductUpdateDTO;
import com.campus.backend.dto.ProductVO;
import com.campus.backend.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "商品管理", description = "商品相关接口")
@Slf4j
public class ProductController {

    @Autowired(required = false)
    private ProductService productService;

    @GetMapping
    @Operation(summary = "获取商品列表", description = "根据条件查询商品列表，支持分类筛选和关键词搜索")
    public Result<List<ProductVO>> getProductList(
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        checkService();
        ProductQueryDTO query = new ProductQueryDTO();
        query.setCategoryId(categoryId);
        query.setKeyword(keyword);
        query.setPage(page);
        query.setSize(size);

        List<ProductVO> productList = productService.getProductList(query);
        return Result.success(productList);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取商品详情", description = "根据商品ID获取商品详细信息")
    public Result<ProductVO> getProductDetail(@PathVariable Long id) {
        checkService();
        ProductVO productVO = productService.getProductDetail(id);
        return Result.success(productVO);
    }

    @PostMapping
    @Operation(summary = "发布商品", description = "发布新商品")
    public Result<ProductVO> createProduct(@Valid @RequestBody ProductCreateDTO createDTO) {
        checkService();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // TODO: 根据用户名获取用户ID
        // 暂时使用测试用户ID
        Long sellerId = 1L;

        ProductVO productVO = productService.createProduct(createDTO, sellerId);
        return Result.success(productVO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新商品", description = "更新商品信息")
    public Result<ProductVO> updateProduct(@PathVariable Long id,
                                          @Valid @RequestBody ProductUpdateDTO updateDTO) {
        checkService();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // TODO: 根据用户名获取用户ID
        // 暂时使用测试用户ID
        Long sellerId = 1L;

        ProductVO productVO = productService.updateProduct(id, updateDTO, sellerId);
        return Result.success(productVO);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除商品", description = "删除商品（软删除）")
    public Result<Void> deleteProduct(@PathVariable Long id) {
        checkService();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // TODO: 根据用户名获取用户ID
        // 暂时使用测试用户ID
        Long sellerId = 1L;

        productService.deleteProduct(id, sellerId);
        return Result.success();
    }

    @GetMapping("/my")
    @Operation(summary = "获取我的商品", description = "获取当前用户发布的商品列表")
    public Result<List<ProductVO>> getMyProducts() {
        checkService();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // TODO: 根据用户名获取用户ID
        // 暂时使用测试用户ID
        Long sellerId = 1L;

        List<ProductVO> productList = productService.getMyProducts(sellerId);
        return Result.success(productList);
    }

    private void checkService() {
        if (productService == null) {
            log.error("ProductService is null! Database connection may have failed.");
            throw new RuntimeException("Service unavailable - check database connection");
        }
    }
}

package com.campus.backend.controller;

import com.campus.backend.common.Result;
import com.campus.backend.dto.OrderCreateDTO;
import com.campus.backend.dto.OrderVO;
import com.campus.backend.service.OrderService;
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
@RequestMapping("/api/orders")
@Tag(name = "订单管理", description = "订单相关接口")
@Slf4j
public class OrderController {

    @Autowired(required = false)
    private OrderService orderService;

    @PostMapping
    @Operation(summary = "创建订单", description = "创建新订单")
    public Result<OrderVO> createOrder(@Valid @RequestBody OrderCreateDTO createDTO) {
        checkService();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // TODO: 根据用户名获取用户ID
        // 暂时使用测试用户ID
        Integer buyerId = 1;

        OrderVO orderVO = orderService.createOrder(createDTO, buyerId.longValue());
        return Result.success(orderVO);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取订单详情", description = "根据订单ID获取订单详细信息")
    public Result<OrderVO> getOrderDetail(@PathVariable Integer id) {
        checkService();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // TODO: 根据用户名获取用户ID
        // 暂时使用测试用户ID
        Integer userId = 1;

        OrderVO orderVO = orderService.getOrderDetail(id.longValue(), userId.longValue());
        return Result.success(orderVO);
    }

    @GetMapping("/buyer")
    @Operation(summary = "获取买家订单", description = "获取当前用户的购买订单列表")
    public Result<List<OrderVO>> getBuyerOrders() {
        checkService();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // TODO: 根据用户名获取用户ID
        // 暂时使用测试用户ID
        Integer buyerId = 1;

        List<OrderVO> orders = orderService.getBuyerOrders(buyerId.longValue());
        return Result.success(orders);
    }

    @GetMapping("/seller")
    @Operation(summary = "获取卖家订单", description = "获取当前用户的销售订单列表")
    public Result<List<OrderVO>> getSellerOrders() {
        checkService();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // TODO: 根据用户名获取用户ID
        // 暂时使用测试用户ID
        Integer sellerId = 1;

        List<OrderVO> orders = orderService.getSellerOrders(sellerId.longValue());
        return Result.success(orders);
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "更新订单状态", description = "卖家更新订单状态")
    public Result<Void> updateOrderStatus(@PathVariable Integer id, @RequestParam String status) {
        checkService();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // TODO: 根据用户名获取用户ID
        // 暂时使用测试用户ID
        Integer userId = 1;

        orderService.updateOrderStatus(id.longValue(), status, userId.longValue());
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "取消订单", description = "买家取消订单")
    public Result<Void> cancelOrder(@PathVariable Integer id) {
        checkService();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // TODO: 根据用户名获取用户ID
        // 暂时使用测试用户ID
        Integer userId = 1;

        orderService.cancelOrder(id.longValue(), userId.longValue());
        return Result.success();
    }

    @PutMapping("/{id}/confirm")
    @Operation(summary = "确认收货", description = "买家确认收货")
    public Result<Void> confirmOrder(@PathVariable Integer id) {
        checkService();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // TODO: 根据用户名获取用户ID
        // 暂时使用测试用户ID
        Integer userId = 1;

        orderService.confirmOrder(id.longValue(), userId.longValue());
        return Result.success();
    }

    private void checkService() {
        if (orderService == null) {
            log.error("OrderService is null! Database connection may have failed.");
            throw new RuntimeException("Service unavailable - check database connection");
        }
    }
}

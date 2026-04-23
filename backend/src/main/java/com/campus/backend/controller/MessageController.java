package com.campus.backend.controller;

import com.campus.backend.common.Result;
import com.campus.backend.dto.MessageSendDTO;
import com.campus.backend.dto.MessageVO;
import com.campus.backend.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
@RequestMapping("/api/messages")
@Tag(name = "消息管理", description = "消息聊天相关接口")
@Slf4j
public class MessageController {

    @Autowired(required = false)
    private MessageService messageService;

    @PostMapping
    @Operation(summary = "发送消息", description = "发送消息给其他用户")
    public Result<MessageVO> sendMessage(@Valid @RequestBody MessageSendDTO sendDTO) {
        checkService();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // TODO: 根据用户名获取用户ID
        // 暂时使用测试用户ID
        Integer senderId = 1;

        MessageVO messageVO = messageService.sendMessage(sendDTO, senderId.longValue());
        return Result.success(messageVO);
    }

    @GetMapping("/conversation/{otherUserId}")
    @Operation(summary = "获取对话", description = "获取与指定用户的对话记录")
    public Result<List<MessageVO>> getConversation(@PathVariable Integer otherUserId) {
        checkService();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // TODO: 根据用户名获取用户ID
        // 暂时使用测试用户ID
        Integer userId = 1;

        List<MessageVO> messages = messageService.getConversation(userId.longValue(), otherUserId.longValue());
        return Result.success(messages);
    }

    @GetMapping("/contacts")
    @Operation(summary = "获取联系人", description = "获取所有联系人的最近消息")
    public Result<List<MessageVO>> getContacts() {
        checkService();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // TODO: 根据用户名获取用户ID
        // 暂时使用测试用户ID
        Integer userId = 1;

        List<MessageVO> contacts = messageService.getContacts(userId.longValue());
        return Result.success(contacts);
    }

    @GetMapping("/unread")
    @Operation(summary = "获取未读消息", description = "获取所有未读消息")
    public Result<List<MessageVO>> getUnreadMessages() {
        checkService();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // TODO: 根据用户名获取用户ID
        // 暂时使用测试用户ID
        Integer userId = 1;

        List<MessageVO> messages = messageService.getUnreadMessages(userId.longValue());
        return Result.success(messages);
    }

    @PutMapping("/{id}/read")
    @Operation(summary = "标记消息为已读", description = "标记单条消息为已读")
    public Result<Void> markAsRead(@PathVariable Integer id) {
        checkService();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // TODO: 根据用户名获取用户ID
        // 暂时使用测试用户ID
        Integer userId = 1;

        messageService.markAsRead(id.longValue(), userId.longValue());
        return Result.success();
    }

    @PutMapping("/conversation/{senderId}/read")
    @Operation(summary = "标记对话为已读", description = "标记与指定用户的所有消息为已读")
    public Result<Void> markConversationAsRead(@PathVariable Integer senderId) {
        checkService();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // TODO: 根据用户名获取用户ID
        // 暂时使用测试用户ID
        Integer userId = 1;

        messageService.markConversationAsRead(userId.longValue(), senderId.longValue());
        return Result.success();
    }

    @GetMapping("/unread/count")
    @Operation(summary = "获取未读消息数量", description = "获取未读消息总数")
    public Result<Integer> getUnreadCount() {
        checkService();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // TODO: 根据用户名获取用户ID
        // 暂时使用测试用户ID
        Integer userId = 1;

        int count = messageService.getUnreadCount(userId.longValue());
        return Result.success(count);
    }

    private void checkService() {
        if (messageService == null) {
            log.error("MessageService is null! Database connection may have failed.");
            throw new RuntimeException("Service unavailable - check database connection");
        }
    }
}

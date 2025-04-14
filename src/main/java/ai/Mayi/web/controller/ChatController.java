package ai.Mayi.web.controller;


import ai.Mayi.apiPayload.ApiResponse;
import ai.Mayi.converter.ChatConverter;
import ai.Mayi.domain.Chat;
import ai.Mayi.domain.User;
import ai.Mayi.jwt.CookieUtil;
import ai.Mayi.service.ChatService;
import ai.Mayi.service.UserServiceImpl;
import ai.Mayi.web.dto.ChatDTO;
import ai.Mayi.web.dto.CommonDTO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")

public class ChatController {

    private final ChatService chatService;
    private final ChatConverter chatConverter;
    private final UserServiceImpl userService;

    @PostMapping("")
    @Operation(summary = "채팅방 생성 API")
    public ApiResponse<ChatDTO.ChatResponseDTO> createChatRoom(
            HttpServletRequest http, @RequestBody ChatDTO.ChatRequestDTO chatRequestDTO) {
        String accessToken = CookieUtil.getCookieValue(http, "accessToken");
        User user = userService.findByAccessToken(accessToken);

        Chat chat = chatService.createChat(chatRequestDTO, user);
        return ApiResponse.onSuccess(chatConverter.toChatDTO(chat));
    }

    @GetMapping("")
    @Operation(summary = "채팅방 조회 API")
    public ApiResponse<List<ChatDTO.ChatListResponseDTO>> getChatRoom(
            HttpServletRequest http) {
        String accessToken = CookieUtil.getCookieValue(http, "accessToken");
        User user = userService.findByAccessToken(accessToken);

        return ApiResponse.onSuccess(chatService.getChatList(user));
    }

    @PostMapping("/{chatId}")
    @Operation(summary = "채팅방 삭제 API")
    public ApiResponse<CommonDTO.IsSuccessDTO> deleteChatRoom(
            HttpServletRequest http, @PathVariable Long chatId) {
        String accessToken = CookieUtil.getCookieValue(http, "accessToken");
        User user = userService.findByAccessToken(accessToken);

        return ApiResponse.onSuccess(chatService.deleteChat(user,chatId));
    }

    @PostMapping("/test")
    @Operation(summary = "test API")
    public boolean deleteChatRoom() {
        return true;
    }
}

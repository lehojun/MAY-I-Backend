package ai.Mayi.web.controller;


import ai.Mayi.apiPayload.ApiResponse;
import ai.Mayi.converter.ChatConverter;
import ai.Mayi.domain.Chat;
import ai.Mayi.service.ChatService;
import ai.Mayi.web.dto.ChatDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")

public class ChatController {

    private final ChatService chatService;
    private final ChatConverter chatConverter;

    @PostMapping("")
    @Operation(summary = "채팅방 생성 API")
    public ApiResponse<ChatDTO.ChatResponseDTO> createChatRoom(
            @RequestBody ChatDTO.ChatRequestDTO chatRequestDTO) {

        Chat chat = chatService.createChat(chatRequestDTO);
        return ApiResponse.onSuccess(chatConverter.toChatDTO(chat));
    }

    @GetMapping("{id}")
    @Operation(summary = "채팅방 조회 API")
    public ApiResponse<List<ChatDTO.ChatListResponseDTO>> getChatRoom(
            @PathVariable(name="id") Long userId) {

        return ApiResponse.onSuccess(chatService.getChatList(userId));
    }
}

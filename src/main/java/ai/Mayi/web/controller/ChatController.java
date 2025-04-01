package ai.Mayi.web.controller;


import ai.Mayi.apiPayload.ApiResponse;
import ai.Mayi.converter.ChatConverter;
import ai.Mayi.domain.Chat;
import ai.Mayi.service.ChatService;
import ai.Mayi.web.dto.ChatDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;
    private final ChatConverter chatConverter;

    @PostMapping("/room")
    public ApiResponse<ChatDTO.ChatResponseDTO> createChatRoom(
            @RequestBody ChatDTO.ChatRequestDTO chatRequestDTO) {

        Chat chat = chatService.createChat(chatRequestDTO);
        return ApiResponse.onSuccess(chatConverter.toChatDTO(chat));
    }

}

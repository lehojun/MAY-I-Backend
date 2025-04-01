package ai.Mayi.converter;

import ai.Mayi.domain.Chat;
import ai.Mayi.domain.User;
import ai.Mayi.web.dto.ChatDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ChatConverter {

    public Chat toChat(ChatDTO.ChatRequestDTO request, User user){
        return Chat.builder()
                .chatName(request.getChatName())
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();
    }
    public ChatDTO.ChatResponseDTO toChatDTO(Chat chat){
        return ChatDTO.ChatResponseDTO.builder()
                .chatId(chat.getChatId())
                .build();
    }
    public ChatDTO.ChatListResponseDTO toChatListDTO(Chat chat) {
        return ChatDTO.ChatListResponseDTO.builder()
                .chatId(chat.getChatId())
                .chatName(chat.getChatName())
                .build();
    }
}

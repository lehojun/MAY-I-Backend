package ai.Mayi.service;

import ai.Mayi.domain.Chat;
import ai.Mayi.domain.Message;
import ai.Mayi.domain.enums.MessageType;
import ai.Mayi.repository.ChatRepository;
import ai.Mayi.repository.MessageRepository;
import ai.Mayi.web.dto.MessageDTO;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    public Message enterChat(Chat chat, String text){
        Message message = Message.builder()
                .chat(chat)
                .messageType(MessageType.USER)
                .text(text)
                .build();

        return messageRepository.save(message);
    }

    @Async
    public CompletableFuture<MessageDTO.ChatResDTO> GPTService(@NotNull List<MessageType> aiTypeList, Message userMessage){
        if(!aiTypeList.contains(MessageType.GPT)) {
            return null;
        }

        return CompletableFuture.completedFuture(MessageDTO.ChatResDTO.builder()
                .text("")
                .messageType(MessageType.GPT)
                .build());
    }

    @Async
    public CompletableFuture<MessageDTO.ChatResDTO> CopliotService(@NotNull List<MessageType> aiTypeList, Message userMessage){
        if(!aiTypeList.contains(MessageType.COPLIOT)) {
            return null;
        }

        return CompletableFuture.completedFuture(MessageDTO.ChatResDTO.builder()
                .text("")
                .messageType(MessageType.COPLIOT)
                .build());
    }

    @Async
    public CompletableFuture<MessageDTO.ChatResDTO> BardService(@NotNull List<MessageType> aiTypeList, Message userMessage){
        if(!aiTypeList.contains(MessageType.BARD)) {
            return null;
        }

        return CompletableFuture.completedFuture(MessageDTO.ChatResDTO.builder()
                .text("")
                .messageType(MessageType.BARD)
                .build());
    }

    @Async
    public CompletableFuture<MessageDTO.ChatResDTO> ClaudeService(@NotNull List<MessageType> aiTypeList, Message userMessage){
        if(!aiTypeList.contains(MessageType.CLAUDE)) {
            return null;
        }

        return CompletableFuture.completedFuture(MessageDTO.ChatResDTO.builder()
                .text("")
                .messageType(MessageType.CLAUDE)
                .build());
    }
}

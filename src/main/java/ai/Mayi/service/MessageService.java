package ai.Mayi.service;

import ai.Mayi.apiPayload.code.status.ErrorStatus;
import ai.Mayi.apiPayload.exception.handler.MessageHandler;
import ai.Mayi.apiPayload.exception.handler.TokenHandler;
import ai.Mayi.domain.Chat;
import ai.Mayi.domain.Message;
import ai.Mayi.domain.User;
import ai.Mayi.domain.enums.MessageType;
import ai.Mayi.repository.ChatRepository;
import ai.Mayi.repository.MessageRepository;
import ai.Mayi.repository.UserRepository;
import ai.Mayi.web.dto.MessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    public void enterChat(Chat chat, String text){
        Message message = Message.builder()
                .chat(chat)
                .messageType(MessageType.USER)
                .text(text)
                .build();

        messageRepository.save(message);
    }

    public Message GPTService(Message message){

        return message;
    }

    public Message CopliotService(Message message){

        return message;
    }

    public Message BardService(Message message){

        return message;
    }

    public Message ClaudeService(Message message){

        return message;
    }
}

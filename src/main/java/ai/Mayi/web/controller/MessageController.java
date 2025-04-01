package ai.Mayi.web.controller;

import ai.Mayi.apiPayload.code.status.ErrorStatus;
import ai.Mayi.apiPayload.exception.handler.MessageHandler;
import ai.Mayi.domain.Chat;
import ai.Mayi.domain.Message;
import ai.Mayi.domain.User;
import ai.Mayi.service.ChatService;
import ai.Mayi.service.MessageService;
import ai.Mayi.service.UserServiceImpl;
import ai.Mayi.web.dto.MessageDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "MessageController", description = "채팅 메세지 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/message")
public class MessageController {
    private final MessageService messageService;
    private final UserServiceImpl userService;
    private final ChatService chatService;

    @PostMapping("")
    @Operation(summary = "채팅 입력 API")
    public ResponseEntity<MessageDTO.enterChatResDTO> enterChat(@RequestBody @Valid MessageDTO.enterChatReqDTO request) {
        User user = userService.findUserById(request.getUserId());
        Chat chat = chatService.findChatById(request.getChatId());

        if(chat.getUser() != user){
            throw new MessageHandler(ErrorStatus._NOT_MATCH_CHAT);
        }

        messageService.enterChat(chat, request.getText());

        List<MessageDTO.ChatResDTO> responseDTOList = request.getAiTypeList().stream()
                .map(messageType -> {
                    Message message = Message.builder()
                            .chat(chat)
                            .build();
                    switch (messageType){
                        case GPT -> {
                            //gpt service
                            message.setMessageType(messageType);
                            message = messageService.GPTService(message);
                        }
                        case CLAUDE -> {
                            //CLAUDE service
                            message.setMessageType(messageType);
                            message = messageService.ClaudeService(message);
                        }
                        case BARD -> {
                            //BARD service
                            message.setMessageType(messageType);
                            message = messageService.BardService(message);
                        }
                        case COPLIOT -> {
                            //COPLIOT service
                            message.setMessageType(messageType);
                            message = messageService.CopliotService(message);
                        }
                        default -> throw new MessageHandler(ErrorStatus._NOT_EXIST_TOKEN_TYPE);
                    }
                    return MessageDTO.ChatResDTO.builder()
                            .messageType(message.getMessageType())
                            .text(message.getText())
                            .build();
                }).toList();

        return ResponseEntity.ok(MessageDTO.enterChatResDTO.builder()
                        .chatId(chat.getChatId())
                        .responseDTOList(responseDTOList)
                .build());
    }
}

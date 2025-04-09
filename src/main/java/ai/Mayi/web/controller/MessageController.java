package ai.Mayi.web.controller;

import ai.Mayi.apiPayload.ApiResponse;
import ai.Mayi.apiPayload.code.status.ErrorStatus;
import ai.Mayi.apiPayload.exception.handler.MessageHandler;
import ai.Mayi.domain.Chat;
import ai.Mayi.domain.Message;
import ai.Mayi.domain.User;
import ai.Mayi.domain.enums.MessageType;
import ai.Mayi.service.ChatService;
import ai.Mayi.service.MessageService;
import ai.Mayi.service.UserServiceImpl;
import ai.Mayi.web.dto.MessageDTO;
import ai.Mayi.web.dto.TokenDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Tag(name = "MessageController", description = "채팅 메세지 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/message")
public class MessageController {
    private final MessageService messageService;
    private final UserServiceImpl userService;
    private final ChatService chatService;

    @PostMapping("/")
    @Operation(summary = "채팅 입력 API")
    public ApiResponse<MessageDTO.enterChatResDTO> enterChat(@RequestBody @Valid MessageDTO.enterChatReqDTO request) throws InterruptedException, ExecutionException {
        User user = userService.findUserById(request.getUserId());
        Chat chat = chatService.findChatById(request.getChatId());

        if(chat.getUser() != user){
            throw new MessageHandler(ErrorStatus._NOT_MATCH_CHAT);
        }
        if(request.getAiTypeList().contains(MessageType.USER)){
            throw new MessageHandler(ErrorStatus._INVALID_AI_TYPE);
        }

        Message userMessage = messageService.enterChat(chat, request.getText());

        List<CompletableFuture<MessageDTO.ChatResDTO>> futures = new ArrayList<>();
        if (request.getAiTypeList().contains(MessageType.GPT)) {
            futures.add(messageService.GPTService(userMessage));
        }
        if (request.getAiTypeList().contains(MessageType.DEEPSEEK)) {
            futures.add(messageService.DeepSeekService(userMessage));
        }
        if (request.getAiTypeList().contains(MessageType.CLAUDE)) {
            futures.add(messageService.ClaudeService(userMessage));
        }
        if (request.getAiTypeList().contains(MessageType.BARD)) {
            futures.add(messageService.BardService(userMessage));
        }

        List<MessageDTO.ChatResDTO> responseDTOList = new ArrayList<>();
        for (CompletableFuture<MessageDTO.ChatResDTO> future : futures) {
            responseDTOList.add(future.get());
        }

        responseDTOList.remove(null);

        return ApiResponse.onSuccess(MessageDTO.enterChatResDTO.builder()
                .chatId(chat.getChatId())
                .responseDTOList(responseDTOList)
                .build());
    }

    @GetMapping("/{chatId}")
    @Operation(summary = "메세지 조회 API")
    public ApiResponse<MessageDTO.getChatResDTO> getMessageList(@RequestParam @Valid Long userId, @PathVariable Long chatId) {
        User user = userService.findUserById(userId);
        Chat chat = chatService.findChatById(chatId);

        return ApiResponse.onSuccess(messageService.getMessageList(user, chat));
    }
}

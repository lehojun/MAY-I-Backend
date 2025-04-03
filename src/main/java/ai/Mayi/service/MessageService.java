package ai.Mayi.service;

import ai.Mayi.apiPayload.code.status.ErrorStatus;
import ai.Mayi.apiPayload.exception.handler.MessageHandler;
import ai.Mayi.apiPayload.exception.handler.TokenHandler;
import ai.Mayi.domain.Chat;
import ai.Mayi.domain.Message;
import ai.Mayi.domain.Token;
import ai.Mayi.domain.enums.MessageType;
import ai.Mayi.repository.ChatRepository;
import ai.Mayi.repository.MessageRepository;
import ai.Mayi.repository.TokenRepository;
import ai.Mayi.web.dto.MessageDTO;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final TokenRepository tokenRepository;
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
        Token bardToken = tokenRepository.findByUser(userMessage.getChat().getUser()).stream()
                .filter(token -> token.getTokenType().toString().equals(MessageType.BARD.toString()))
                .findFirst()
                .orElseThrow(() -> new MessageHandler(ErrorStatus._NOT_EXIST_TOKEN));
        //webClient init
        WebClient webClient = WebClient.builder().build();
        String key = bardToken.getTokenValue();
        String uri = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + key;

        //req body init
        List<MessageDTO.BardContents.Parts> partsList = new ArrayList<>();
        partsList.add(MessageDTO.BardContents.Parts.builder().text(userMessage.getText()).build());

        List<MessageDTO.BardContents> contentsList = new ArrayList<>();
        contentsList.add(MessageDTO.BardContents.builder().parts(partsList).build());

        MessageDTO.BardChatReqDTO reqBody = MessageDTO.BardChatReqDTO.builder()
                .contents(contentsList)
                .build();

        //url call
        MessageDTO.BardChatResDTO resBody = webClient.post()
                .uri(uri)
                .body(Mono.just(reqBody), MessageDTO.BardChatReqDTO.class)
                .retrieve()
                .bodyToMono(MessageDTO.BardChatResDTO.class)
                .block();

        if (resBody != null) {
            String text = resBody.getCandidates().get(0).getContent().getParts().get(0).getText();

            Message message = Message.builder()
                    .chat(userMessage.getChat())
                    .messageType(MessageType.BARD)
                    .text(text)
                    .build();
            messageRepository.save(message);

            return CompletableFuture.completedFuture(MessageDTO.ChatResDTO.builder()
                    .text(text)
                    .messageType(MessageType.BARD)
                    .build());
        }
        else {
            throw new MessageHandler(ErrorStatus._BARD_RESPONSE_NULL);
        }
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

package ai.Mayi.service;

import ai.Mayi.apiPayload.code.status.ErrorStatus;
import ai.Mayi.apiPayload.exception.handler.TokenHandler;
import ai.Mayi.apiPayload.exception.handler.MessageHandler;
import ai.Mayi.converter.MessageConverter;
import ai.Mayi.domain.*;
import ai.Mayi.domain.enums.MessageType;
import ai.Mayi.domain.enums.TokenType;
import ai.Mayi.repository.ChatRepository;
import ai.Mayi.repository.MessageRepository;
import ai.Mayi.repository.ModelMessageRepository;
import ai.Mayi.repository.TokenRepository;
import ai.Mayi.web.dto.MessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final TokenRepository tokenRepository;
    private final MessageConverter messageConverter;
    private final ModelMessageRepository modelMessageRepository;

    @Value("${ai.model.gpt}") private String gptModel;
    @Value("${ai.api.url.gpt}") private String gptUrl;
    @Value("${ai.model.claude}") private String claudeModel;
    @Value("${ai.api.url.claude}") private String claudeUrl;

    public Message enterChat(Chat chat, String text){
        Message message = Message.builder()
                .chat(chat)
                .messageAt(LocalDateTime.now())
                .text(text)
                .build();

        return messageRepository.save(message);
    }

    @Async
    public CompletableFuture<MessageDTO.ChatResDTO> GPTService(Message userMessage) {

        User user = userMessage.getChat().getUser();
        Token token = tokenRepository.findByUserAndTokenType(user, TokenType.GPT);

        if (token == null) throw new TokenHandler(ErrorStatus._BAD_REQUEST);

        MessageDTO.gptReqDTO chatRequest = new MessageDTO.gptReqDTO(gptModel,userMessage.getText());

        //webClient init
        WebClient webClient = WebClient.builder()
                .baseUrl(gptUrl)
                .defaultHeader("Authorization", "Bearer " + token.getTokenValue())
                .build();


        //Url call
        MessageDTO.gptResDTO response;
        try {
             response = webClient.post()
                    .uri(gptUrl)
                    .bodyValue(chatRequest)
                    .retrieve()
                    .bodyToMono(MessageDTO.gptResDTO.class)
                    .block();
        } catch (WebClientResponseException.BadRequest e) {
            throw new MessageHandler(ErrorStatus._GPT_CONNECT_FAIL);
        }

        //save response
        if (response != null) {
            String text = response.getChoices().get(0).getMessage().getContent();

            ModelMessage message = ModelMessage.builder()
                    .message(userMessage)
                    .messageType(MessageType.GPT)
                    .messageAt(LocalDateTime.now())
                    .text(text)
                    .build();
            modelMessageRepository.save(message);

            return CompletableFuture.completedFuture(MessageDTO.ChatResDTO.builder()
                    .text(text)
                    .messageType(MessageType.GPT)
                    .build());
        } else {
            throw new MessageHandler(ErrorStatus._GPT_CONNECT_FAIL);
        }
    }

    @Async
    public CompletableFuture<MessageDTO.ChatResDTO> DeepSeekService(Message userMessage) {
        //get User Token
        Token deepSeekToken = tokenRepository.findByUser(userMessage.getChat().getUser()).stream()
                .filter(token -> token.getTokenType().toString().equals(MessageType.DEEPSEEK.toString()))
                .findFirst()
                .orElseThrow(() -> new MessageHandler(ErrorStatus._NOT_EXIST_TOKEN));
        String key = deepSeekToken.getTokenValue();

        //webClient init
        WebClient webClient = WebClient.builder().build();
        String uri = "https://api.deepseek.com/chat/completions";

        //req body init
        List<MessageDTO.DeepSeekMessage> messageList = new ArrayList<>();
        messageList.add(MessageDTO.DeepSeekMessage.builder().role("user").content(userMessage.getText()).build());
        MessageDTO.DeepSeekChatReqDTO reqBody = MessageDTO.DeepSeekChatReqDTO.builder()
                .model("deepseek-chat")
                .messages(messageList)
                .build();

        //url call
        MessageDTO.DeepSeekChatResDTO resBody;
        try {
            resBody = webClient.post()
                    .uri(uri)
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .header("Authorization", "Bearer " + key)
                    .body(Mono.just(reqBody), MessageDTO.DeepSeekChatResDTO.class)
                    .retrieve()
                    .bodyToMono(MessageDTO.DeepSeekChatResDTO.class)
                    .block();
        } catch (WebClientResponseException.Unauthorized e) {
            throw new MessageHandler(ErrorStatus._DEEPSEEK_CONNECT_FAIL);
        }

        //save response
        if (resBody != null) {
            String text = resBody.getChoices().get(0).getMessage().getContent();

            ModelMessage message = ModelMessage.builder()
                    .message(userMessage)
                    .messageType(MessageType.DEEPSEEK)
                    .messageAt(LocalDateTime.now())
                    .text(text)
                    .build();
            modelMessageRepository.save(message);

            return CompletableFuture.completedFuture(MessageDTO.ChatResDTO.builder()
                    .text(text)
                    .messageType(MessageType.DEEPSEEK)
                    .build());
        } else {
            throw new MessageHandler(ErrorStatus._DEEPSEEK_RESPONSE_NULL);
        }
    }




    @Async
    public CompletableFuture<MessageDTO.ChatResDTO> BardService(Message userMessage) {
        //get User Token
        Token bardToken = tokenRepository.findByUser(userMessage.getChat().getUser()).stream()
                .filter(token -> token.getTokenType().toString().equals(MessageType.BARD.toString()))
                .findFirst()
                .orElseThrow(() -> new MessageHandler(ErrorStatus._NOT_EXIST_TOKEN));
        String key = bardToken.getTokenValue();

        //webClient init
        WebClient webClient = WebClient.builder().build();
        String uri = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + key;

        //req body init
        List<MessageDTO.BardContents> contentsList = new ArrayList<>();
        contentsList.add(messageConverter.toBardContents(userMessage));

        MessageDTO.BardChatReqDTO reqBody = MessageDTO.BardChatReqDTO.builder()
                .contents(contentsList)
                .build();

        //url call
        MessageDTO.BardChatResDTO resBody;
        try {
            resBody = webClient.post()
                    .uri(uri)
                    .body(Mono.just(reqBody), MessageDTO.BardChatReqDTO.class)
                    .retrieve()
                    .bodyToMono(MessageDTO.BardChatResDTO.class)
                    .block();
        } catch (WebClientResponseException.BadRequest e) {
            throw new MessageHandler(ErrorStatus._BARD_CONNECT_FAIL);
        }

        //save response
        if (resBody != null) {
            String text = resBody.getCandidates().get(0).getContent().getParts().get(0).getText();

            ModelMessage message = ModelMessage.builder()
                    .message(userMessage)
                    .messageType(MessageType.BARD)
                    .messageAt(LocalDateTime.now())
                    .text(text)
                    .build();
            modelMessageRepository.save(message);

            return CompletableFuture.completedFuture(MessageDTO.ChatResDTO.builder()
                    .text(text)
                    .messageType(MessageType.BARD)
                    .build());
        } else {
            throw new MessageHandler(ErrorStatus._BARD_RESPONSE_NULL);
        }
    }


    @Async
    public CompletableFuture<MessageDTO.ChatResDTO> ClaudeService(Message userMessage) {

        User user = userMessage.getChat().getUser();
        Token token = tokenRepository.findByUserAndTokenType(user, TokenType.CLAUDE);
        int maxTokens = 1000;

        if (token == null)  throw new TokenHandler(ErrorStatus._BAD_REQUEST);

        //webClient init
        WebClient webClient = WebClient.builder()
                .baseUrl(claudeUrl)
                .defaultHeader("x-api-key", token.getTokenValue())
                .defaultHeader("anthropic-version", "2023-06-01")
                .defaultHeader("content-type", "application/json")
                .build();


        MessageDTO.ClaudeReqDto request = MessageDTO.ClaudeReqDto.builder()
                .model(claudeModel)
                .messages(List.of(MessageDTO.ClaudeReqDto.Message.builder()
                                .role("user")
                                .content(userMessage.getText())
                        .build()))
                .max_tokens(maxTokens)
                .build();


        //url call
        MessageDTO.claudeResDto response;
        try {
            response = webClient.post()
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(MessageDTO.claudeResDto.class)
                    .block();
        } catch (WebClientResponseException.BadRequest e) {
            throw new MessageHandler(ErrorStatus._BARD_CONNECT_FAIL);
        }

        //save response
        if (response != null) {
            String text = response.getContent().get(0).getText();

            ModelMessage message = ModelMessage.builder()
                    .message(userMessage)
                    .messageType(MessageType.CLAUDE)
                    .messageAt(LocalDateTime.now())
                    .text(text)
                    .build();
            modelMessageRepository.save(message);

            return CompletableFuture.completedFuture(MessageDTO.ChatResDTO.builder()
                    .text(text)
                    .messageType(MessageType.CLAUDE)
                    .build());
        } else {
            throw new MessageHandler(ErrorStatus._CLAUDE_RESPONSE_NULL);
        }

    }

    public MessageDTO.getChatResDTO getMessageList(User user, Chat chat){
        if(chat.getUser() != user){
            throw new MessageHandler(ErrorStatus._NOT_MATCH_CHAT);
        }

        List<MessageDTO.messageDTO> messageDto = new ArrayList<>();

        messageRepository.findByChat(chat).forEach(message -> {
            //user Message 추가
            List<MessageDTO.ChatResDTO> messageUserDTO = new ArrayList<>();
            messageUserDTO.add(MessageDTO.ChatResDTO.builder()
                    .messageType(MessageType.USER)
                    .text(message.getText())
                    .build());
            messageDto.add(MessageDTO.messageDTO.builder()
                    .isUser(true)
                    .messages(messageUserDTO)
                    .build());

            List<MessageDTO.ChatResDTO> messageModelDTO = new ArrayList<>();
            messageModelDTO = message.getModelMessages().stream()
                    .map(modelMessage -> MessageDTO.ChatResDTO.builder()
                            .messageType(modelMessage.getMessageType())
                            .text(modelMessage.getText())
                            .build())
                    .toList();
            messageDto.add(MessageDTO.messageDTO.builder()
                    .isUser(false)
                    .messages(messageModelDTO)
                    .build());
        });

        return MessageDTO.getChatResDTO.builder()
                .chatId(chat.getChatId())
                .chatName(chat.getChatName())
                .messages(messageDto)
                .build();
    }
}
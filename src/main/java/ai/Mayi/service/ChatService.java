package ai.Mayi.service;

import ai.Mayi.apiPayload.code.status.ErrorStatus;
import ai.Mayi.apiPayload.exception.handler.ChatHandler;
import ai.Mayi.converter.ChatConverter;
import ai.Mayi.domain.Chat;
import ai.Mayi.domain.User;
import ai.Mayi.repository.ChatRepository;
import ai.Mayi.repository.UserRepository;
import ai.Mayi.web.dto.ChatDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ChatService {

    private final UserRepository userRepository;
    private final ChatConverter chatConverter;
    private final ChatRepository chatRepository;

    @Transactional
    public Chat createChat(ChatDTO.ChatRequestDTO request) {
        User user = userRepository.findByUserId(request.getUserId()).orElseThrow(
                () -> new ChatHandler(ErrorStatus._NOT_EXIST_USER));

        Chat newChat = chatConverter.toChat(request, user);
        chatRepository.save(newChat);

        return newChat;
    }
}

package ai.Mayi.service;

import ai.Mayi.apiPayload.code.BaseCode;
import ai.Mayi.apiPayload.code.status.ErrorStatus;
import ai.Mayi.apiPayload.exception.GeneralException;
import ai.Mayi.domain.Token;
import ai.Mayi.domain.User;
import ai.Mayi.domain.enums.TokenType;
import ai.Mayi.repository.TokenRepository;
import ai.Mayi.repository.UserRepository;
import ai.Mayi.web.dto.CommonDTO;
import ai.Mayi.web.dto.TokenDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    public CommonDTO.IsSuccessDTO saveToken(TokenDTO.saveTokenReqDto request){
        User user = userRepository.findByUserId(request.getUserId()).orElseThrow(() -> new GeneralException(ErrorStatus._BAD_REQUEST));

        //기존 토큰 정보 전체 삭제
        tokenRepository.deleteAll(tokenRepository.findByUser(user));

        //입력한 토큰 정보 삽입
        request.getTokenList().forEach(tokenDto -> {
            TokenType tokenType;
            if(tokenDto.getTokenType().toUpperCase().equals(TokenType.GPT.toString())){
                tokenType = TokenType.GPT;
            } else if (tokenDto.getTokenType().toUpperCase().equals(TokenType.COPLIOT.toString())) {
                tokenType = TokenType.COPLIOT;
            } else if (tokenDto.getTokenType().toUpperCase().equals(TokenType.BARD.toString())) {
                tokenType = TokenType.BARD;
            } else if (tokenDto.getTokenType().toUpperCase().equals(TokenType.CLAUDE.toString())) {
                tokenType = TokenType.CLAUDE;
            } else {
                throw new GeneralException(ErrorStatus._BAD_REQUEST);
            }

            tokenRepository.save(Token.builder().user(user).tokenType(tokenType).tokenValue(tokenDto.getValue()).build());
        });

        return CommonDTO.IsSuccessDTO.builder()
                .isSuccess(true)
                .build();
    }
}

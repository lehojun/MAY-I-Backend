package ai.Mayi.apiPayload.exception.handler;

import ai.Mayi.apiPayload.code.BaseErrorCode;
import ai.Mayi.apiPayload.exception.GeneralException;

public class TokenHandler extends GeneralException {
    public TokenHandler(BaseErrorCode code) {super(code);}
}

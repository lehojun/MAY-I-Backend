package ai.Mayi.apiPayload.exception.handler;

import ai.Mayi.apiPayload.code.BaseErrorCode;
import ai.Mayi.apiPayload.exception.GeneralException;

public class ChatHandler extends GeneralException {
    public ChatHandler(BaseErrorCode code) {super(code);}
}

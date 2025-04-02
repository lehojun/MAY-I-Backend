package ai.Mayi.apiPayload.exception.handler;

import ai.Mayi.apiPayload.code.BaseErrorCode;
import ai.Mayi.apiPayload.exception.GeneralException;

public class MessageHandler extends GeneralException {
    public MessageHandler(BaseErrorCode code) {super(code);}
}
package ai.Mayi.apiPayload.exception.handler;

import ai.Mayi.apiPayload.code.BaseErrorCode;
import ai.Mayi.apiPayload.exception.GeneralException;

public class JwtHandler extends GeneralException {
    public JwtHandler(BaseErrorCode code) {super(code);}
}

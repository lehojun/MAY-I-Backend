package ai.Mayi.apiPayload.exception.handler;

import ai.Mayi.apiPayload.code.BaseErrorCode;
import ai.Mayi.apiPayload.exception.GeneralException;

public class SocialLoginHandler extends GeneralException {
  public SocialLoginHandler(BaseErrorCode code) {super(code);}
}

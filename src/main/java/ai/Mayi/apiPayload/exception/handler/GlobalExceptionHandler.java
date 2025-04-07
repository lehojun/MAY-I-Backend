package ai.Mayi.apiPayload.exception.handler;

import ai.Mayi.apiPayload.exception.GeneralException;
import ai.Mayi.apiPayload.code.ErrorReasonDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<ErrorReasonDTO> handleGeneralException(GeneralException ex) {
        return new ResponseEntity<>(ex.getErrorReasonHttpStatus(), ex.getErrorReasonHttpStatus().getHttpStatus());
    }
}

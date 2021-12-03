package hiepnh.noticemanagement.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import java.util.*;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomizedResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomizedResponseEntityExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<?> handleNotFoundException(ResourceNotFoundException ex) {
        LOGGER.error("Message error {} ", ex.getMessage());
        return new ResponseEntity<>(new ErrorResponseDto(Collections.singletonList(new ErrorDto(ex.getMessage()))), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<?> handleException(Exception ex) {
        LOGGER.error("Message error {} ", ex.getMessage());
        return new ResponseEntity<>(new ErrorResponseDto(Collections.singletonList(new ErrorDto(ex.getMessage()))), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoticeGenericException.class)
    public final ResponseEntity<?> handleNoticeGenericException(NoticeGenericException ex) {
        LOGGER.error("Message error {} ", ex.getMessage());
        return new ResponseEntity<>(new ErrorResponseDto(Collections.singletonList(new ErrorDto(ex.getMessage()))), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleMaxSizeException(MaxUploadSizeExceededException exc) {
        LOGGER.error("Message error {} ", exc.getMessage());
        return new ResponseEntity<>(new ErrorResponseDto(Collections.singletonList(new ErrorDto("Files are too large. Pls try again!"))), HttpStatus.EXPECTATION_FAILED);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<?> bindExceptionHandler(BindException ex){
        LOGGER.error("Message error {} ", ex.getMessage());
        Map<String, List<String>> body = new HashMap<>();
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        body.put("errors", errors);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}

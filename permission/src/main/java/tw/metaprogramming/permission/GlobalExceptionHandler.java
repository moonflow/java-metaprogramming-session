package tw.metaprogramming.permission;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import tw.metaprogramming.permission.dto.ErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    ErrorResponse handleException(Exception e){
        return new ErrorResponse(e.getMessage());
    }
}

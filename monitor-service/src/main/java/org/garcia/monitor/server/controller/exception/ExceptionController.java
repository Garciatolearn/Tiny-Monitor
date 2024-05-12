package org.garcia.monitor.server.controller.exception;


import jakarta.validation.ValidationException;
import org.garcia.monitor.server.entity.Result;
import org.garcia.monitor.server.entity.Results;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler({ValidationException.class})
    public Result<String> validError(ValidationException e){
        return Results.failure(e.getMessage(),"参数有误");
    }
}

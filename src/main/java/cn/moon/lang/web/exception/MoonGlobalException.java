package cn.moon.lang.web.exception;


import cn.moon.lang.web.Result;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Component
public class MoonGlobalException {

    @ExceptionHandler(Exception.class)
    public Result error(Exception e) {
        String msg = new HumanExceptionTool().covert( e);
        return Result.err().msg(msg);
    }



}

package cc.novices.usercontrol.commons;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 自定义异常处理类
     * @param e 异常
     * @return 自定义异常错误统一返回类
     */
    @ExceptionHandler(BusinessException.class)
    public Result businessExceptionHandler(BusinessException e){
        log.info(e.getMessage());
        System.out.println(e.getMessage());;
        return new Result<>().fail(e);
    }

    /**
     * 其他异常处理（应该全部为系统错误）
     * @param e 异常
     * @return 系统错误
     */
    @ExceptionHandler(Exception.class)
    public Result ExceptionHandler(Exception e){
        log.error(e.getMessage());
        e.printStackTrace();
        return new Result<>().fail(ResultEnum.SYSTEM_ERROR);
    }

}

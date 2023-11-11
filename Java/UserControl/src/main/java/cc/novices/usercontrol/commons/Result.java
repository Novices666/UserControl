package cc.novices.usercontrol.commons;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = -4179492909908304695L;

    private int code;
    private T data;
    private String msg;
    private String description;

    public Result() {
    }

    public Result(int code, T data, String msg, String description) {
        this.code = code;
        this.data = data;
        this.msg = msg;
        this.description = description;
    }


    public Result fail(BusinessException businessException){
        Result result = new Result();
        result.code = businessException.getCode();
        result.msg = businessException.getMsg();
        result.description = businessException.getDescription();
        return result;
    }

    public Result fail(BusinessException businessException,T data){
        Result result = new Result();
        result.code = businessException.getCode();
        result.msg = businessException.getMsg();
        result.description = businessException.getDescription();
        return result;
    }

    public Result fail(ResultEnum resultEnum){
        Result result = new Result();
        result.code = resultEnum.getCode();
        result.msg = resultEnum.getMsg();;
        return result;
    }

    public Result ok(T data,String description){
        Result result = new Result();
        result.code = ResultEnum.SUCCESS.getCode();
        result.msg = ResultEnum.SUCCESS.getMsg();
        result.data = data;
        result.description = description;
        return result;
    }

    public Result ok(T data){
        Result result = new Result();
        result.code = ResultEnum.SUCCESS.getCode();
        result.msg = ResultEnum.SUCCESS.getMsg();
        result.data = data;
        return result;
    }

    public Result ok(String description){
        Result result = new Result();
        result.code = ResultEnum.SUCCESS.getCode();
        result.msg = ResultEnum.SUCCESS.getMsg();
        result.description = description;
        return result;
    }



}

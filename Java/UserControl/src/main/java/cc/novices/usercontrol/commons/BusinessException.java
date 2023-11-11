package cc.novices.usercontrol.commons;

import lombok.Data;

import java.io.Serializable;

@Data
public class BusinessException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = -2913766614523852457L;

    private int code;
    private String msg;

    private String description;

    public BusinessException(int code, String msg, String description) {
        this.code = code;
        this.msg = msg;
        this.description = description;
    }

    public BusinessException(ResultEnum resultEnum, String description) {
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
        this.description = description;
    }

    public BusinessException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
    }
}

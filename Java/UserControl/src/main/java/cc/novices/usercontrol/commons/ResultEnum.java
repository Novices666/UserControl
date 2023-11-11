package cc.novices.usercontrol.commons;

import lombok.Getter;

@Getter
public enum ResultEnum {
    /**
     * 状态码说明
     * 成功 			20000
     * NO系列  	    40100
     * ERROR系列	    400xx
     * 服务器内部	    500xx
     */
    SUCCESS(20000,"succcess"),

    NO_LOGIN(40100,"用户未登录"),
    NO_ACCESS(40101,"请求权限不足"),

    ERROR_PARAMS(40001,"请求参数错误"),

    SYSTEM_ERROR(50000,"服务器内部错误");


    private final int code;
    private final String msg;

    ResultEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }


}

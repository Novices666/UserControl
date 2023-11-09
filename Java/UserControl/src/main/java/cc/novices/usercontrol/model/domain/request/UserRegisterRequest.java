package cc.novices.usercontrol.model.domain.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserRegisterRequest implements Serializable {
    private static final long serialVersionUID = -7854926339830168882L;

    private String userAccount;
    private String userPassword;
    private String chackPassword;
}

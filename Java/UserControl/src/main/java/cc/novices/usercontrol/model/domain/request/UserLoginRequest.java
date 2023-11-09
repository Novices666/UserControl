package cc.novices.usercontrol.model.domain.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserLoginRequest implements Serializable {


    private static final long serialVersionUID = 63586077486716304L;
    private String userAccount;
    private String userPassword;
}


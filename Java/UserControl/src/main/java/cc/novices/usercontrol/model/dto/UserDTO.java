package cc.novices.usercontrol.model.dto;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
public class UserDTO implements Serializable {
    private static final long serialVersionUID = -2823571370838459899L;
    private Long id;
    private String userAccount;
    private String userPassword;
    private Integer userType;
    private String username;
    private String avatarUrl;
    private Integer gender;
    private String phone;
    private String email;
    private Integer userStatus;
    private Date createTime;
    private Date updateTime;
    private Integer isDelete;
    private String tags;

}
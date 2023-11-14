package cc.novices.usercontrol.model.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 *
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     *
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     *
     */
    @TableField(value = "userAccount")
    private String userAccount;

    /**
     *
     */
    @TableField(value = "userPassword")
    private String userPassword;

    /**
     * 用户类型 0 - 普通用户 1 - 管理员
     */
    @TableField(value = "userType")
    private Integer userType;

    /**
     *
     */
    @TableField(value = "username")
    private String username;

    /**
     *
     */
    @TableField(value = "avatarUrl")
    private String avatarUrl;

    /**
     *
     */
    @TableField(value = "gender")
    private Integer gender;

    /**
     *
     */
    @TableField(value = "phone")
    private String phone;

    /**
     *
     */
    @TableField(value = "email")
    private String email;

    /**
     * 0正常
     */
    @TableField(value = "userStatus")
    private Integer userStatus;

    /**
     *
     */
    @TableField(value = "createTime")
    private Date createTime;

    /**
     *
     */
    @TableField(value = "updateTime")
    private Date updateTime;

    /**
     *
     */
    @TableLogic
    @TableField(value = "isDelete")
    private Integer isDelete;

    @TableField(value = "tags")
    private String tags;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
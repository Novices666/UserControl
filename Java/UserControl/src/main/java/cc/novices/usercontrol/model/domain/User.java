package cc.novices.usercontrol.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
    private String useraccount;

    /**
     *
     */
    @TableField(value = "userPassword")
    private String userpassword;

    /**
     * 用户类型 0 - 普通用户 1 - 管理员
     */
    @TableField(value = "userType")
    private Integer usertype;

    /**
     *
     */
    @TableField(value = "username")
    private String username;

    /**
     *
     */
    @TableField(value = "avatarUrl")
    private String avatarurl;

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
    private Integer userstatus;

    /**
     *
     */
    @TableField(value = "createTime")
    private Date createtime;

    /**
     *
     */
    @TableField(value = "updateTime")
    private Date updatetime;

    /**
     *
     */
    @TableField(value = "isDelete")
    private Integer isdelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
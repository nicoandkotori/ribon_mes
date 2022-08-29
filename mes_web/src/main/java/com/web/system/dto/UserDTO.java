package com.web.system.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Classname UserDTO
 * @Description 用户Dto
 * @Date 2019-04-23 21:26
 * @Version 1.0
 */
@Data
public class UserDTO implements Serializable {

    private String userName;
    private String userCode;
    private String lastLoginIp;
    private Date lastLoginDate;
    private String account;
    private String venCode;
    private String venName;
}

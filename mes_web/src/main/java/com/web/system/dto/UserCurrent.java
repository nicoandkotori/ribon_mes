package com.web.system.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by Yao on 2019/10/7.
 */
@Setter
@Getter
public class UserCurrent {
    private String id;
    private String userName;
    private String userCode;
    private Integer izAdmin;
    private String depId;
    private String depCode;
    private String depName;
    private String account;   //哪个账套的  建机  或者  重工。
}

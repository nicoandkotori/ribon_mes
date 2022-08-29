package com.web.system.dto;

import lombok.Data;

/**
 * 用户的登录名或者手机号码   与数据库对应的关系。
 * 需要存放到Redis中。
 * Created by Yao on 2018-6-20.
 */
@Data
public class UserDataSource {
    private String userCode;
    private String mobileCode;
    private String databaseName;


}

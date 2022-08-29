package com.web.demo.entity;

import lombok.Data;

import java.util.List;

/**
 * Created by Li on 2019/11/14.
 */
@Data
public class TreeTableResult {

    private int code;

    private String msg;

    private List data;

    public TreeTableResult(){
        code=0;
        msg="ok";
    }

    private int count;
}

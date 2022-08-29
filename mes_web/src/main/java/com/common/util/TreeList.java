package com.common.util;

import lombok.Data;

import java.util.List;

/**
 * Created by Li on 2019/11/20.
 */
@Data
public class TreeList {

    private String id;

    private String title;

    private String code;

    private String name;

    private Boolean open;

    private List<TreeList> children;

    private Boolean spread;
}

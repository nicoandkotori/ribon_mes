package com.common.util;

import lombok.Data;

/**
 * layer 相册图片类
 */

@Data
public class LayPhoto {
    /**
     * 图片名称
     */
    private String alt;

    /**
     * 图片id
     */
    private Integer pid;

    /**
     * 原图地址(相对路径)
     */
    private String src;

    /**
     * 缩略图地址(相对路径)
     */
    private String thumb;
}

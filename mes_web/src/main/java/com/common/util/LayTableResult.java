package com.common.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.util.List;

/**
 * jqgrid  返回实体类
 */
@Data
public class LayTableResult<E> {
    /**
     * layui表格必须参数⬇⬇⬇⬇⬇⬇
     */
    private Integer code = 0;
    private String msg = "";
    /**
     * 显示的记录
     */
    private List<E> data;
    /**
     * 总记录
     */
    private long count;

    private boolean success;

    public static LayTableResult<Object> success(IPage pg) {
        LayTableResult<Object> r = new LayTableResult<Object>();
        r.setSuccess(true);
        r.setMsg("操作成功");
        r.setData(pg.getRecords());
        r.setCount(pg.getTotal());
        return r;
    }

    public static LayTableResult<Object> error(String msg) {
        LayTableResult<Object> r = new LayTableResult<Object>();
        r.setSuccess(false);
        r.setMsg(msg);
        r.setCode(1);
        return r;
    }
}

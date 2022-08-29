package com.web.event;

import com.common.util.R;

public interface FineEventHandler<T> {
    /**
     * 事件的处理方法
     *
     * @param
     * @return
     */
    R execute(FineEventDTO<T> fineEventDTO);
}

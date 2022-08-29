package com.web.event;


import com.web.event.util.SpringUtil;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class FineEventListner implements ApplicationListener<FineEvent> {


    @Async
    @Override
    public void onApplicationEvent(FineEvent event){
        FineEventHandler fineEventHandler = SpringUtil.getBean(event.getFineEventDTO().getEventType().getValue(), FineEventHandler.class);
        assert fineEventHandler != null;
        fineEventHandler.execute(event.getFineEventDTO());
    }


}

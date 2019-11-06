package com.hy.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

/**
 * .
 * Created by of liaoxg
 * date: 2019/9/2
 * user: lxg
 * package_name: com.hy.config
 */
public class NoticeEvent extends ApplicationEvent {

    /**
     * .
     * message数据
     */
    @Setter
    @Getter
    private String message;

    /**
     * .
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the
     *               event initially occurred (never {@code null})
     * @param msg    消息
     */
    public NoticeEvent(final Object source, final String msg) {
        super(source);
        this.message = msg;
    }
}
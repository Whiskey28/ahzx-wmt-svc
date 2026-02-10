package com.wmt.framework.websocket.core.sender.local;

import com.wmt.framework.websocket.core.sender.AbstractWebSocketMessageSender;
import com.wmt.framework.websocket.core.sender.WebSocketMessageSender;
import com.wmt.framework.websocket.core.session.WebSocketSessionManager;

/**
 * 本地的 {@link WebSocketMessageSender} 实现类
 *
 * 注意：仅仅适合单机场景！！！
 *
 * @author wmt
 */
public class LocalWebSocketMessageSender extends AbstractWebSocketMessageSender {

    public LocalWebSocketMessageSender(WebSocketSessionManager sessionManager) {
        super(sessionManager);
    }

}

package com.mooo.sestus.indoor_locator.data;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class DjangoChannelListener extends WebSocketListener {

    private static final int NORMAL_CLOSURE_STATUS = 1000;

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        RemoteFloorPlanRepository.getInstance().onMessageReceived(text);
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        RemoteFloorPlanRepository.getInstance().onSocketClosing(webSocket, code,reason);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        RemoteFloorPlanRepository.getInstance().onFailure(webSocket, t, response);
    }

}

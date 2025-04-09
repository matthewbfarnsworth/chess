package client.net;

import com.google.gson.Gson;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.net.URI;

public class WebSocketCommunicator extends Endpoint {

    public Session session;
    ServerMessageObserver serverMessageObserver;

    public WebSocketCommunicator(String url, ServerMessageObserver serverMessageObserver) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.serverMessageObserver = serverMessageObserver;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            session = container.connectToServer(this, socketURI);

            session.addMessageHandler((MessageHandler.Whole<String>) message -> {
                ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                serverMessageObserver.notify(serverMessage);
            });
        }
        catch (Exception e) {
            throw new ResponseException(e.getMessage(), 500);
        }
    }

    public void send(String message) throws ResponseException {
        try {
            session.getBasicRemote().sendText(message);
        }
        catch (Exception e) {
            throw new ResponseException(e.getMessage(), 500);
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

}

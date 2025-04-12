package client.net;

import websocket.WebSocketSerializer;
import websocket.messages.ErrorServerMessage;
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

            session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    try {
                        ServerMessage serverMessage = WebSocketSerializer.deserialize(message, ServerMessage.class);
                        serverMessageObserver.notify(serverMessage);
                    }
                    catch (Exception e) {
                        serverMessageObserver.notify(new ErrorServerMessage(e.getMessage()));
                    }
                }
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

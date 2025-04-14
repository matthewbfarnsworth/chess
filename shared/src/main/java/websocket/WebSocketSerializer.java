package websocket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import websocket.commands.UserGameCommand;
import websocket.commands.UserGameCommandTypeAdapter;
import websocket.messages.ServerMessage;
import websocket.messages.ServerMessageTypeAdapter;

public class WebSocketSerializer {
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(UserGameCommand.class, new UserGameCommandTypeAdapter())
            .registerTypeAdapter(ServerMessage.class, new ServerMessageTypeAdapter())
            .create();

    public static <T> T deserialize(String json, Class<T> tClass) {
        return GSON.fromJson(json, tClass);
    }

    public static String serialize(Object object) {
        return GSON.toJson(object);
    }
}

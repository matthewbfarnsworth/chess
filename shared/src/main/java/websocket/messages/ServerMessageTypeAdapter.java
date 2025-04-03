package websocket.messages;

import com.google.gson.*;

import java.lang.reflect.Type;

public class ServerMessageTypeAdapter implements JsonDeserializer<ServerMessage> {
    @Override
    public ServerMessage deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String messageString = jsonObject.get("serverMessageType").getAsString();
        ServerMessage.ServerMessageType messageType;

        try {
            messageType = ServerMessage.ServerMessageType.valueOf(messageString);
        }
        catch (IllegalArgumentException e) {
            throw new JsonParseException(e);
        }

        return switch (messageType) {
            case LOAD_GAME -> context.deserialize(jsonElement, LoadGameServerMessage.class);
            case ERROR -> context.deserialize(jsonElement, ErrorServerMessage.class);
            case NOTIFICATION -> context.deserialize(jsonElement, NotificationMessage.class);
        };
    }
}

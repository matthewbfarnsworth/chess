package websocket.commands;

import com.google.gson.*;

import java.lang.reflect.Type;

public class UserGameCommandTypeAdapter implements JsonDeserializer<UserGameCommand> {
    @Override
    public UserGameCommand deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String commandString = jsonObject.get("commandType").getAsString();
        UserGameCommand.CommandType commandType;

        try {
            commandType = UserGameCommand.CommandType.valueOf(commandString);
        }
        catch (IllegalArgumentException e) {
            throw new JsonParseException(e);
        }

        return switch (commandType) {
            case CONNECT -> context.deserialize(jsonElement, ConnectGameCommand.class);
            case MAKE_MOVE -> context.deserialize(jsonElement, MakeMoveGameCommand.class);
            case LEAVE -> context.deserialize(jsonElement, LeaveGameCommand.class);
            case RESIGN -> context.deserialize(jsonElement, ResignGameCommand.class);
        };
    }
}

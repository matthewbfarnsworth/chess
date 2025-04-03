package websocket;

import chess.ChessMove;
import chess.ChessPosition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import websocket.commands.*;

public class WebSocketSerializerTests {
    @Test
    public void testConnectGameCommandSerialization() {
        ConnectGameCommand command = new ConnectGameCommand("a", 1);
        String serializedCommand = WebSocketSerializer.serialize(command);
        UserGameCommand deserializedCommand = WebSocketSerializer.deserialize(serializedCommand,
                UserGameCommand.class);
        Assertions.assertEquals(command, (ConnectGameCommand) deserializedCommand);
    }

    @Test
    public void testMakeMoveGameCommandSerialization() {
        ChessMove move = new ChessMove(new ChessPosition(2, 2), new ChessPosition(4, 2), null);
        MakeMoveGameCommand command = new MakeMoveGameCommand("a", 1, move);
        String serializedCommand = WebSocketSerializer.serialize(command);
        UserGameCommand deserializedCommand = WebSocketSerializer.deserialize(serializedCommand,
                UserGameCommand.class);
        Assertions.assertEquals(command, (MakeMoveGameCommand) deserializedCommand);
    }

    @Test
    public void testLeaveGameCommandSerialization() {
        LeaveGameCommand command = new LeaveGameCommand("a", 1);
        String serializedCommand = WebSocketSerializer.serialize(command);
        UserGameCommand deserializedCommand = WebSocketSerializer.deserialize(serializedCommand,
                UserGameCommand.class);
        Assertions.assertEquals(command, (LeaveGameCommand) deserializedCommand);
    }

    @Test
    public void testResignGameCommandSerialization() {
        ResignGameCommand command = new ResignGameCommand("a", 1);
        String serializedCommand = WebSocketSerializer.serialize(command);
        UserGameCommand deserializedCommand = WebSocketSerializer.deserialize(serializedCommand,
                UserGameCommand.class);
        Assertions.assertEquals(command, (ResignGameCommand) deserializedCommand);
    }
}

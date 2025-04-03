package chess;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ChessGameSerializerTests {
    @Test
    public void testSerializerDeserializerEqual() {
        ChessGame chessGame = new ChessGame();
        try {
            chessGame.makeMove(new ChessMove(new ChessPosition(2, 1), new ChessPosition(4, 1), null));
            String serializedGame = ChessGameSerializer.serialize(chessGame);
            ChessGame deserializedGame = ChessGameSerializer.deserialize(serializedGame);
            Assertions.assertEquals(chessGame, deserializedGame);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

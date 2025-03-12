package dataaccess;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ChessGameSerializerTests {
    @Test
    public void testSerializerDeserializerEqual() {
        ChessGame chessGame = new ChessGame();
        try {
            chessGame.makeMove(new ChessMove(new ChessPosition(2, 1), new ChessPosition(4, 1), null));
            String serializedGame = new ChessGameSerializer().chessGameToString(chessGame);
            ChessGame deserializedGame = new ChessGameSerializer().stringToChessGame(serializedGame);
            Assertions.assertEquals(chessGame, deserializedGame);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

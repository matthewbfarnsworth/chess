package chess;

import com.google.gson.Gson;

public class ChessGameSerializer {

    public static ChessGame deserialize(String string) {
        return new Gson().fromJson(string, ChessGame.class);
    }

    public static String serialize(ChessGame chessGame) {
        return new Gson().toJson(chessGame);
    }
}

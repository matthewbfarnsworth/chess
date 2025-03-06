package service;

import chess.ChessGame;
import com.google.gson.Gson;

public class ChessGameSerializer {
    public String chessGameToString(ChessGame chessGame) {
        return new Gson().toJson(chessGame);
    }

    public ChessGame stringToChessGame(String string) {
        return new Gson().fromJson(string, ChessGame.class);
    }
}

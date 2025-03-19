import chess.*;
import ui.ChessBoard;

public class Main {
    public static void main(String[] args) {
        System.out.println("♕ 240 Chess Client");
        new ChessBoard().printBoard(new chess.ChessGame().getBoard(), true);
    }
}
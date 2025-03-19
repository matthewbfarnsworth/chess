package ui;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import static ui.EscapeSequences.*;

public class ChessBoard {

    private static final int BOARD_SIZE = 8;
    private static final String BORDER_COLOR = SET_BG_COLOR_DARK_GREEN;
    private static final String LIGHT_TILE = SET_BG_COLOR_WHITE;
    private static final String DARK_TILE = SET_BG_COLOR_LIGHT_GREY;
    private static final String TEXT_COLOR = SET_TEXT_COLOR_WHITE;
    private static final String PIECE_COLOR = SET_TEXT_COLOR_BLACK;

    private String pieceToString(ChessPiece piece) {
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            return switch (piece.getPieceType()) {
                case BISHOP -> WHITE_BISHOP;
                case KING -> WHITE_KING;
                case KNIGHT -> WHITE_KNIGHT;
                case PAWN -> WHITE_PAWN;
                case QUEEN -> WHITE_QUEEN;
                case ROOK -> WHITE_ROOK;
            };
        }
        else {
            return switch (piece.getPieceType()) {
                case BISHOP -> BLACK_BISHOP;
                case KING -> BLACK_KING;
                case KNIGHT -> BLACK_KNIGHT;
                case PAWN -> BLACK_PAWN;
                case QUEEN -> BLACK_QUEEN;
                case ROOK -> BLACK_ROOK;
            };
        }
    }

    private String intToLetter(int integer) {
        String output = switch (integer) {
            case 1 -> "a";
            case 2 -> "b";
            case 3 -> "c";
            case 4 -> "d";
            case 5 -> "e";
            case 6 -> "f";
            case 7 -> "g";
            case 8 -> "h";
            default -> "?";
        };
        return " " + output + EM_SPACE;
    }

    private void printTile(String input, String backgroundColor, String textColor) {
        System.out.print(backgroundColor);
        System.out.print(textColor);
        System.out.print(input);
        System.out.print(RESET_BG_COLOR);
        System.out.print(RESET_TEXT_COLOR);
    }

    private void printHeader(boolean isWhite) {
        printTile(EMPTY, BORDER_COLOR, TEXT_COLOR);
        for (int i = 1; i <= BOARD_SIZE; i++) {
            printTile(intToLetter(i), BORDER_COLOR, TEXT_COLOR);
        }
        printTile(EMPTY, BORDER_COLOR, TEXT_COLOR);
        System.out.println();
    }

    private void printChessRow(chess.ChessBoard board, int row, boolean isWhite) {
        printTile(" " + row + EM_SPACE, BORDER_COLOR, TEXT_COLOR);
        for (int i = 1; i <= BOARD_SIZE; i++) {
            String tileColor = (row + i) % 2 == 0 ? DARK_TILE : LIGHT_TILE;
            ChessPiece piece = board.getPiece(new ChessPosition(row, i));
            if (piece == null) {
                printTile(EMPTY, tileColor, PIECE_COLOR);
            }
            else {
                printTile(pieceToString(piece), tileColor, PIECE_COLOR);
            }
        }
        printTile(" " + row + EM_SPACE, BORDER_COLOR, TEXT_COLOR);
        System.out.println();
    }

    public void printBoard(chess.ChessBoard board, boolean isWhite) {
        printHeader(isWhite);
        for (int i = BOARD_SIZE; i >= 1; i--) {
            printChessRow(board, i, isWhite);
        }
        printHeader(isWhite);
    }

}

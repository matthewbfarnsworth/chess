package client.ui;

import chess.*;

import java.util.Collection;

import static client.ui.EscapeSequences.*;

public class ChessBoard {

    private static final int BOARD_SIZE = 8;
    private static final String BORDER_COLOR = SET_BG_COLOR_DARK_GREEN;
    private static final String LIGHT_TILE = SET_BG_COLOR_WHITE;
    private static final String DARK_TILE = SET_BG_COLOR_LIGHT_GREY;
    private static final String HIGHLIGHTED_LIGHT_TILE = SET_BG_COLOR_LIGHT_BLUE;
    private static final String HIGHLIGHTED_DARK_TILE = SET_BG_COLOR_BLUE;
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

    private void printBlock(String input, String backgroundColor, String textColor) {
        System.out.print(backgroundColor);
        System.out.print(textColor);
        System.out.print(input);
        System.out.print(RESET_BG_COLOR);
        System.out.print(RESET_TEXT_COLOR);
    }

    private void printHeader(boolean isWhite) {
        printBlock(EMPTY, BORDER_COLOR, TEXT_COLOR);
        if (isWhite) {
            for (int i = 1; i <= BOARD_SIZE; i++) {
                printBlock(intToLetter(i), BORDER_COLOR, TEXT_COLOR);
            }
        }
        else {
            for (int i = BOARD_SIZE; i >= 1; i--) {
                printBlock(intToLetter(i), BORDER_COLOR, TEXT_COLOR);
            }
        }
        printBlock(EMPTY, BORDER_COLOR, TEXT_COLOR);
        System.out.println();
    }

    private void printTile(chess.ChessBoard board, boolean[][] highlightedTiles, int row, int col) {
        String tileColor;
        if (highlightedTiles[row - 1][col - 1]) {
            tileColor = (row + col) % 2 == 0 ? HIGHLIGHTED_DARK_TILE : HIGHLIGHTED_LIGHT_TILE;
        }
        else {
            tileColor = (row + col) % 2 == 0 ? DARK_TILE : LIGHT_TILE;
        }
        ChessPiece piece = board.getPiece(new ChessPosition(row, col));
        if (piece == null) {
            printBlock(EMPTY, tileColor, PIECE_COLOR);
        }
        else {
            printBlock(pieceToString(piece), tileColor, PIECE_COLOR);
        }
    }

    private void printChessRow(chess.ChessBoard board, boolean[][] highlightedTiles, int row, boolean isWhite) {
        printBlock(" " + row + EM_SPACE, BORDER_COLOR, TEXT_COLOR);
        if (isWhite) {
            for (int col = 1; col <= BOARD_SIZE; col++) {
                printTile(board, highlightedTiles, row, col);
            }
        }
        else {
            for (int col = BOARD_SIZE; col >= 1; col--) {
                printTile(board, highlightedTiles, row, col);
            }
        }
        printBlock(" " + row + EM_SPACE, BORDER_COLOR, TEXT_COLOR);
        System.out.println();
    }

    private boolean[][] getHighlightedMoveTiles(ChessGame game, chess.ChessPosition startPosition) {
        boolean[][] highlightedBoard = new boolean[BOARD_SIZE][BOARD_SIZE];
        if (startPosition != null) {
            int startRow = startPosition.getRow();
            int startCol = startPosition.getColumn();
            highlightedBoard[startRow - 1][startCol - 1] = true;
            Collection<ChessMove> validMoves = game.validMoves(startPosition);
            for (chess.ChessMove move : validMoves) {
                ChessPosition endPosition = move.getEndPosition();
                int endRow = endPosition.getRow();
                int endCol = endPosition.getColumn();
                highlightedBoard[endRow - 1][endCol - 1] = true;
            }
        }
        return highlightedBoard;
    }

    public void printBoard(ChessGame game, boolean isWhite) {
        printBoard(game, null, isWhite);
    }

    public void printBoard(ChessGame game, ChessPosition position, boolean isWhite) {
        printHeader(isWhite);
        boolean[][] highlightedTiles = getHighlightedMoveTiles(game, position);
        if (isWhite) {
            for (int i = BOARD_SIZE; i >= 1; i--) {
                printChessRow(game.getBoard(), highlightedTiles, i, true);
            }
        }
        else {
            for (int i = 1; i <= BOARD_SIZE; i++) {
                printChessRow(game.getBoard(), highlightedTiles, i, false);
            }
        }
        printHeader(isWhite);
    }

}

package chess;

import java.util.Collection;
import java.util.HashSet;

public interface PieceMovesCalculator {
    int BOARD_LOWER_LIMIT = 1;
    int BOARD_UPPER_LIMIT = 8;

    Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition);

    static boolean withinBounds(ChessPosition checkPosition) {
        int checkRow = checkPosition.getRow();
        int checkCol = checkPosition.getColumn();
        return checkRow >= BOARD_LOWER_LIMIT && checkRow <= BOARD_UPPER_LIMIT &&
                checkCol >= BOARD_LOWER_LIMIT && checkCol <= BOARD_UPPER_LIMIT;
    }

    static Collection<ChessMove> diagonalMoves(ChessBoard board, ChessPosition myPosition, boolean down,
                                                boolean right) {
        int checkRow = myPosition.getRow();
        int checkCol = myPosition.getColumn();
        Collection<ChessMove> bishopMoves = new HashSet<>();
        ChessPiece checkPiece;
        while (((!down && checkRow > BOARD_LOWER_LIMIT) || (down && checkRow < BOARD_UPPER_LIMIT)) &&
                ((!right && checkCol > BOARD_LOWER_LIMIT) || (right && checkCol < BOARD_UPPER_LIMIT))) {
            checkRow = down ? checkRow + 1: checkRow - 1;
            checkCol = right ?  checkCol + 1: checkCol - 1;
            ChessPosition checkPosition = new ChessPosition(checkRow, checkCol);
            checkPiece = board.getPiece(checkPosition);
            if (checkPiece == null) {
                bishopMoves.add(new ChessMove(myPosition, checkPosition, null));
            }
            else if (checkPiece.getTeamColor() == board.getPiece(myPosition).getTeamColor()) {
                break;
            }
            else {
                bishopMoves.add(new ChessMove(myPosition, checkPosition, null));
                break;
            }
        }
        return bishopMoves;
    }

    static Collection<ChessMove> horizontalVerticalMoves(ChessBoard board, ChessPosition myPosition, boolean vertical,
                                                          boolean downRight) {
        int checkRow = myPosition.getRow();
        int checkCol = myPosition.getColumn();
        Collection<ChessMove> horizontalVerticalMoves = new HashSet<>();
        ChessPiece checkPiece;
        while ((vertical && downRight && checkRow < BOARD_UPPER_LIMIT) ||
                (vertical && !downRight && checkRow > BOARD_LOWER_LIMIT) ||
                (!vertical && downRight && checkCol < BOARD_UPPER_LIMIT) ||
                (!vertical && !downRight && checkCol > BOARD_LOWER_LIMIT)) {
            if (vertical) {
                checkRow = downRight ? checkRow + 1: checkRow - 1;
            }
            else {
                checkCol = downRight ?  checkCol + 1: checkCol - 1;
            }
            ChessPosition checkPosition = new ChessPosition(checkRow, checkCol);
            checkPiece = board.getPiece(checkPosition);
            if (checkPiece == null) {
                horizontalVerticalMoves.add(new ChessMove(myPosition, checkPosition, null));
            }
            else if (checkPiece.getTeamColor() == board.getPiece(myPosition).getTeamColor()) {
                break;
            }
            else {
                horizontalVerticalMoves.add(new ChessMove(myPosition, checkPosition, null));
                break;
            }
        }
        return horizontalVerticalMoves;
    }
}

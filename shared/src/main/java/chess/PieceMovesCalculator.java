package chess;

import java.util.Collection;
import java.util.HashSet;

abstract class PieceMovesCalculator {
    static final int BOARD_LOWER_LIMIT = 1;
    static final int BOARD_UPPER_LIMIT = 8;

    abstract Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition);

    protected boolean isNullPiece(ChessBoard board, ChessPosition checkPosition) {
        return board.getPiece(checkPosition) == null;
    }

    protected boolean isEnemyTeam(ChessBoard board, ChessPosition myPosition, ChessPosition checkPosition) {
        return !isNullPiece(board, checkPosition)
                && board.getPiece(myPosition).getTeamColor() != board.getPiece(checkPosition).getTeamColor();
    }

    protected boolean isValidPosition(ChessPosition checkPosition) {
        int checkRow = checkPosition.getRow();
        int checkCol = checkPosition.getColumn();
        return checkRow >= BOARD_LOWER_LIMIT && checkRow <= BOARD_UPPER_LIMIT
                && checkCol >= BOARD_LOWER_LIMIT && checkCol <= BOARD_UPPER_LIMIT;
    }

    protected Collection<ChessMove> setMoves(ChessBoard board, ChessPosition myPosition,
                                          int[] rowMoves, int[] colMoves) {
        if (rowMoves.length != colMoves.length) {
            throw new RuntimeException("Arrays of directions must be equal");
        }

        int checkRow;
        int checkCol;
        ChessPosition checkPosition;
        Collection<ChessMove> setMoves = new HashSet<>();

        for (int i = 0; i < rowMoves.length; i++) {
            checkRow = myPosition.getRow() + rowMoves[i];
            checkCol = myPosition.getColumn() + colMoves[i];
            checkPosition = new ChessPosition(checkRow, checkCol);
            if (isValidPosition(checkPosition)) {
                if (isNullPiece(board, checkPosition) || isEnemyTeam(board, myPosition, checkPosition)) {
                    setMoves.add(new ChessMove(myPosition, checkPosition, null));
                }
            }
        }

        return setMoves;
    }

    protected Collection<ChessMove> slideMoves(ChessBoard board, ChessPosition myPosition,
                                            int[] rowDirections, int[] colDirections) {
        if (rowDirections.length != colDirections.length) {
            throw new RuntimeException("Arrays of directions must be equal");
        }

        int checkRow;
        int checkCol;
        ChessPosition checkPosition;
        Collection<ChessMove> slideMoves = new HashSet<>();

        for (int i = 0; i < rowDirections.length; i++) {
            checkRow = myPosition.getRow() + rowDirections[i];
            checkCol = myPosition.getColumn() + colDirections[i];
            checkPosition = new ChessPosition(checkRow, checkCol);
            while (isValidPosition(checkPosition)) {
                if (isNullPiece(board, checkPosition)) {
                    slideMoves.add(new ChessMove(myPosition, checkPosition, null));
                    checkRow += rowDirections[i];
                    checkCol += colDirections[i];
                    checkPosition = new ChessPosition(checkRow, checkCol);
                }
                else if (isEnemyTeam(board, myPosition, checkPosition)) {
                    slideMoves.add(new ChessMove(myPosition, checkPosition, null));
                    break;
                }
                else break;
            }
        }

        return slideMoves;
    }
}

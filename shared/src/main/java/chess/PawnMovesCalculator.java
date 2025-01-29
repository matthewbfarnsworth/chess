package chess;

import java.util.Collection;
import java.util.HashSet;

public class PawnMovesCalculator extends PieceMovesCalculator {

    private Collection<ChessMove> promotionMoves(ChessPosition startPosition, ChessPosition endPosition) {
        Collection<ChessMove> promotionMoves = new HashSet<>();
        promotionMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.BISHOP));
        promotionMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.KNIGHT));
        promotionMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.QUEEN));
        promotionMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.ROOK));
        return promotionMoves;
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        int checkRow;
        int checkCol;
        ChessPosition checkPosition;
        ChessGame.TeamColor pieceColor = board.getPiece(myPosition).getTeamColor();
        int rowForward = pieceColor == ChessGame.TeamColor.WHITE ? 1 : -1;
        int startRow = pieceColor == ChessGame.TeamColor.WHITE ? 2 : BOARD_UPPER_LIMIT - 1;
        int promotionRow = pieceColor == ChessGame.TeamColor.WHITE ? BOARD_UPPER_LIMIT : BOARD_LOWER_LIMIT;
        Collection<ChessMove> pieceMoves = new HashSet<>();

        for (int colOffset = -1; colOffset <= 1; colOffset++) {
            checkRow = myPosition.getRow() + rowForward;
            checkCol = myPosition.getColumn() + colOffset;
            checkPosition = new ChessPosition(checkRow, checkCol);
            if (!isValidPosition(checkPosition)) continue;
            if (colOffset == 0) {
                if (isNullPiece(board, checkPosition)) {
                    if (checkRow == promotionRow) {
                        pieceMoves.addAll(promotionMoves(myPosition, checkPosition));
                    }
                    else {
                        pieceMoves.add(new ChessMove(myPosition, checkPosition, null));
                    }
                    if (myPosition.getRow() == startRow) {
                        checkRow += rowForward;
                        checkPosition = new ChessPosition(checkRow, checkCol);
                        if (isValidPosition(checkPosition)
                                && isNullPiece(board, checkPosition)) {
                            pieceMoves.add(new ChessMove(myPosition, checkPosition, null));
                        }
                    }
                }
            }
            else {
                if (isEnemyTeam(board, myPosition, checkPosition)) {
                    if (checkRow == promotionRow) {
                        pieceMoves.addAll(promotionMoves(myPosition, checkPosition));
                    }
                    else {
                        pieceMoves.add(new ChessMove(myPosition, checkPosition, null));
                    }
                }
            }
        }

        return pieceMoves;
    }
}

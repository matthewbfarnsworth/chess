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

    private Collection<ChessMove> forwardMoves(ChessBoard board, ChessPosition myPosition, int rowForward,
                                               int startRow, int promotionRow) {
        int checkRow = myPosition.getRow() + rowForward;
        int checkCol = myPosition.getColumn();
        ChessPosition checkPosition = new ChessPosition(checkRow, checkCol);
        Collection<ChessMove> forwardMoves = new HashSet<>();
        if (isValidPosition(checkPosition) && isNullPiece(board, checkPosition)){
            if (checkRow == promotionRow) {
                forwardMoves.addAll(promotionMoves(myPosition, checkPosition));
            }
            else {
                forwardMoves.add(new ChessMove(myPosition, checkPosition, null));
            }
            if (myPosition.getRow() == startRow) {
                checkRow += rowForward;
                checkPosition = new ChessPosition(checkRow, checkCol);
                if (isValidPosition(checkPosition)
                        && isNullPiece(board, checkPosition)) {
                    forwardMoves.add(new ChessMove(myPosition, checkPosition, null));
                }
            }
            return forwardMoves;
        }
        return forwardMoves;
    }

    private Collection<ChessMove> captureMoves(ChessBoard board, ChessPosition myPosition, int rowForward,
                                               int promotionRow) {
        Collection<ChessMove> captureMoves = new HashSet<>();
        for (int colOffset = -1; colOffset <= 1; colOffset += 2) {
            int checkRow = myPosition.getRow() + rowForward;
            int checkCol = myPosition.getColumn() + colOffset;
            ChessPosition checkPosition = new ChessPosition(checkRow, checkCol);
            if (isValidPosition(checkPosition) && isEnemyTeam(board, myPosition, checkPosition)) {
                if (checkRow == promotionRow) {
                    captureMoves.addAll(promotionMoves(myPosition, checkPosition));
                }
                else {
                    captureMoves.add(new ChessMove(myPosition, checkPosition, null));
                }
            }
        }
        return captureMoves;
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessGame.TeamColor pieceColor = board.getPiece(myPosition).getTeamColor();
        int rowForward = pieceColor == ChessGame.TeamColor.WHITE ? 1 : -1;
        int startRow = pieceColor == ChessGame.TeamColor.WHITE ? 2 : BOARD_UPPER_LIMIT - 1;
        int promotionRow = pieceColor == ChessGame.TeamColor.WHITE ? BOARD_UPPER_LIMIT : BOARD_LOWER_LIMIT;
        Collection<ChessMove> pieceMoves = new HashSet<>();
        pieceMoves.addAll(forwardMoves(board, myPosition, rowForward, startRow, promotionRow));
        pieceMoves.addAll(captureMoves(board, myPosition, rowForward, promotionRow));
        return pieceMoves;
    }
}

package chess;

import java.util.Collection;
import java.util.HashSet;

public class KingMovesCalculator implements PieceMovesCalculator {
    int[] kingMovesRowOffsets = {-1, -1, -1, 0, 0, 1, 1, 1};
    int[] kingMovesColOffsets = {-1, 0, 1, -1, 1, -1, 0, 1};
    static int MAX_KING_MOVES = 8;

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> kingMoves = new HashSet<>();
        for (int i = 0; i < MAX_KING_MOVES; i++) {
            int checkRow = myPosition.getRow() + kingMovesRowOffsets[i];
            int checkCol = myPosition.getColumn() + kingMovesColOffsets[i];
            ChessPosition checkPosition = new ChessPosition(checkRow, checkCol);
            if (PieceMovesCalculator.withinBounds(checkPosition)) {
                ChessPiece checkPiece = board.getPiece(new ChessPosition(checkRow, checkCol));
                if (checkPiece == null || checkPiece.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                    kingMoves.add(new ChessMove(myPosition, checkPosition, null));
                }
            }
        }
        return kingMoves;
    }
}

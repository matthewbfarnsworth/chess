package chess;

import java.util.Collection;
import java.util.HashSet;

public class KingMovesCalculator implements PieceMovesCalculator {
    int[] kingMovesRowOffsets = {-1, -1, -1, 0, 0, 1, 1, 1};
    int[] kingMovesColOffsets = {-1, 0, 1, -1, 1, -1, 0, 1};
    int MAX_KING_MOVES = 8;

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> kingMoves = new HashSet<>();
        for (int i = 0; i < MAX_KING_MOVES; i++) {
            int checkRow = myPosition.getRow() + kingMovesRowOffsets[i];
            int checkCol = myPosition.getColumn() + kingMovesColOffsets[i];
            if (checkRow >= BOARD_LOWER_LIMIT && checkRow <= BOARD_UPPER_LIMIT &&
                    checkCol >= BOARD_LOWER_LIMIT && checkCol <= BOARD_UPPER_LIMIT) {
                ChessPiece checkPiece = board.getPiece(new ChessPosition(checkRow, checkCol));
                if (checkPiece == null || checkPiece.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                    ChessPosition checkPosition = new ChessPosition(checkRow, checkCol);
                    kingMoves.add(new ChessMove(myPosition, checkPosition, null));
                }
            }
        }
        return kingMoves;
    }
}

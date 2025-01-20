package chess;

import java.util.Collection;
import java.util.HashSet;

public class KnightMovesCalculator implements PieceMovesCalculator {
    int[] knightMovesRowOffsets = {-2, -2, -1, -1, 1, 1, 2, 2};
    int[] knightMovesColOffsets = {-1, 1, -2, 2, -2, 2, -1, 1};
    int MAX_KNIGHT_MOVES = 8;

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> knightMoves = new HashSet<>();
        for (int i = 0; i < MAX_KNIGHT_MOVES; i++) {
            int checkRow = myPosition.getRow() + knightMovesRowOffsets[i];
            int checkCol = myPosition.getColumn() + knightMovesColOffsets[i];
            if (checkRow >= BOARD_LOWER_LIMIT && checkRow <= BOARD_UPPER_LIMIT &&
                    checkCol >= BOARD_LOWER_LIMIT && checkCol <= BOARD_UPPER_LIMIT) {
                ChessPiece checkPiece = board.getPiece(new ChessPosition(checkRow, checkCol));
                if (checkPiece == null || checkPiece.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                    ChessPosition checkPosition = new ChessPosition(checkRow, checkCol);
                    knightMoves.add(new ChessMove(myPosition, checkPosition, null));
                }
            }
        }
        return knightMoves;
    }
}

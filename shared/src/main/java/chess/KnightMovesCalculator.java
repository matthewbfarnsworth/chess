package chess;

import java.util.Collection;

public class KnightMovesCalculator extends PieceMovesCalculator {
    private final int[] rowMoves = new int[]{-2, -2, -1, 1, 2, 2, -1, 1};
    private final int[] colMoves = new int[]{-1, 1, 2, 2, -1, 1, -2, -2};

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return setMoves(board, myPosition, rowMoves, colMoves);
    }
}

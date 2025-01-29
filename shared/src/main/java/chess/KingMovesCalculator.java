package chess;

import java.util.Collection;

public class KingMovesCalculator extends PieceMovesCalculator {
    private final int[] rowMoves = new int[]{-1, -1, -1, 0, 0, 1, 1, 1};
    private final int[] colMoves = new int[]{-1, 0, 1, -1, 1, -1, 0, 1};

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return setMoves(board, myPosition, rowMoves, colMoves);
    }
}

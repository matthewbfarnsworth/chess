package chess;

import java.util.Collection;

public class QueenMovesCalculator extends PieceMovesCalculator {
    private final int[] rowDirections = new int[]{-1, -1, -1, 0, 0, 1, 1, 1};
    private final int[] colDirections = new int[]{-1, 0, 1, -1, 1, -1, 0, 1};

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return slideMoves(board, myPosition, rowDirections, colDirections);
    }
}

package chess;

import java.util.Collection;

public class RookMovesCalculator extends PieceMovesCalculator {
    private final int[] rowDirections = new int[]{-1, 0, 1, 0};
    private final int[] colDirections = new int[]{0, 1, 0, -1};

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return slideMoves(board, myPosition, rowDirections, colDirections);
    }
}

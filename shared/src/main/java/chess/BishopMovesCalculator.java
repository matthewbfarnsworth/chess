package chess;

import java.util.Collection;

public class BishopMovesCalculator extends PieceMovesCalculator {
    private final int[] rowDirections = new int[]{-1, -1, 1, 1};
    private final int[] colDirections = new int[]{-1, 1, -1, 1};

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return slideMoves(board, myPosition, rowDirections, colDirections);
    }
}

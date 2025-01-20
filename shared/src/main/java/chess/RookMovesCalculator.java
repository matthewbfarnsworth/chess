package chess;

import java.util.Collection;
import java.util.HashSet;

public class RookMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> rookMoves = new HashSet<>();
        rookMoves.addAll(PieceMovesCalculator.horizontalVerticalMoves(board, myPosition, true, false));
        rookMoves.addAll(PieceMovesCalculator.horizontalVerticalMoves(board, myPosition, false, true));
        rookMoves.addAll(PieceMovesCalculator.horizontalVerticalMoves(board, myPosition, true, true));
        rookMoves.addAll(PieceMovesCalculator.horizontalVerticalMoves(board, myPosition, false, false));
        return rookMoves;
    }
}

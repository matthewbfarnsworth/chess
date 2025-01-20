package chess;

import java.util.Collection;
import java.util.HashSet;

public class BishopMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> bishopMoves = new HashSet<>();
        bishopMoves.addAll(PieceMovesCalculator.diagonalMoves(board, myPosition, false, false));
        bishopMoves.addAll(PieceMovesCalculator.diagonalMoves(board, myPosition, true, false));
        bishopMoves.addAll(PieceMovesCalculator.diagonalMoves(board, myPosition, false, true));
        bishopMoves.addAll(PieceMovesCalculator.diagonalMoves(board, myPosition, true, true));
        return bishopMoves;
    }
}

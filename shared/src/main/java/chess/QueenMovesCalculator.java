package chess;

import java.util.Collection;
import java.util.HashSet;

public class QueenMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> queenMoves = new HashSet<>();
        queenMoves.addAll(PieceMovesCalculator.diagonalMoves(board, myPosition, false, false));
        queenMoves.addAll(PieceMovesCalculator.horizontalVerticalMoves(board, myPosition, true, false));
        queenMoves.addAll(PieceMovesCalculator.diagonalMoves(board, myPosition, true, false));
        queenMoves.addAll(PieceMovesCalculator.horizontalVerticalMoves(board, myPosition, false, true));
        queenMoves.addAll(PieceMovesCalculator.diagonalMoves(board, myPosition, false, true));
        queenMoves.addAll(PieceMovesCalculator.horizontalVerticalMoves(board, myPosition, true, true));
        queenMoves.addAll(PieceMovesCalculator.diagonalMoves(board, myPosition, true, true));
        queenMoves.addAll(PieceMovesCalculator.horizontalVerticalMoves(board, myPosition, false, false));
        return queenMoves;
    }
}

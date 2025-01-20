package chess;

import java.util.Collection;
import java.util.HashSet;

public class KingMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> kingMoves = new HashSet<>();
        for (int checkRowOffset = -1; checkRowOffset <= 1; checkRowOffset++) {
            for (int checkColOffset = -1; checkColOffset <= 1; checkColOffset++) {
                int checkRow = myPosition.getRow() + checkRowOffset;
                int checkCol = myPosition.getColumn() + checkColOffset;
                if (checkRow >= BOARD_LOWER_LIMIT && checkRow <= BOARD_UPPER_LIMIT &&
                        checkCol >= BOARD_LOWER_LIMIT && checkCol <= BOARD_UPPER_LIMIT) {
                    ChessPiece checkPiece = board.getPiece(new ChessPosition(checkRow, checkCol));
                    if (checkPiece == null || checkPiece.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                        ChessPosition checkPosition = new ChessPosition(checkRow, checkCol);
                        kingMoves.add(new ChessMove(myPosition, checkPosition, null));
                    }
                }
            }
        }
        return kingMoves;
    }
}

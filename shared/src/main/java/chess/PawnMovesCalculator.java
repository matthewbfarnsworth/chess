package chess;

import java.util.Collection;
import java.util.HashSet;

public class PawnMovesCalculator implements PieceMovesCalculator {
    private Collection<ChessMove> pawnPromotionMoves(ChessPosition startPosition, ChessPosition endPosition) {
        Collection<ChessMove> pawnPromotionMoves = new HashSet<>();
        pawnPromotionMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.BISHOP));
        pawnPromotionMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.KNIGHT));
        pawnPromotionMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.QUEEN));
        pawnPromotionMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.ROOK));
        return pawnPromotionMoves;
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> pawnMoves = new HashSet<>();
        ChessGame.TeamColor teamColor = board.getPiece(myPosition).getTeamColor();
        int forwardRowOffset = teamColor == ChessGame.TeamColor.WHITE ? 1 : -1;
        int startingRow = teamColor == ChessGame.TeamColor.WHITE ? 2 : 7;

        //Forward move
        int checkRow = myPosition.getRow() + forwardRowOffset;
        int checkCol = myPosition.getColumn();
        ChessPosition checkPosition = new ChessPosition(checkRow, checkCol);
        if (PieceMovesCalculator.withinBounds(checkPosition) && board.getPiece(checkPosition) == null) {
            //Promote if at end of board
            if (checkRow == BOARD_LOWER_LIMIT || checkRow == BOARD_UPPER_LIMIT) {
                pawnMoves.addAll(pawnPromotionMoves(myPosition, checkPosition));
            }
            else {
                pawnMoves.add(new ChessMove(myPosition, checkPosition, null));
                //Double move start
                if (myPosition.getRow() == startingRow) {
                    checkRow = myPosition.getRow() + forwardRowOffset * 2;
                    checkPosition = new ChessPosition(checkRow, checkCol);
                    if (PieceMovesCalculator.withinBounds(checkPosition) && board.getPiece(checkPosition) == null) {
                        pawnMoves.add(new ChessMove(myPosition, checkPosition, null));
                    }
                }
            }
        }
        //Capture
        checkRow = myPosition.getRow() + forwardRowOffset;
        for (int colOffset = -1; colOffset <= 1; colOffset += 2) {
            checkCol = myPosition.getColumn() + colOffset;
            checkPosition = new ChessPosition(checkRow, checkCol);
            if (PieceMovesCalculator.withinBounds(checkPosition)) {
                if (board.getPiece(checkPosition) != null && board.getPiece(checkPosition).getTeamColor() != teamColor) {
                    //Promote if at end of board
                    if (checkRow == BOARD_LOWER_LIMIT || checkRow == BOARD_UPPER_LIMIT) {
                        pawnMoves.addAll(pawnPromotionMoves(myPosition, checkPosition));
                    }
                    else {
                        pawnMoves.add(new ChessMove(myPosition, checkPosition, null));
                    }
                }
            }
        }
        return pawnMoves;
    }
}

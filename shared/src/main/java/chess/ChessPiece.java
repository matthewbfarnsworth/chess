package chess;

import java.util.Collection;
import java.util.Objects;
import java.util.*;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType type;

    private final int BOARD_LOWER_LIMIT = 1;
    private final int BOARD_UPPER_LIMIT = 8;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    private Collection<ChessMove> bishopSubsetMoves(ChessBoard board, ChessPosition myPosition, boolean right,
                                                    boolean down) {
        int checkRow = myPosition.getRow();
        int checkCol = myPosition.getColumn();
        Set<ChessMove> bishopMoves = new HashSet<>();
        ChessPiece checkPiece;
        while (((!right && checkRow > BOARD_LOWER_LIMIT) || (right && checkRow < BOARD_UPPER_LIMIT)) &&
                ((!down && checkCol > BOARD_LOWER_LIMIT) || (down && checkCol < BOARD_UPPER_LIMIT))) {
            checkRow = right ? checkRow + 1: checkRow - 1;
            checkCol = down ?  checkCol + 1: checkCol - 1;
            ChessPosition checkPosition = new ChessPosition(checkRow, checkCol);
            checkPiece = board.getPiece(checkPosition);
            if (checkPiece == null) {
                bishopMoves.add(new ChessMove(myPosition, checkPosition, null));
            }
            else if (checkPiece.getTeamColor() == pieceColor) {
                break;
            }
            else {
                bishopMoves.add(new ChessMove(myPosition, checkPosition, null));
                break;
            }
        }
        return bishopMoves;
    }

    private Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {
        Set<ChessMove> bishopMoves = new HashSet<>();
        bishopMoves.addAll(bishopSubsetMoves(board, myPosition, false, false));
        bishopMoves.addAll(bishopSubsetMoves(board, myPosition, true, false));
        bishopMoves.addAll(bishopSubsetMoves(board, myPosition, false, true));
        bishopMoves.addAll(bishopSubsetMoves(board, myPosition, true, true));
        return bishopMoves;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if (type == PieceType.BISHOP) {
            return bishopMoves(board, myPosition);
        }
        else {
            throw new RuntimeException("Not implemented");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
}

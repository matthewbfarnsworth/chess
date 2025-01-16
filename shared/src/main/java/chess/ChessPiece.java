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

    private Collection<ChessMove> diagonalMoves(ChessBoard board, ChessPosition myPosition, boolean down,
                                                boolean right) {
        int checkRow = myPosition.getRow();
        int checkCol = myPosition.getColumn();
        Collection<ChessMove> bishopMoves = new HashSet<>();
        ChessPiece checkPiece;
        while (((!down && checkRow > BOARD_LOWER_LIMIT) || (down && checkRow < BOARD_UPPER_LIMIT)) &&
                ((!right && checkCol > BOARD_LOWER_LIMIT) || (right && checkCol < BOARD_UPPER_LIMIT))) {
            checkRow = down ? checkRow + 1: checkRow - 1;
            checkCol = right ?  checkCol + 1: checkCol - 1;
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

    private Collection<ChessMove> horizontalVerticalMoves(ChessBoard board, ChessPosition myPosition, boolean vertical,
                                                          boolean downRight) {
        int checkRow = myPosition.getRow();
        int checkCol = myPosition.getColumn();
        Collection<ChessMove> horizontalVerticalMoves = new HashSet<>();
        ChessPiece checkPiece;
        while ((vertical && downRight && checkRow < BOARD_UPPER_LIMIT) ||
                (vertical && !downRight && checkRow > BOARD_LOWER_LIMIT) ||
                (!vertical && downRight && checkCol < BOARD_UPPER_LIMIT) ||
                (!vertical && !downRight && checkCol > BOARD_LOWER_LIMIT)) {
            if (vertical) {
                checkRow = downRight ? checkRow + 1: checkRow - 1;
            }
            else {
                checkCol = downRight ?  checkCol + 1: checkCol - 1;
            }
            ChessPosition checkPosition = new ChessPosition(checkRow, checkCol);
            checkPiece = board.getPiece(checkPosition);
            if (checkPiece == null) {
                horizontalVerticalMoves.add(new ChessMove(myPosition, checkPosition, null));
            }
            else if (checkPiece.getTeamColor() == pieceColor) {
                break;
            }
            else {
                horizontalVerticalMoves.add(new ChessMove(myPosition, checkPosition, null));
                break;
            }
        }
        return horizontalVerticalMoves;
    }

    private Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> bishopMoves = new HashSet<>();
        bishopMoves.addAll(diagonalMoves(board, myPosition, false, false));
        bishopMoves.addAll(diagonalMoves(board, myPosition, true, false));
        bishopMoves.addAll(diagonalMoves(board, myPosition, false, true));
        bishopMoves.addAll(diagonalMoves(board, myPosition, true, true));
        return bishopMoves;
    }

    private Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> queenMoves = new HashSet<>();
        queenMoves.addAll(diagonalMoves(board, myPosition, false, false));
        queenMoves.addAll(horizontalVerticalMoves(board, myPosition, true, false));
        queenMoves.addAll(diagonalMoves(board, myPosition, true, false));
        queenMoves.addAll(horizontalVerticalMoves(board, myPosition, false, true));
        queenMoves.addAll(diagonalMoves(board, myPosition, false, true));
        queenMoves.addAll(horizontalVerticalMoves(board, myPosition, true, true));
        queenMoves.addAll(diagonalMoves(board, myPosition, true, true));
        queenMoves.addAll(horizontalVerticalMoves(board, myPosition, false, false));
        return queenMoves;
    }

    private Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> rookMoves = new HashSet<>();
        rookMoves.addAll(horizontalVerticalMoves(board, myPosition, true, false));
        rookMoves.addAll(horizontalVerticalMoves(board, myPosition, false, true));
        rookMoves.addAll(horizontalVerticalMoves(board, myPosition, true, true));
        rookMoves.addAll(horizontalVerticalMoves(board, myPosition, false, false));
        return rookMoves;
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
        if (type == PieceType.QUEEN) {
            return queenMoves(board, myPosition);
        }
        else if (type == PieceType.ROOK) {
            return rookMoves(board, myPosition);
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

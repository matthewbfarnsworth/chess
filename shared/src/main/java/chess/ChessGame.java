package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    static final int BOARD_SIZE = 8;
    private TeamColor teamTurn = TeamColor.WHITE;
    private ChessBoard board = new ChessBoard();

    public ChessGame() {
        board.resetBoard();
    }

    public ChessGame(ChessGame chessGame) {
        this.teamTurn = chessGame.teamTurn;
        this.board = new ChessBoard(chessGame.board);
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return teamTurn == chessGame.teamTurn && Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamTurn, board);
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    private boolean moveWillNotBeCheck(ChessMove checkMove) {
        ChessPiece checkPiece = board.getPiece(checkMove.getStartPosition());
        if (checkPiece != null) {
            ChessGame checkGame = new ChessGame(this);
            checkGame.changeBoard(checkMove);
            return !checkGame.isInCheck(checkPiece.getTeamColor());
        }
        return true;
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece chessPiece = board.getPiece(startPosition);
        if (chessPiece == null) {
            return null;
        }
        else {
            Collection<ChessMove> proposedMoves = chessPiece.pieceMoves(board, startPosition);
            Collection<ChessMove> validMoves = new HashSet<>();
            for (ChessMove move : proposedMoves) {
                if (moveWillNotBeCheck(move)) {
                    validMoves.add(move);
                }
            }
            return validMoves;
        }
    }

    private void changeBoard(ChessMove move) {
        if (move.getPromotionPiece() == null) {
            board.addPiece(move.getEndPosition(), board.getPiece(move.getStartPosition()));
        }
        else {
            board.addPiece(move.getEndPosition(), new ChessPiece(getTeamTurn(), move.getPromotionPiece()));
        }
        board.addPiece(move.getStartPosition(), null);
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());
        if (validMoves == null) {
            throw new InvalidMoveException("Can not make move on null piece");
        }
        else if (board.getPiece(move.getStartPosition()).getTeamColor() != getTeamTurn()) {
            throw new InvalidMoveException("Can not make move on wrong team");
        }
        else if (!validMoves.contains(move)) {
            throw new InvalidMoveException("Invalid move");
        }
        else {
            changeBoard(move);
        }
        setTeamTurn(getTeamTurn() == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE);
    }

    private boolean pieceCanCaptureKing(TeamColor teamColor, ChessPosition checkPosition, ChessPiece gamePiece) {
        if (gamePiece != null && gamePiece.getTeamColor() != teamColor) {
            Collection<ChessMove> moves = gamePiece.pieceMoves(board, checkPosition);
            for (ChessMove move : moves) {
                ChessPiece checkPiece = board.getPiece(move.getEndPosition());
                if (checkPiece != null && checkPiece.getPieceType() == ChessPiece.PieceType.KING) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        for (int checkRow = 1; checkRow <= BOARD_SIZE; checkRow++) {
            for (int checkCol = 1; checkCol <= BOARD_SIZE; checkCol++) {
                ChessPosition checkPosition = new ChessPosition(checkRow, checkCol);
                ChessPiece gamePiece = getBoard().getPiece(checkPosition);
                if (pieceCanCaptureKing(teamColor, checkPosition, gamePiece)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean existValidMoves(TeamColor teamColor) {
        for (int checkRow = 1; checkRow <= BOARD_SIZE; checkRow++) {
            for (int checkCol = 1; checkCol <= BOARD_SIZE; checkCol++) {
                ChessPosition checkPosition = new ChessPosition(checkRow, checkCol);
                ChessPiece gamePiece = getBoard().getPiece(checkPosition);
                if (gamePiece != null && gamePiece.getTeamColor() == teamColor &&
                        !validMoves(checkPosition).isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            return existValidMoves(teamColor);
        }
        else {
            return false;
        }
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            return false;
        }
        else {
            return existValidMoves(teamColor);
        }
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}

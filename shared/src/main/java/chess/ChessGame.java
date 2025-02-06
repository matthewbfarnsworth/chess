package chess;

import java.util.Collection;
import java.util.HashSet;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private final int BOARD_SIZE = 8;
    private TeamColor teamTurn = TeamColor.WHITE;
    private ChessBoard board = new ChessBoard();

    private boolean blackCanTryQueenSideCastle = true;
    private boolean blackCanTryKingSideCastle = true;
    private boolean whiteCanTryQueenSideCastle = true;
    private boolean whiteCanTryKingSideCastle = true;

    public ChessGame() {
        board.resetBoard();
    }

    public ChessGame(ChessGame chessGame) {
        this.teamTurn = chessGame.teamTurn;
        this.board = new ChessBoard(chessGame.board);
        this.blackCanTryQueenSideCastle = chessGame.blackCanTryQueenSideCastle;
        this.blackCanTryKingSideCastle = chessGame.blackCanTryKingSideCastle;
        this.whiteCanTryQueenSideCastle = chessGame.whiteCanTryQueenSideCastle;
        this.whiteCanTryKingSideCastle = chessGame.whiteCanTryKingSideCastle;
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

    private Collection<ChessMove> validCastleMoveOnSide(TeamColor teamColor, boolean kingSide) {
        Collection<ChessMove> castleMoves = new HashSet<>();
        int checkRow = (teamColor == TeamColor.WHITE) ? 1 : 8;
        int checkRightOneCol = kingSide ? 6 : 4;
        int checkRightTwoCol = kingSide ? 7 : 3;
        int rookCol = kingSide ? 8 : 1;
        ChessPosition kingPosition = new ChessPosition(checkRow, 5);
        ChessPosition rookPosition = new ChessPosition(checkRow, rookCol);
        ChessPiece kingPiece = board.getPiece(kingPosition);
        ChessPiece rookPiece = board.getPiece(rookPosition);
        if ((kingPiece != null) && (kingPiece.getTeamColor() == teamColor) &&
                (kingPiece.getPieceType() == ChessPiece.PieceType.KING) && (rookPiece != null) &&
                (rookPiece.getTeamColor() == teamColor) && (rookPiece.getPieceType() == ChessPiece.PieceType.ROOK)) {
            if ((teamColor == TeamColor.WHITE && kingSide && whiteCanTryKingSideCastle) ||
                    (teamColor == TeamColor.WHITE && !kingSide && whiteCanTryQueenSideCastle) ||
                    (teamColor == TeamColor.BLACK && kingSide && blackCanTryKingSideCastle) ||
                    (teamColor == TeamColor.BLACK && !kingSide && blackCanTryQueenSideCastle)) {
                ChessPosition checkPosRightOne = new ChessPosition(checkRow, checkRightOneCol);
                ChessPosition checkPosRightTwo = new ChessPosition(checkRow, checkRightTwoCol);
                ChessMove checkMoveRightOne = new ChessMove(kingPosition, checkPosRightOne, null);
                ChessMove checkMoveRightTwo = new ChessMove(kingPosition, checkPosRightTwo, null);
                if (board.getPiece(checkPosRightOne) == null && board.getPiece(checkPosRightTwo) == null &&
                        moveWillNotBeCheck(checkMoveRightOne) && moveWillNotBeCheck(checkMoveRightTwo)) {
                    castleMoves.add(checkMoveRightTwo);
                }
            }
        }
        return castleMoves;
    }

    private Collection<ChessMove> validCastleMoves(TeamColor teamColor) {
        Collection<ChessMove> castleMoves = new HashSet<>();
        if (!isInCheck(teamColor)) {
            castleMoves.addAll(validCastleMoveOnSide(teamColor, true));
            castleMoves.addAll(validCastleMoveOnSide(teamColor, false));
        }
        return castleMoves;
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
            // only add moves which would not put the king in check
            for (ChessMove move : proposedMoves) {
                if (moveWillNotBeCheck(move)) {
                    validMoves.add(move);
                }
            }
            // castling moves
            validMoves.addAll(validCastleMoves(chessPiece.getTeamColor()));
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

    private void setCanTryCastle(ChessMove move) {
        ChessPosition startPosition = move.getStartPosition();
        if (startPosition.equals(new ChessPosition(1, 5))) {
            whiteCanTryKingSideCastle = false;
            whiteCanTryQueenSideCastle = false;
        }
        else if (startPosition.equals(new ChessPosition(8, 5))) {
            blackCanTryKingSideCastle = false;
            blackCanTryQueenSideCastle = false;
        }
        else if (startPosition.equals(new ChessPosition(1, 1))) whiteCanTryQueenSideCastle = false;
        else if (startPosition.equals(new ChessPosition(1, 8))) whiteCanTryKingSideCastle = false;
        else if (startPosition.equals(new ChessPosition(8, 1))) blackCanTryQueenSideCastle = false;
        else if (startPosition.equals(new ChessPosition(8, 8))) blackCanTryKingSideCastle = false;
    }

    private ChessMove castleRookMove(TeamColor teamColor, ChessMove move) {
        int checkRow = (teamColor == TeamColor.WHITE) ? 1 : 8;
        ChessPosition kingPosition = new ChessPosition(checkRow, 5);
        if (move.equals(new ChessMove(kingPosition, new ChessPosition(checkRow, 7), null))) {
            return new ChessMove(new ChessPosition(checkRow, 8), new ChessPosition(checkRow, 6), null);
        }
        else if (move.equals(new ChessMove(kingPosition, new ChessPosition(checkRow, 3), null ))) {
            return new ChessMove(new ChessPosition(checkRow, 1), new ChessPosition(checkRow, 4), null);
        }
        else {
            return null;
        }
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
            ChessMove castleRookMove = castleRookMove(getTeamTurn(), move);
            if (castleRookMove != null) {
                changeBoard(castleRookMove);
            }
            setCanTryCastle(move);
        }
        setTeamTurn(getTeamTurn() == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE);
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
                if (gamePiece != null && gamePiece.getTeamColor() != teamColor) {
                    Collection<ChessMove> moves = gamePiece.pieceMoves(board, checkPosition);
                    for (ChessMove move : moves) {
                        ChessPiece checkPiece = board.getPiece(move.getEndPosition());
                        if (checkPiece != null && checkPiece.getPieceType() == ChessPiece.PieceType.KING) {
                            return true;
                        }
                    }
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
        else return false;
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
        else return existValidMoves(teamColor);
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
        whiteCanTryKingSideCastle = true;
        whiteCanTryQueenSideCastle = true;
        blackCanTryKingSideCastle = true;
        blackCanTryQueenSideCastle = true;
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

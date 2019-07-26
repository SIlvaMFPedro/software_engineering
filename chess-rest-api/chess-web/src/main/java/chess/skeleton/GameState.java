/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.skeleton;

import chess.skeleton.pieces.Bishop;
import chess.skeleton.pieces.King;
import chess.skeleton.pieces.Knight;
import chess.skeleton.pieces.Pawn;
import chess.skeleton.pieces.Piece;
import chess.skeleton.pieces.Queen;
import chess.skeleton.pieces.Rook;
import java.util.Collections;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author pedro
 */

/**
 * Class that represents the current state of the game.  Basically, what pieces are in which positions on the
 * board.
 */
public class GameState {
   public static final int MIN_ROW = 1;
    public static final int MAX_ROW = 8;
    public static final char MIN_COLUMN = 'a';
    public static final char MAX_COLUMN = 'h';
    private Player currentPlayer = Player.White;
    private final Map<Position, Piece> positionToPieceMap;
    private final Map<Piece, Position> pieceToPositionMap;
    private Move lastMove;

    public GameState() {
        this.positionToPieceMap = new HashMap<Position, Piece>();
        this.pieceToPositionMap = new HashMap<Piece, Position>();
    }

    public GameState(Map<Position, Piece> piecesToPlace) {
        this.positionToPieceMap = new HashMap<Position, Piece>();
        this.pieceToPositionMap = new HashMap<Piece, Position>();
        for (Map.Entry<Position, Piece> entry : piecesToPlace.entrySet()) {
            this.placePiece(entry.getValue(), entry.getKey());
        }
    }

    public GameState(GameState other) {
        Position copied;
        this.currentPlayer = other.currentPlayer;
        this.positionToPieceMap = new HashMap<Position, Piece>();
        for (Position position : other.positionToPieceMap.keySet()) {
            copied = new Position(position);
            Piece piece = other.positionToPieceMap.get((Object)position);
            this.positionToPieceMap.put(copied, piece);
        }
        this.pieceToPositionMap = new HashMap<Piece, Position>(other.pieceToPositionMap);
        for (Piece piece : other.pieceToPositionMap.keySet()) {
            copied = new Position(other.pieceToPositionMap.get((Object)piece));
            this.pieceToPositionMap.put(piece, copied);
        }
        if (other.lastMove != null) {
            this.lastMove = new Move(other.lastMove);
        }
    }

    public void reset() {
        this.placePiece((Piece)new Rook(Player.White), new Position("a1"));
        this.placePiece((Piece)new Knight(Player.White), new Position("b1"));
        this.placePiece((Piece)new Bishop(Player.White), new Position("c1"));
        this.placePiece((Piece)new Queen(Player.White), new Position("d1"));
        this.placePiece((Piece)new King(Player.White), new Position("e1"));
        this.placePiece((Piece)new Bishop(Player.White), new Position("f1"));
        this.placePiece((Piece)new Knight(Player.White), new Position("g1"));
        this.placePiece((Piece)new Rook(Player.White), new Position("h1"));
        this.placePiece((Piece)new Pawn(Player.White), new Position("a2"));
        this.placePiece((Piece)new Pawn(Player.White), new Position("b2"));
        this.placePiece((Piece)new Pawn(Player.White), new Position("c2"));
        this.placePiece((Piece)new Pawn(Player.White), new Position("d2"));
        this.placePiece((Piece)new Pawn(Player.White), new Position("e2"));
        this.placePiece((Piece)new Pawn(Player.White), new Position("f2"));
        this.placePiece((Piece)new Pawn(Player.White), new Position("g2"));
        this.placePiece((Piece)new Pawn(Player.White), new Position("h2"));
        this.placePiece((Piece)new Rook(Player.Black), new Position("a8"));
        this.placePiece((Piece)new Knight(Player.Black), new Position("b8"));
        this.placePiece((Piece)new Bishop(Player.Black), new Position("c8"));
        this.placePiece((Piece)new Queen(Player.Black), new Position("d8"));
        this.placePiece((Piece)new King(Player.Black), new Position("e8"));
        this.placePiece((Piece)new Bishop(Player.Black), new Position("f8"));
        this.placePiece((Piece)new Knight(Player.Black), new Position("g8"));
        this.placePiece((Piece)new Rook(Player.Black), new Position("h8"));
        this.placePiece((Piece)new Pawn(Player.Black), new Position("a7"));
        this.placePiece((Piece)new Pawn(Player.Black), new Position("b7"));
        this.placePiece((Piece)new Pawn(Player.Black), new Position("c7"));
        this.placePiece((Piece)new Pawn(Player.Black), new Position("d7"));
        this.placePiece((Piece)new Pawn(Player.Black), new Position("e7"));
        this.placePiece((Piece)new Pawn(Player.Black), new Position("f7"));
        this.placePiece((Piece)new Pawn(Player.Black), new Position("g7"));
        this.placePiece((Piece)new Pawn(Player.Black), new Position("h7"));
    }

    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void toggleCurrentPlayer() {
        this.currentPlayer = this.currentPlayer.other();
    }

    public Piece placePiece(Piece piece, String colrow) {
        Position position = new Position(colrow);
        return this.placePiece(piece, position);
    }

    public Piece placePiece(Piece piece, Position position) {
        Piece replaced;
        Position originalPosition = this.getPositionOf(piece);
        if (originalPosition != null) {
            this.positionToPieceMap.remove((Object)originalPosition);
        }
        if ((replaced = this.positionToPieceMap.put(position, piece)) != null) {
            this.pieceToPositionMap.put(replaced, Position.OFF_BOARD);
        }
        this.pieceToPositionMap.put(piece, position);
        return replaced;
    }

    public Piece getPieceAt(String colrow) {
        Position position = new Position(colrow);
        return this.getPieceAt(position);
    }

   @SuppressWarnings("element-type-mismatch")
    public Piece getPieceAt(Position position) {
        return this.positionToPieceMap.get((Object)position);
    }

   @SuppressWarnings("element-type-mismatch")
    private Position getPositionOf(Piece piece) {
        if (piece == null) {
            return null;
        }
        return this.pieceToPositionMap.get((Object)piece);
    }

    public Set<Piece> getPiecesOnBoard(Player player) {
        @SuppressWarnings("Convert2Diamond")
        HashSet<Piece> pieces = new HashSet<Piece>(this.positionToPieceMap.values());
        Set<Piece> playerPieces = this.filterPieces(pieces, player);
        return Collections.unmodifiableSet(playerPieces);
    }

    public Map<Position, Piece> getPiecePositions() {
        return Collections.unmodifiableMap(this.positionToPieceMap);
    }

    private boolean isCoveredBy(Position position, Player player) {
        Set<Piece> otherPieces = this.getPiecesOnBoard(player);
        for (Piece piece : otherPieces) {
            Position pieceOrigin;
            pieceOrigin = this.getPositionOf(piece);
            Set possible = piece.getNextPositions(this, pieceOrigin);
            if (!possible.contains((Object)position)) continue;
            return true;
        }
        return false;
    }

    public Map<Piece, Set<Move>> findPossibleMoves() {
        HashMap<Piece, Set<Move>> moveMap = new HashMap<Piece, Set<Move>>();
        Set<Piece> pieces = this.getPiecesOnBoard(this.currentPlayer);
        for (Piece piece : pieces) {
            moveMap.put(piece, this.findValidMovesFor(piece));
        }
        return moveMap;
    }

    public Set<Move> findValidMovesFor(Piece piece) {
        HashSet<Move> moves = new HashSet<Move>();
        Position start = this.getPositionOf(piece);
        Set<Position> possible = piece.getNextPositions(this, start);
        for (Position position : possible) {
            Move move = new Move(start, position);
            if (this.wouldBeInCheckAfter(move)) continue;
            moves.add(move);
        }
        return Collections.unmodifiableSet(moves);
    }

    public boolean makeMove(String moveStr) {
        Move move = new Move(moveStr);
        return this.makeMove(move, true);
    }

    public boolean makeMove(Move move, boolean validate) throws InvalidMoveException {
        boolean inCheck = this.doMakeMove(move, validate);
        if (inCheck) {
            this.revert(move);
            return false;
        }
        this.currentPlayer = this.currentPlayer.other();
        System.out.println("Current Player: " + this.currentPlayer);
        return true;
    }

    private boolean doMakeMove(Move move, boolean validate) {
        Position origin = move.getOrigin();
        Piece movingPiece = this.getPieceAt(origin);
        if (movingPiece == null || !this.currentPlayer.equals((Object)movingPiece.getPlayer())) {
            throw new InvalidMoveException(move);
        }
        Set validPositions = movingPiece.getNextPositions(this, origin);
        Position destination = move.getDestination();
        if (validate && !validPositions.contains((Object)destination)) {
            throw new InvalidMoveException(move);
        }
        Piece replaced = this.placePiece(movingPiece, destination);
        move.record(movingPiece, replaced);
        this.lastMove = move;
        return this.isInCheck();
    }

    void revert(Move move) {
        if (move == null || !move.equals((Object)this.lastMove)) {
            throw new IllegalArgumentException("Cannot revert: " + (Object)move);
        }
        Piece replaced = move.getReplacedPiece();
        if (replaced != null) {
            this.placePiece(replaced, move.getDestination());
        }
        this.placePiece(move.getMovedPiece(), move.getOrigin());
    }

    public boolean isInCheck() {
        Position kingPosition = this.findCurrentKing();
        Piece king = this.getPieceAt(kingPosition);
        return king != null && this.isCoveredBy(kingPosition, this.currentPlayer.other());
    }

    public boolean isGameOver() {
        if (this.isInCheck()) {
            Map<Piece, Set<Move>> moveMap = this.findPossibleMoves();
            HashSet<Move> moves = new HashSet<Move>();
            for (Set<Move> current : moveMap.values()) {
                moves.addAll(current);
            }
            for (Move move : moves) {
                boolean inCheck = this.wouldBeInCheckAfter(move);
                if (inCheck) continue;
                return false;
            }
            return true;
        }
        return false;
    }

    private Position findCurrentKing() {
        Set<Piece> pieces = this.getPiecesOnBoard(this.currentPlayer);
        King king = null;
        for (Piece piece : pieces) {
            if (!(piece instanceof King)) continue;
            king = (King)piece;
            break;
        }
        return this.getPositionOf((Piece)king);
    }

    private Set<Piece> filterPieces(Set<Piece> pieces, Player player) {
        HashSet<Piece> playerPieces = new HashSet<Piece>();
        for (Piece piece : pieces) {
            if (!piece.getPlayer().equals((Object)player)) continue;
            playerPieces.add(piece);
        }
        return playerPieces;
    }

    private boolean wouldBeInCheckAfter(Move move) {
        boolean putsInCheck = this.doMakeMove(move, true);
        this.revert(move);
        return putsInCheck;
    }
}

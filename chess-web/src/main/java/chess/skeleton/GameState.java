/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.skeleton;

import chess.consumer.ChessConsumer;
import chess.skeleton.pieces.Bishop;
import chess.skeleton.pieces.King;
import chess.skeleton.pieces.Knight;
import chess.skeleton.pieces.Pawn;
import chess.skeleton.pieces.Piece;
import chess.skeleton.pieces.Queen;
import chess.skeleton.pieces.Rook;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

/**
 *
 * @author pedro
 */

/**
 * Class that represents the current state of the game.  Basically, what pieces are in which positions on the
 * board.
 */
public class GameState
{
  public static final int MIN_ROW = 1;
  public static final int MAX_ROW = 8;
  public static final char MIN_COLUMN = 'a';
  public static final char MAX_COLUMN = 'h';
  private Player currentPlayer = Player.White;
  private final Map<Position, Piece> positionToPieceMap;
  private final Map<Piece, Position> pieceToPositionMap;
  private Move lastMove;
  
  public GameState()
  {
    this.positionToPieceMap = new HashMap();
    this.pieceToPositionMap = new HashMap();
  }
  
  public GameState(GameState other)
  {
    this.currentPlayer = other.currentPlayer;
    
    this.positionToPieceMap = new HashMap();
    for (Position position : other.positionToPieceMap.keySet())
    {
      Position copied = new Position(position);
      Piece piece = (Piece)other.positionToPieceMap.get(position);
      this.positionToPieceMap.put(copied, piece);
    }
    this.pieceToPositionMap = new HashMap(other.pieceToPositionMap);
    for (Piece piece : other.pieceToPositionMap.keySet())
    {
      Position copied = new Position((Position)other.pieceToPositionMap.get(piece));
      this.pieceToPositionMap.put(piece, copied);
    }
    if (other.lastMove != null) {
      this.lastMove = new Move(other.lastMove);
    }
  }
  
  public void reset()
  {
    placePiece(new Rook(Player.White), new Position("a1"));
    placePiece(new Knight(Player.White), new Position("b1"));
    placePiece(new Bishop(Player.White), new Position("c1"));
    placePiece(new Queen(Player.White), new Position("d1"));
    placePiece(new King(Player.White), new Position("e1"));
    placePiece(new Bishop(Player.White), new Position("f1"));
    placePiece(new Knight(Player.White), new Position("g1"));
    placePiece(new Rook(Player.White), new Position("h1"));
    placePiece(new Pawn(Player.White), new Position("a2"));
    placePiece(new Pawn(Player.White), new Position("b2"));
    placePiece(new Pawn(Player.White), new Position("c2"));
    placePiece(new Pawn(Player.White), new Position("d2"));
    placePiece(new Pawn(Player.White), new Position("e2"));
    placePiece(new Pawn(Player.White), new Position("f2"));
    placePiece(new Pawn(Player.White), new Position("g2"));
    placePiece(new Pawn(Player.White), new Position("h2"));
    
    placePiece(new Rook(Player.Black), new Position("a8"));
    placePiece(new Knight(Player.Black), new Position("b8"));
    placePiece(new Bishop(Player.Black), new Position("c8"));
    placePiece(new Queen(Player.Black), new Position("d8"));
    placePiece(new King(Player.Black), new Position("e8"));
    placePiece(new Bishop(Player.Black), new Position("f8"));
    placePiece(new Knight(Player.Black), new Position("g8"));
    placePiece(new Rook(Player.Black), new Position("h8"));
    placePiece(new Pawn(Player.Black), new Position("a7"));
    placePiece(new Pawn(Player.Black), new Position("b7"));
    placePiece(new Pawn(Player.Black), new Position("c7"));
    placePiece(new Pawn(Player.Black), new Position("d7"));
    placePiece(new Pawn(Player.Black), new Position("e7"));
    placePiece(new Pawn(Player.Black), new Position("f7"));
    placePiece(new Pawn(Player.Black), new Position("g7"));
    placePiece(new Pawn(Player.Black), new Position("h7"));
  }
  
  public Player getCurrentPlayer()
  {
    return this.currentPlayer;
  }
  
  public void setCurrentPlayer(Player currentPlayer)
  {
    this.currentPlayer = currentPlayer;
  }
  
  public void toggleCurrentPlayer()
  {
    this.currentPlayer = this.currentPlayer.other();
  }
  
  public Piece placePiece(Piece piece, String colrow)
  {
    Position position = new Position(colrow);
    return placePiece(piece, position);
  }
  
  public Piece placePiece(Piece piece, Position position)
  {
    Position originalPosition = getPositionOf(piece);
    if (originalPosition != null) {
      this.positionToPieceMap.remove(originalPosition);
    }
    Piece replaced = (Piece)this.positionToPieceMap.put(position, piece);
    if (replaced != null) {
      this.pieceToPositionMap.put(replaced, Position.OFF_BOARD);
    }
    this.pieceToPositionMap.put(piece, position);
    
    return replaced;
  }
  
  public Piece getPieceAt(String colrow)
  {
    Position position = new Position(colrow);
    return getPieceAt(position);
  }
  
  public Piece getPieceAt(Position position)
  {
    return (Piece)this.positionToPieceMap.get(position);
  }
  
  private Position getPositionOf(Piece piece)
  {
    if (piece == null) {
      return null;
    }
    return (Position)this.pieceToPositionMap.get(piece);
  }
  
  public Set<Piece> getPiecesOnBoard(Player player)
  {
    HashSet<Piece> pieces = new HashSet(this.positionToPieceMap.values());
    Set<Piece> playerPieces = filterPieces(pieces, player);
    return Collections.unmodifiableSet(playerPieces);
  }
  
  public Map<Position, Piece> getPiecePositions()
  {
    return Collections.unmodifiableMap(this.positionToPieceMap);
  }
  
  private boolean isCoveredBy(Position position, Player player)
  {
    Set<Piece> otherPieces = getPiecesOnBoard(player);
    for (Piece piece : otherPieces)
    {
      Position pieceOrigin = getPositionOf(piece);
      Set<Position> possible = piece.getNextPositions(this, pieceOrigin);
      if (possible.contains(position)) {
        return true;
      }
    }
    return false;
  }
  
  public Map<Piece, Set<Move>> findPossibleMoves()
  {
    Map<Piece, Set<Move>> moveMap = new HashMap();
    
    Set<Piece> pieces = getPiecesOnBoard(this.currentPlayer);
    for (Piece piece : pieces) {
      moveMap.put(piece, findValidMovesFor(piece));
    }
    return moveMap;
  }
  
  public Set<Move> findValidMovesFor(Piece piece)
  {
    Set<Move> moves = new HashSet();
    Position start = getPositionOf(piece);
    
    Set<Position> possible = piece.getNextPositions(this, start);
    for (Position position : possible)
    {
      Move move = new Move(start, position);
      if (!wouldBeInCheckAfter(move)) {
        moves.add(move);
      }
    }
    return Collections.unmodifiableSet(moves);
  }
  
  public boolean makeMove(String moveStr)
  {
    Move move = new Move(moveStr);
    return makeMove(move, true);
  }
  
  public boolean makeMove(Move move, boolean validate)
    throws InvalidMoveException
  {
    boolean inCheck = doMakeMove(move, validate);
    if (inCheck)
    {
      revert(move);
      return false;
    }
    this.currentPlayer = this.currentPlayer.other();
    return true;
  }
  
  private boolean doMakeMove(Move move, boolean validate)
  {
    Position origin = move.getOrigin();
    Piece movingPiece = getPieceAt(origin);
    if ((movingPiece == null) || (!this.currentPlayer.equals(movingPiece.getPlayer()))) {
      throw new InvalidMoveException(move);
    }
    Set<Position> validPositions = movingPiece.getNextPositions(this, origin);
    Position destination = move.getDestination();
    if ((validate) && (!validPositions.contains(destination))) {
      throw new InvalidMoveException(move);
    }
    Piece replaced = placePiece(movingPiece, destination);
    move.record(movingPiece, replaced);
    this.lastMove = move;
    
    return isInCheck();
  }
  
  void revert(Move move)
  {
    if ((move == null) || (!move.equals(this.lastMove))) {
      throw new IllegalArgumentException("Cannot revert: " + move);
    }
    Piece replaced = move.getReplacedPiece();
    if (replaced != null) {
      placePiece(replaced, move.getDestination());
    }
    placePiece(move.getMovedPiece(), move.getOrigin());
  }
  
  public boolean isInCheck()
  {
    Position kingPosition = findCurrentKing();
    Piece king = getPieceAt(kingPosition);
    return (king != null) && (isCoveredBy(kingPosition, this.currentPlayer.other()));
  }
  
  public boolean isGameOver()
  {
    if (isInCheck())
    {
      Map<Piece, Set<Move>> moveMap = findPossibleMoves();
      
      Set<Move> moves = new HashSet();
      for (Set<Move> current : moveMap.values()) {
        moves.addAll(current);
      }
      for (Move move : moves)
      {
        boolean inCheck = wouldBeInCheckAfter(move);
        if (!inCheck) {
          return false;
        }
      }
      return true;
    }
    return false;
  }
  
  private Position findCurrentKing()
  {
    Set<Piece> pieces = getPiecesOnBoard(this.currentPlayer);
    
    King king = null;
    for (Piece piece : pieces) {
      if ((piece instanceof King))
      {
        king = (King)piece;
        break;
      }
    }
    return getPositionOf(king);
  }
  
  private Set<Piece> filterPieces(Set<Piece> pieces, Player player)
  {
    HashSet<Piece> playerPieces = new HashSet();
    for (Piece piece : pieces) {
      if (piece.getPlayer().equals(player)) {
        playerPieces.add(piece);
      }
    }
    return playerPieces;
  }
  
  private boolean wouldBeInCheckAfter(Move move)
  {
    boolean putsInCheck = doMakeMove(move, true);
    revert(move);
    
    return putsInCheck;
  }
}

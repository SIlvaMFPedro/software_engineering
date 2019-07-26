/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.skeleton.pieces;

import chess.skeleton.GameState;
import chess.skeleton.Player;
import chess.skeleton.Position;
import chess.skeleton.pieces.positions.PiecePositionGenerator;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author pedro
 */
/**
 * A base class for chess pieces
 */
public abstract class Piece
{
  final Player player;
  
  Piece(Player player)
  {
    this.player = player;
  }
  
  public final Player getPlayer()
  {
    return this.player;
  }
  
  <T> Set<T> arrayToSet(T[] objs)
  {
    return new HashSet(Arrays.asList(objs));
  }
  
  public final char getIdentifier()
  {
    char id = getIdentifierCharacter();
    if (this.player.equals(Player.Black)) {
      return Character.toUpperCase(id);
    }
    return Character.toLowerCase(id);
  }
  
  public final boolean canMove(GameState gameState, Position origin, Position destination)
  {
    Set<Position> possible = getNextPositions(gameState, origin);
    return possible.contains(destination);
  }
  
  public Set<Position> getNextPositions(GameState gameState, Position origin)
  {
    Set<Position> positions = new HashSet();
    for (PiecePositionGenerator generator : getPositionGenerators(gameState)) {
      positions.addAll(generator.generate(origin));
    }
    return positions;
  }
  
  protected abstract Set<PiecePositionGenerator> getPositionGenerators(GameState paramGameState);
  
  public abstract char getIdentifierCharacter();
  
  @Override
  public String toString()
  {
    return this.player + " " + getIdentifier();
  }
}

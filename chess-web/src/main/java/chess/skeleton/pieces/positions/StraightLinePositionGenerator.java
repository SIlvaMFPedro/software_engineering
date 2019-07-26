/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.skeleton.pieces.positions;

import chess.skeleton.Direction;
import chess.skeleton.GameState;
import chess.skeleton.Player;
import chess.skeleton.Position;
import chess.skeleton.pieces.Piece;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author pedro
 */
public abstract class StraightLinePositionGenerator extends PiecePositionGenerator
{
  private final List<Direction> directions;
  private Integer maxNumSteps;
  
  StraightLinePositionGenerator(Player player, GameState gameState, Direction... directions)
  {
    super(player, gameState);
    this.directions = Arrays.asList(directions);
    this.maxNumSteps = null;
  }
  
  public Set<Position> generate(Position origin)
  {
    Set<Position> positions = new HashSet();
    for (Direction direction : this.directions) {
      positions.addAll(generate(origin, direction));
    }
    return positions;
  }
  
  void setMaxNumSteps(Integer maxNumSteps)
  {
    this.maxNumSteps = maxNumSteps;
  }
  
  Collection<Position> generate(Position origin, Direction direction)
  {
    Set<Position> positions = new HashSet();
    
    Position current = origin.step(direction);
    
    int numSteps = 0;
    while ((current != null) && (
      (this.maxNumSteps == null) || (numSteps < this.maxNumSteps.intValue())))
    {
      Piece pieceAtCurrent = this.state.getPieceAt(current);
      if (pieceAtCurrent == null)
      {
        positions.add(current);
        
        current = current.step(direction);
      }
      else
      {
        if (!this.player.equals(pieceAtCurrent.getPlayer())) {
          positions.add(current);
        }
        current = null;
      }
      numSteps++;
    }
    return positions;
  }
}

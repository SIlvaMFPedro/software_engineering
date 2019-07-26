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
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author pedro
 */
public class KnightPositionGenerator extends PiecePositionGenerator
{
  public KnightPositionGenerator(Player player, GameState gameState)
  {
    super(player, gameState);
  }
  

  public Set<Position> generate(Position origin)
  {
    Set<Position> possible = new HashSet();
    
    possible.addAll(findPossiblePositions(origin, Direction.North, Direction.East, Direction.West));
    possible.addAll(findPossiblePositions(origin, Direction.South, Direction.East, Direction.West));
    possible.addAll(findPossiblePositions(origin, Direction.East, Direction.North, Direction.South));
    possible.addAll(findPossiblePositions(origin, Direction.West, Direction.North, Direction.South));
    
    return possible;
  }
  
  private Set<Position> findPossiblePositions(Position origin, Direction twoStep, Direction left, Direction right) {
    Set<Position> positions = new HashSet();
    Position step = origin.step(twoStep);
    if (step != null) {
      step = step.step(twoStep);
      if (step != null) {
        addIfGoodPosition(positions, new Position[] { step.step(left), step.step(right) });
      }
    }
    return positions;
  }
  
  private void addIfGoodPosition(Set<Position> set, Position... positions){
    for (Position position : positions) {
      if ((position != null) && (!isOccupied(position, player))) {
        set.add(position);
      }
    }
  }
}
  
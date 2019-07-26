/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.skeleton.pieces.positions;

import chess.skeleton.Direction;
import chess.skeleton.GameState;
import chess.skeleton.Player;



/**
 *
 * @author pedro
 */
public class KingPositionGenerator extends StraightLinePositionGenerator{
  @SuppressWarnings({"UnnecessaryBoxing", "OverridableMethodCallInConstructor"})
  public KingPositionGenerator(Player player, GameState gameState)
  {
    super(player, gameState, new Direction[] { Direction.North, Direction.NorthEast, Direction.East, Direction.SouthEast, Direction.South, Direction.SouthWest, Direction.West, Direction.NorthWest });
    
    setMaxNumSteps(Integer.valueOf(1));
  }
}
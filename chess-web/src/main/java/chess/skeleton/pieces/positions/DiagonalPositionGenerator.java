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
public class DiagonalPositionGenerator extends StraightLinePositionGenerator
{
  public DiagonalPositionGenerator(Player player, GameState state)
  {
    super(player, state, new Direction[] { Direction.NorthEast, Direction.NorthWest, Direction.SouthEast, Direction.SouthWest });
  }
}

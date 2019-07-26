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
public class PawnPositionGenerator extends PiecePositionGenerator{
    public PawnPositionGenerator(Player player, GameState gameState){
        super(player, gameState);
    }
  

    public Set<Position> generate(Position origin)
    {
      Set<Direction> attackDir = new HashSet();
      Direction walkDir; 
      if (player.equals(Player.White)) {
        walkDir = Direction.North;
        attackDir.add(Direction.NorthEast);
        attackDir.add(Direction.NorthWest);
      }
      else {
        walkDir = Direction.South;
        attackDir.add(Direction.SouthEast);
        attackDir.add(Direction.SouthWest);
      }
    
      Set<Position> positions = new HashSet();

      Position oneStep = origin.step(walkDir);
      Position twoStep; if ((oneStep != null) && (isEmpty(oneStep))) {
      positions.add(oneStep);

      if (((player.equals(Player.White)) && (origin.getRow() == 2)) || ((player.equals(Player.Black)) && (origin.getRow() == 7))) {
        twoStep = oneStep.step(walkDir);
        if ((twoStep != null) && (isEmpty(twoStep))) {
          positions.add(twoStep);
        }
      }
     }
     for (Direction dir : attackDir) {
      Position attackStep = origin.step(dir);
      if (isOccupied(attackStep, player.other())) {
        positions.add(attackStep);
      }
     }

     return positions;
  }
}

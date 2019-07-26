/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.skeleton.pieces.positions;

import chess.skeleton.GameState;
import chess.skeleton.Player;
import chess.skeleton.Position;
import chess.skeleton.pieces.Piece;
import java.util.Set;

/**
 *
 * @author pedro
 */
public abstract class PiecePositionGenerator {
    final GameState state;
    final Player player;
  
    protected PiecePositionGenerator(Player player, GameState state)
    {
      this.state = state;
      this.player = player;
    }
  
    public abstract Set<Position> generate(Position paramPosition);
  
    protected boolean isEmpty(Position position)
    {
        return !isOccupied(position, null);
    }
  

    boolean isOccupied(Position position, Player player)
    {
      Piece foundPiece = state.getPieceAt(position);

      return (foundPiece != null) && ((player == null) || (foundPiece.getPlayer().equals(player)));
    }
}

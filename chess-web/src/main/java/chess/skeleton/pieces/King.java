/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.skeleton.pieces;

import chess.skeleton.GameState;
import chess.skeleton.Player;
import chess.skeleton.pieces.positions.KingPositionGenerator;
import chess.skeleton.pieces.positions.PiecePositionGenerator;
import java.util.Set;

/**
 *
 * @author pedro
 */
public class King extends Piece
{
  public King(Player player)
  {
    super(player);
  }
  
  @Override
  public char getIdentifierCharacter()
  {
    return 'k';
  }
  
  @Override
  protected Set<PiecePositionGenerator> getPositionGenerators(GameState gameState)
  {
    return arrayToSet(new PiecePositionGenerator[] { new KingPositionGenerator(this.player, gameState) });
  }
}

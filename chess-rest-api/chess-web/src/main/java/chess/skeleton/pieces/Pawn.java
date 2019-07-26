/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.skeleton.pieces;

import chess.skeleton.GameState;
import chess.skeleton.Player;
import chess.skeleton.pieces.positions.PawnPositionGenerator;
import chess.skeleton.pieces.positions.PiecePositionGenerator;
import java.util.Set;
/**
 *
 * @author pedro
 */

/**
 * The Pawn
 */
public class Pawn extends Piece{
  public Pawn(Player player)
  {
    super(player);
  }
  
  @Override
  protected Set<PiecePositionGenerator> getPositionGenerators(GameState gameState)
  {
    return arrayToSet(new PiecePositionGenerator[] { new PawnPositionGenerator(player, gameState) });
  }
  
  @Override
  public char getIdentifierCharacter()
  {
    return 'p';
  }
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.skeleton.pieces;

import chess.skeleton.GameState;
import chess.skeleton.Player;
import chess.skeleton.pieces.positions.ColumnPositionGenerator;
import chess.skeleton.pieces.positions.DiagonalPositionGenerator;
import chess.skeleton.pieces.positions.PiecePositionGenerator;
import chess.skeleton.pieces.positions.RowPositionGenerator;
import java.util.Set;

/**
 *
 * @author pedro
 */

/**
 * The Queen
 */
public class Queen extends Piece
{
  public Queen(Player player)
  {
    super(player);
  }
  
  @Override
  protected Set<PiecePositionGenerator> getPositionGenerators(GameState gameState)
  {
    return arrayToSet(new PiecePositionGenerator[] { new RowPositionGenerator(player, gameState), new ColumnPositionGenerator(player, gameState), new DiagonalPositionGenerator(player, gameState) });
  }
  
  @Override
  public char getIdentifierCharacter()
  {
    return 'q';
  }
}
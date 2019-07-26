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
public class RowPositionGenerator extends StraightLinePositionGenerator{
     public RowPositionGenerator(Player player, GameState gameState){
        super(player, gameState, new Direction[] { Direction.East, Direction.West });
    }
}

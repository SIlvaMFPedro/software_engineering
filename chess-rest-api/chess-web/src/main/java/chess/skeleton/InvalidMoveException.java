/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.skeleton;

/**
 *
 * @author pedro
 */
public class InvalidMoveException extends RuntimeException{
  
  public InvalidMoveException(Move move) {
    this(move.getOrigin(), move.getDestination());
  }
  
  private InvalidMoveException(Position origin, Position destination) {
    super("Cannot move from " + origin + " to " + destination);
  }
  
  public InvalidMoveException() {
    super("Invalid Move Specified");
  }
}
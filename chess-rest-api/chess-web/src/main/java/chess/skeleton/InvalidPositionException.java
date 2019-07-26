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
public class InvalidPositionException extends RuntimeException{
   /**
    * Invalid position exception
    * @param msg message
    * @param row row
    * @param column column
    */ 
   public InvalidPositionException(String msg, int row, char column){
    super(msg + " (row: " + row + " column: " + column);
  }
}

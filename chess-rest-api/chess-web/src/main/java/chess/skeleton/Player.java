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
/**
 * Which side of the board is being played
 */
public enum Player {
    
    
  White,  Black;
  
  private Player() {}
  public Player other() { 
      if (this.equals((Object) White)){
        return Black;
      }
      return White;
  }

}
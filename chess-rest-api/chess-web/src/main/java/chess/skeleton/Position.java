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
 * Describes a position on the Chess Board
 */
public class Position {
  public static final Position OFF_BOARD = new Position();
  
  private int row;
  
  private char column;
  

  private Position()
  {
    row = -1;
    column = 'z';
  }
  
  public Position(char column, int row)
  {
    this.row = validateRow(row);
    this.column = validateColumn(column);
  }
  



  public Position(String colrow)
  {
    if (colrow.length() != 2) {
      throw new InvalidPositionException("Wrong length string to create a position: " + colrow, 0, ' ');
    }
    
    char[] chars = colrow.toCharArray();
    column = validateColumn(chars[0]);
    int rowFromStr = Character.digit(chars[1], 10);
    row = validateRow(rowFromStr);
  }
  



  public Position(Position position)
  {
    this(position.getColumn(), position.getRow());
  }
  
  public int getRow() {
    return row;
  }
  
  public char getColumn() {
    return column;
  }
  
  public Position step(Direction direction) {
    switch (direction) {
    case North:  return safeBuilder(column, row + 1);
    case NorthEast:  return safeBuilder(column, row - 1);
    case East:  return safeBuilder(column + '\001', row);
    case SouthEast:  return safeBuilder(column - '\001', row);
    case South: return safeBuilder(column + '\001', row + 1);
    case SouthWest:  return safeBuilder(column + '\001', row - 1);
    case West:  return safeBuilder(column - '\001', row + 1);
    case NorthWest:  return safeBuilder(column - '\001', row - 1); }
    throw new IllegalArgumentException("Unknown direction: " + direction);
  }
  
  private Position safeBuilder(int column, int row)
  {
    try {
      return new Position((char)column, row);
    } catch (InvalidPositionException ex) {}
    return null;
  }
  

  @Override
  public String toString()
  {
    return "" + column + row;
  }
  
  @Override
  public boolean equals(Object o)
  {
    if (this == o) return true;
    if ((o == null) || (getClass() != o.getClass())) { return false;
    }
    Position position = (Position)o;
    
    if (column != column) { return false;
    }
    
    return row == row;
  }
  
  @Override
  public int hashCode()
  {
    int result = row;
    result = 31 * result + column;
    return result;
  }
  





  private int validateRow(int row)
  {
    if ((row < 1) || (row > 8)) {
      throw new InvalidPositionException("Invalid Row", row, column);
    }
    
    return row;
  }
  





  private char validateColumn(char column)
  {
    column = Character.toLowerCase(column);
    
    if ((column < 'a') || (column > 'h')) {
      throw new InvalidPositionException("Invalid Column", row, column);
    }
    
    return column;
  }
}

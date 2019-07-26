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
public class Position
{
  public static final Position OFF_BOARD = new Position();
  private int row;
  private char column;
  
  private Position()
  {
    this.row = -1;
    this.column = 'z';
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
    this.column = validateColumn(chars[0]);
    int rowFromStr = Character.digit(chars[1], 10);
    this.row = validateRow(rowFromStr);
  }
  
  public Position(Position position)
  {
    this(position.getColumn(), position.getRow());
  }
  
  public int getRow()
  {
    return this.row;
  }
  
  public char getColumn()
  {
    return this.column;
  }
  
  public Position step(Direction direction)
  {
    switch (direction)
    {
    case North: 
      return safeBuilder(this.column, this.row + 1);
    case South: 
      return safeBuilder(this.column, this.row - 1);
    case East: 
      return safeBuilder(this.column + '\001', this.row);
    case West: 
      return safeBuilder(this.column - '\001', this.row);
    case NorthEast: 
      return safeBuilder(this.column + '\001', this.row + 1);
    case SouthEast: 
      return safeBuilder(this.column + '\001', this.row - 1);
    case NorthWest: 
      return safeBuilder(this.column - '\001', this.row + 1);
    case SouthWest: 
      return safeBuilder(this.column - '\001', this.row - 1);
    }
    throw new IllegalArgumentException("Unknown direction: " + direction);
  }
  
  private Position safeBuilder(int column, int row)
  {
    try
    {
      return new Position((char)column, row);
    }
    catch (InvalidPositionException ex) {}
    return null;
  }
  
  public String toString()
  {
    return "" + this.column + this.row;
  }
  
  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if ((o == null) || (getClass() != o.getClass())) {
      return false;
    }
    Position position = (Position)o;
    if (this.column != position.column) {
      return false;
    }
    if (this.row != position.row) {
      return false;
    }
    return true;
  }
  
  public int hashCode()
  {
    int result = this.row;
    result = 31 * result + this.column;
    return result;
  }
  
  private int validateRow(int row)
  {
    if ((row < 1) || (row > 8)) {
      throw new InvalidPositionException("Invalid Row", row, this.column);
    }
    return row;
  }
  
  private char validateColumn(char column)
  {
    column = Character.toLowerCase(column);
    if ((column < 'a') || (column > 'h')) {
      throw new InvalidPositionException("Invalid Column", this.row, column);
    }
    return column;
  }
}

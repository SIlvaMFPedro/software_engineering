/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.skeleton;

import chess.skeleton.pieces.Bishop;
import chess.skeleton.pieces.Piece;
import chess.skeleton.pieces.Rook;
import chess.skeleton.pieces.Pawn;
import chess.skeleton.pieces.Knight;
import chess.skeleton.pieces.Queen;
import chess.skeleton.pieces.King;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author pedro
 */
public class GameStateStringifier
{
  private static Map<Character, Class> pieceMap = new HashMap();
  
  static
  {
    pieceMap.put(Character.valueOf('p'), Pawn.class);
    pieceMap.put(Character.valueOf('n'), Knight.class);
    pieceMap.put(Character.valueOf('b'), Bishop.class);
    pieceMap.put(Character.valueOf('r'), Rook.class);
    pieceMap.put(Character.valueOf('q'), Queen.class);
    pieceMap.put(Character.valueOf('k'), King.class);
  }
  
  public static final String NEWLINE = System.getProperty("line.separator");
  private GameState gameState;
  
  public GameStateStringifier(GameState gameState)
  {
    this.gameState = gameState;
  }
  
  public GameStateStringifier(String[] rows, Player nextToMove)
  {
    this.gameState = new GameState();
    
    int numRows = rows.length;
    if (numRows != 8) {
      throw new IllegalArgumentException("Wrong # of rows: " + numRows);
    }
    for (int currentRow = 0; currentRow < 8; currentRow++)
    {
      String row = rows[currentRow];
      if (row.length() != 8) {
        throw new IllegalArgumentException("Wrong # of columns in row: " + row.length());
      }
      char[] chars = row.toCharArray();
      for (int currentChar = 0; currentChar <= 7; currentChar++)
      {
        char pieceChar = chars[currentChar];
        if (pieceChar != ' ')
        {
          Piece piece = instantiatePiece(pieceChar);
          if (piece != null)
          {
            char chessCol = (char)(currentChar + 97);
            int chessRow = 8 - currentRow;
            Position position = new Position(chessCol, chessRow);
            this.gameState.placePiece(piece, position);
          }
        }
      }
    }
    this.gameState.setCurrentPlayer(nextToMove);
  }
  
  public GameState getGameState()
  {
    return this.gameState;
  }
  
  public String getBoardAsString()
  {
    StringBuilder builder = new StringBuilder();
    
    printColumnLabels(builder);
    for (int i = 8; i >= 1; i--)
    {
      printSeparator(builder);
      printSquares(i, builder);
    }
    printSeparator(builder);
    printColumnLabels(builder);
    
    return builder.toString();
  }
  
  private Piece instantiatePiece(char pieceChar)
  {
    Class clazz = (Class)pieceMap.get(Character.valueOf(Character.toLowerCase(pieceChar)));
    
    Player owner = pieceChar <= 'Z' ? Player.Black : Player.White;
    if (clazz == null) {
      throw new IllegalArgumentException("No chess piece for character '" + pieceChar + "'");
    }
    try
    {
      Constructor ctor = clazz.getDeclaredConstructor(new Class[] { Player.class });
      return (Piece)ctor.newInstance(new Object[] { owner });
    }
    catch (Exception ex)
    {
      throw new RuntimeException("Failed to create piece", ex);
    }
  }
  
  private void printSquares(int rowLabel, StringBuilder builder)
  {
    builder.append(rowLabel);
    for (char c = 'a'; c <= 'h'; c = (char)(c + '\001'))
    {
      Piece piece = this.gameState.getPieceAt(new Position(c, rowLabel));
      char pieceChar = piece == null ? ' ' : piece.getIdentifier();
      builder.append(" | ").append(pieceChar);
    }
    builder.append(" | ").append(rowLabel).append(NEWLINE);
  }
  
  private void printSeparator(StringBuilder builder)
  {
    builder.append("  +---+---+---+---+---+---+---+---+").append(NEWLINE);
  }
  
  private void printColumnLabels(StringBuilder builder)
  {
    builder.append("   ");
    for (char c = 'a'; c <= 'h'; c = (char)(c + '\001')) {
      builder.append(" ").append(c).append("  ");
    }
    builder.append(NEWLINE);
  }
}
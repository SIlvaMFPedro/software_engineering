/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.skeleton;

import chess.consumer.ChessConsumerBlack;
import chess.consumer.ChessConsumerWhite;
import chess.skeleton.pieces.Piece;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;



/**
 *
 * @author pedro
 */

/**
 * This class provides the basic CLI interface to the Chess game.
 */
public class CLI
{
  private final BufferedReader inReader;
  private final PrintStream outStream;
  private GameState gameState = null;
  
  static ArrayList<String> white_moves;
  static ArrayList<String> black_moves;
  
  private int white_count_moves;
  private int black_count_moves;
  
  public CLI(InputStream inputStream, PrintStream outStream)
  {
    this.inReader = new BufferedReader(new InputStreamReader(inputStream));
    this.outStream = outStream;
    white_moves = ChessConsumerWhite.retrieveMoves();
    ChessConsumerWhite.produceWhiteMoves(white_moves);
    black_moves = ChessConsumerBlack.retrieveMoves();
    ChessConsumerBlack.produceBlackMoves(black_moves);
    //initiate counters
    this.white_count_moves = 0;
    this.black_count_moves  = 0;
    writeOutput("Welcome to Chess!");
  }
  
  private void writeOutput(String str)
  {
    this.outStream.println(str);
  }
  
  private String getInput()
  {
    try
    {
      this.outStream.print("> ");
      return this.inReader.readLine();
    }
    catch (IOException e)
    {
      throw new RuntimeException("Failed to read from input: ", e);
    }
  }
  
  void startEventLoop()
  {
    writeOutput("Type 'help' for a list of commands.");
    doNewGame();
    for (;;)
    {
      showBoard();
      Player currentPlayer = this.gameState.getCurrentPlayer();
      if (this.gameState.isGameOver()) {
        writeOutput("The game is over.  Congrats to " + currentPlayer.other() + ".");
      } else if (this.gameState.isInCheck()) {
        writeOutput(currentPlayer + " is in check");
      } else {
        writeOutput(currentPlayer + "'s Move");
      }
      //String input = getInput();
      String input = "move";
      if (input == null) {
        break;
      }
      if (input.length() > 0) {
        if (input.equals("help"))
        {
          showCommands();
        }
        else if (input.equals("new"))
        {
          doNewGame();
        }
        else if (input.equals("quit"))
        {
          writeOutput("Goodbye!");
          System.exit(0);
        }
        else if (input.equals("board"))
        {
          writeOutput("Current Game:");
        }
        else if (input.equals("list"))
        {
          showList();
        }
        else if (input.startsWith("move"))
        {
          //currentPlayer = Player.Black;
          if((this.white_count_moves + this.black_count_moves) == 23){
                break;
          }
          //doMove(input);
          if(currentPlayer == Player.White){
              //white_moves = ChessConsumerWhite.retrieveMoves();
              doWhiteMove(this.white_count_moves);
              this.white_count_moves++;
          }
          else{
              //black_moves = ChessConsumerBlack.retrieveMoves();
              doBlackMove(this.black_count_moves);
              this.black_count_moves++;
          }
        }
        else
        {
          writeOutput("I didn't understand that.  Type 'help' for a list of commands.");
        }
      }
    }
  }
  
  private void doNewGame()
  {
    this.gameState = new GameState();
    this.gameState.reset();
  }
  
  private void showList()
  {
    Map<Piece, Set<Move>> moveMap = this.gameState.findPossibleMoves();
    writeOutput(this.gameState.getCurrentPlayer() + "'s Possible Moves: ");
    for (Piece piece : moveMap.keySet())
    {
      Set<Move> moves = (Set)moveMap.get(piece);
      for (Move move : moves) {
        writeOutput("    " + move);
      }
    }
  }
  
  private void doMove(String input)
  {
    String moveStr = input.substring(4).trim();
    try
    {
      boolean executed = this.gameState.makeMove(moveStr);
      if (!executed) {
        writeOutput("Invalid move: " + this.gameState.getCurrentPlayer() + " would be in check");
      }
    }
    catch (InvalidMoveException ex)
    {
      writeOutput("That move doesn't make sense.");
    }
    catch (InvalidPositionException ex)
    {
      writeOutput("Please specify a move as 'colrow colrow'. For instance, 'move d2 d4'");
    }
  }
  
  private void doWhiteMove(int index){
        String str = new String();
        str = white_moves.get(index);
        try {
            boolean executed = this.gameState.makeMove(str);
            if (!executed) {
                this.writeOutput("Invalid move: " + (Object)((Object)this.gameState.getCurrentPlayer()) + " would be in check");
            }
        }
        catch (InvalidMoveException ex) {
            this.writeOutput("That move doesn't make sense.");
        }
        catch (InvalidPositionException ex) {
            this.writeOutput("Please specify a move as 'colrow colrow'. For instance, 'move d2 d4'");
        }
  }
  
  private void doBlackMove(int index){
        String str = new String();
        str = black_moves.get(index);
        try {
            boolean executed = this.gameState.makeMove(str);
            if (!executed) {
                this.writeOutput("Invalid move: " + (Object)((Object)this.gameState.getCurrentPlayer()) + " would be in check");
            }
        }
        catch (InvalidMoveException ex) {
            this.writeOutput("That move doesn't make sense.");
        }
        catch (InvalidPositionException ex) {
            this.writeOutput("Please specify a move as 'colrow colrow'. For instance, 'move d2 d4'");
        }
  }
  
  private void showBoard()
  {
    writeOutput(getBoardAsString());
  }
  
  private void showCommands()
  {
    writeOutput("Possible commands: ");
    writeOutput("    'help'                       Show this menu");
    writeOutput("    'quit'                       Quit Chess");
    writeOutput("    'new'                        Create a new game");
    writeOutput("    'board'                      Show the chess board");
    writeOutput("    'list'                       List all possible moves");
    writeOutput("    'move <colrow> <colrow>'     Make a move");
  }
  
  String getBoardAsString()
  {
    GameStateStringifier stringifer = new GameStateStringifier(this.gameState);
    return stringifer.getBoardAsString();
  }
  
  public static void main(String[] args)
  {
    CLI cli = new CLI(System.in, System.out);
    cli.startEventLoop();
  }
}

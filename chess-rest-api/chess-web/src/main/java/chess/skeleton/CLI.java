/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.skeleton;

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
import java.util.Map;
import java.util.Set;



/**
 *
 * @author pedro
 */

/**
 * This class provides the basic CLI interface to the Chess game.
 */
public class CLI {
    private static final String SAVED_GAMES_LOCATION = System.getProperty("user.dir") + "/__SAVED-GAMES__";
    private final BufferedReader inReader;
    private final PrintStream outStream;
    private GameState gameState = null;

    public CLI(InputStream inputStream, PrintStream outStream) {
        this.inReader = new BufferedReader(new InputStreamReader(inputStream));
        this.outStream = outStream;
        this.writeOutput("Welcome to Chess!");
    }

    private void writeOutput(String str) {
        this.outStream.println(str);
    }

    private String getInput() {
        try {
            this.outStream.print("> ");
            return this.inReader.readLine();
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to read from input: ", e);
        }
    }

    void startEventLoop() {
        this.writeOutput("Type 'help' for a list of commands.");
        this.doNewGame();
        do {
            String name;
            this.showBoard();
            Player currentPlayer = this.gameState.getCurrentPlayer();
            if (this.gameState.isGameOver()) {
                this.writeOutput("The game is over.  Congrats to " + (Object)((Object)currentPlayer.other()) + ".");
            } else if (this.gameState.isInCheck()) {
                this.writeOutput((Object)((Object)currentPlayer) + " is in check");
            } else {
                this.writeOutput((Object)((Object)currentPlayer) + "'s Move");
            }
            String input = this.getInput();
            if (input == null) break;
            if (input.length() <= 0) continue;
            if (input.equals("help")) {
                this.showCommands();
                continue;
            }
            if (input.equals("new")) {
                this.doNewGame();
                continue;
            }
            if (input.equals("quit")) {
                this.writeOutput("Goodbye!");
                System.exit(0);
                continue;
            }
            if (input.equals("board")) {
                this.writeOutput("Current Game:");
                continue;
            }
            if (input.equals("list")) {
                this.showList();
                continue;
            }
            if (input.equals("savedGameLocation")) {
                this.writeOutput(SAVED_GAMES_LOCATION);
                continue;
            }
            if (input.startsWith("move")) {
                this.doMove(input);
                continue;
            }
            if (input.startsWith("save")) {
                name = input.split(" ")[1];
                this.doWriteGameToFile(name);
                continue;
            }
            if (input.startsWith("load")) {
                name = input.split(" ")[1];
                this.doCreateGameFromFile(name);
                continue;
            }
            this.writeOutput("I didn't understand that.  Type 'help' for a list of commands.");
        } while (true);
    }

    private void doNewGame() {
        this.gameState = new GameState();
        this.gameState.reset();
    }

    private void showList() {
        Map<Piece, Set<Move>> moveMap = this.gameState.findPossibleMoves();
        this.writeOutput((Object)((Object)this.gameState.getCurrentPlayer()) + "'s Possible Moves: ");
        for (Piece piece : moveMap.keySet()) {
            Set<Move> moves = moveMap.get((Object)piece);
            for (Move move : moves) {
                this.writeOutput("    " + (Object)move);
            }
        }
    }

    private void doMove(String input) {
        String moveStr = input.substring(4).trim();
        try {
            boolean executed = this.gameState.makeMove(moveStr);
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

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @SuppressWarnings({"CallToPrintStackTrace", "null"})
    private void doWriteGameToFile(String name) {
        block10 : {
            new File(SAVED_GAMES_LOCATION).mkdirs();
            Writer writer = null;
            try {
                writer = new BufferedWriter(new OutputStreamWriter((OutputStream)new FileOutputStream(SAVED_GAMES_LOCATION + "/" + name + ".txt"), "utf-8"));
                writer.write("player:" + (Object)((Object)this.gameState.getCurrentPlayer()) + "\n");
                GameStateStringifier stringifier = new GameStateStringifier(this.gameState);
                writer.write(stringifier.getBoardAsString(false));
                break block10;
            }
            catch (IOException e) {
                e.printStackTrace();
                break block10;
            }
            finally {
                try {
                    writer.close();
                }
                catch (Exception e) {
                }
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void doCreateGameFromFile(String name) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(SAVED_GAMES_LOCATION + "/" + name + ".txt"));
            StringBuilder sb = new StringBuilder();
            String line = reader.readLine();
            boolean black = false;
            if (line.startsWith("player")) {
                black = line.contains("Black");
                line = reader.readLine();
            }
            while (line != null) {
                sb.append(line);
                line = reader.readLine();
            }
            GameStateStringifier stringifier = new GameStateStringifier(sb.toString(), black ? Player.Black : Player.White);
            this.gameState = stringifier.getGameState();
        }
        catch (FileNotFoundException e) {
            this.writeOutput("Invalid game name provided");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                reader.close();
            }
            catch (Exception ex) {}
        }
    }

    private void showBoard() {
        this.writeOutput(this.getBoardAsString());
    }

    private void showCommands() {
        this.writeOutput("Possible commands: ");
        this.writeOutput("    'help'                       Show this menu");
        this.writeOutput("    'quit'                       Quit Chess");
        this.writeOutput("    'new'                        Create a new game");
        this.writeOutput("    'board'                      Show the chess board");
        this.writeOutput("    'list'                       List all possible moves");
        this.writeOutput("    'move <colrow> <colrow>'     Make a move");
        this.writeOutput("    'save <gameName>'            Save a game to local storage");
        this.writeOutput("    'load <gameName>'            Load a game from local storage");
        this.writeOutput("    'savedGameLocation'          Output the directory of saved games");
    }

    String getBoardAsString() {
        GameStateStringifier stringifer = new GameStateStringifier(this.gameState);
        return stringifer.getBoardAsString();
    }

    public static void main(String[] args) {
        CLI cli = new CLI(System.in, System.out);
        cli.startEventLoop();
    }
}

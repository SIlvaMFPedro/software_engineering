package chess.web;


import chess.consumer.MovesFrequency;
import chess.skeleton.GameState;
import chess.skeleton.GameStateStringifier;
import chess.skeleton.InvalidMoveException;
import chess.skeleton.Move;
import chess.skeleton.Player;
import chess.skeleton.pieces.Piece;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.glassfish.grizzly.http.server.Session;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.*;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.codehaus.jackson.util.DefaultPrettyPrinter;

/**
 * REST Resource that exposes the Chess Game via
 */
@Path("api/chess")
public class ChessResource {
    static final String CHESS_GAME = "chess.web.game-state";
    private GameStateChanger gameChanger;
    private Set<Move> possible_moves = new HashSet<Move>();

    @SuppressWarnings("UnusedDeclaration")
    public ChessResource() {
        this(new GameStateChanger());
        System.out.print("Created chess resource...\n");
    }
    
    public ChessResource(GameStateChanger gameChanger) {
        this.gameChanger = gameChanger;
        System.out.print("Created chess resource game changer...\n");
    }
    
    @GET
    @Path("/getgame")
    @Produces({ MediaType.APPLICATION_JSON })
    public String getGame(@Context Request request, @Context Response response) throws IOException {
        System.out.println("Get GameStateBean");
        GameStateBean gamestatebean = getGameStateBean(request, response);
        GameState gamestate = GameStateBean.toGameState(gamestatebean);
        GameStateStringifier stringifer = new GameStateStringifier(gamestate);
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
        try{
            System.out.print("Creating json file...\n");
            //StringBuilder str = new StringBuilder().append("{\"index\":{\"_index\":\"chesstourny1\"}}\n").append("\n").append(stringifer.getBoardAsString());
            //write get game into json file
            JsonObj obj = new JsonObj("{\"index\":{\"_index\":\"chesstourney1\"}}", stringifer.getBoardAsString());
            writer.writeValue(new File("GetGame.json"), obj);
        }catch(FileNotFoundException e){
            
        }
        return stringifer.getBoardAsString();
        
    }
    
    @GET
    @Path("/game")
    @Produces({ MediaType.APPLICATION_JSON })
    public GameStateBean getGameState(@Context Request request, @Context Response response) {
        System.out.println("Get GameStateBean");
        return getGameStateBean(request, response);
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/putgame")
    @Produces({ MediaType.APPLICATION_JSON })
    public GameStateBean putGame(@Context Request request, @Context Response response, GameStateBean proposedGameState) {
        System.out.print("Put GameStateBean\n");
        GameState currentState = getGameState(request);
        if (gameChanger.isValid(currentState, proposedGameState)) {
            GameState newState = gameChanger.applyNewState(currentState, proposedGameState);
            storeGameState(request, newState);
            return getGameStateBean(request, response);
        } else {
            throw new BadRequestException("Invalid Move");
        }
    }
    
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/getputgame")
    @Produces({ MediaType.APPLICATION_JSON })
    public String getPutGame(@Context Request request, @Context Response response, @QueryParam("id") int ID){
        System.out.print("Get Put New GameStateBean\n");
        String position = new String();
        System.out.println(ID);
        switch(ID){
            case 1:
                position = "c2 c4";
                System.out.println(position);
                break;
            case 2:
                position = "h7 h6";
                System.out.println(position);
                break;
            case 3:
                position = "h2 h4";
                System.out.println(position);
                break;
            case 4:
                position = "b8 a6";
                System.out.println(position);
                break;
            case 5:
                position = "h1 h3";
                System.out.println(position);
                break;
            case 6:
                position = "b7 b6";
                System.out.println(position);
                break;
            case 7:
                position = "c4 c5";
                System.out.println(position);
                break;
            case 8:
                position = "d7 d5";
                System.out.println(position);
                break;
            case 9:
                position = "f2 f3";
                System.out.println(position);
                break;
            case 10:
                position = "e8 d7";
                System.out.println(position);
                break;
            case 11:
                position = "b2 b3";
                System.out.println(position);
                break;
            case 12:
                position = "h8 h7";
                System.out.println(position);
                break;
            case 13:
                position = "h3 h1";
                System.out.println(position);
                break;
            case 14:
                position = "c7 c6";
                System.out.println(position);
                break;
            case 15:
                position = "d2 d4";
                System.out.println(position);
                break;
            case 16:
                position = "e7 e5";
                System.out.println(position);
                break;
            case 17:
                position = "c1 f4";
                System.out.println(position);
                break;
            case 18:
                position = "a6 b4";
                System.out.println(position);
                break;
            case 19:
                position = "b1 a3";
                System.out.println(position);
                break;
            case 20:
                position = "a7 a5";
                System.out.println(position);
                break;
            case 21:
                position = "a3 b5";
                System.out.println(position);
                break;
            case 22:
                position = "d8 h4";
                System.out.println(position);
                break;
            case 23:
                position = "g2 g3";
                System.out.println(position);
                break;
            default:
                throw new BadRequestException("Invalid Game ID");
        }
        if(position.length() != 5){
            throw new InvalidMoveException();
        }
        else{
            Move move = new Move(position);
            MoveBean movebean = new MoveBean(move);
            GameState currentState = getGameState(request);
            
            if(ID%2 == 0){
                currentState.setCurrentPlayer(Player.Black);
            }
            else{
                currentState.setCurrentPlayer(Player.White);
            }
            
            try{
                System.out.print("Current Player: " + currentState.getCurrentPlayer() + "\n");
                currentState.makeMove(movebean.toString());
                GameStateBean gamestatebean = getGameStateBean(request, response);
                GameState gamestate = GameStateBean.toGameState(gamestatebean);
                GameStateStringifier stringifer = new GameStateStringifier(gamestate);
                ObjectMapper mapper = new ObjectMapper();
                ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
        
                try{
                    System.out.print("Creating json file...\n");
                    JsonObj obj = new JsonObj("{\"index\":{\"_index\":\"chesstourney2\"}}", stringifer.getBoardAsString());
                    //write put game into file
                    writer.writeValue(new File("PutGame.json"), obj);
                }catch(FileNotFoundException | UnsupportedEncodingException e){

                }
                
                return stringifer.getBoardAsString();
            } catch (Exception imex) {
                throw new BadRequestException("Bad Move: " + imex.getMessage());
            }
        }
    }
    
    
    @PUT
    @Path("/game")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String putGame(@Context Request request, @Context Response response, @QueryParam("id") int ID) {
        System.out.print("Get Put New GameStateBean\n");
        String position = new String();
        System.out.println(ID);
        switch(ID){
            case 1:
                position = "c2 c4";
                System.out.println(position);
                break;
            case 2:
                position = "h7 h6";
                System.out.println(position);
                break;
            case 3:
                position = "h2 h4";
                System.out.println(position);
                break;
            case 4:
                position = "b8 a6";
                System.out.println(position);
                break;
            case 5:
                position = "h1 h3";
                System.out.println(position);
                break;
            case 6:
                position = "b7 b6";
                System.out.println(position);
                break;
            case 7:
                position = "c4 c5";
                System.out.println(position);
                break;
            case 8:
                position = "d7 d5";
                System.out.println(position);
                break;
            case 9:
                position = "f2 f3";
                System.out.println(position);
                break;
            case 10:
                position = "e8 d7";
                System.out.println(position);
                break;
            case 11:
                position = "b2 b3";
                System.out.println(position);
                break;
            case 12:
                position = "h8 h7";
                System.out.println(position);
                break;
            case 13:
                position = "h3 h1";
                System.out.println(position);
                break;
            case 14:
                position = "c7 c6";
                System.out.println(position);
                break;
            case 15:
                position = "d2 d4";
                System.out.println(position);
                break;
            case 16:
                position = "e7 e5";
                System.out.println(position);
                break;
            case 17:
                position = "c1 f4";
                System.out.println(position);
                break;
            case 18:
                position = "a6 b4";
                System.out.println(position);
                break;
            case 19:
                position = "b1 a3";
                System.out.println(position);
                break;
            case 20:
                position = "a7 a5";
                System.out.println(position);
                break;
            case 21:
                position = "a3 b5";
                System.out.println(position);
                break;
            case 22:
                position = "d8 h4";
                System.out.println(position);
                break;
            case 23:
                position = "g2 g3";
                System.out.println(position);
                break;
            default:
                throw new BadRequestException("Invalid Game ID");
        }
        if(position.length() != 5){
            throw new InvalidMoveException();
        }
        else{
            Move move = new Move(position);
            MoveBean movebean = new MoveBean(move);
            GameState currentState = getGameState(request);
            
            if(ID%2 == 0){
                currentState.setCurrentPlayer(Player.Black);
            }
            else{
                currentState.setCurrentPlayer(Player.White);
            }
            
            try{
                System.out.print("Current Player: " + currentState.getCurrentPlayer() + "\n");
                currentState.makeMove(movebean.toString());
                GameStateBean gamestatebean = getGameStateBean(request, response);
                GameState gamestate = GameStateBean.toGameState(gamestatebean);
                GameStateStringifier stringifer = new GameStateStringifier(gamestate);
                ObjectMapper mapper = new ObjectMapper();
                ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
        
                try{
                    System.out.print("Creating json file...\n");
                    JsonObj obj = new JsonObj("{\"index\":{\"_index\":\"chesstourney2\"}}", stringifer.getBoardAsString());
                    //write put game into file
                    writer.writeValue(new File("PutGame.json"), obj);
                }catch(FileNotFoundException | UnsupportedEncodingException e){

                }
                
                return stringifer.getBoardAsString();
            } catch (Exception imex) {
                throw new BadRequestException("Bad Move: " + imex.getMessage());
            }
        }
    }
      
    @GET
    @Path("/getpostgame")
    @Produces({ MediaType.APPLICATION_JSON })
    public String getPostGame(@Context Request request, @Context Response response) throws IOException{
        System.out.print("Get Post Game!");
        GameState gameState = new GameState();
        gameState.reset();
        storeGameState(request, gameState);
        GameStateBean gamestatebean = getGameStateBean(request, response);
        GameState gamestate = GameStateBean.toGameState(gamestatebean);
        GameStateStringifier stringifer = new GameStateStringifier(gamestate);
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
        
        try{
            System.out.print("Creating json file...\n");
            JsonObj obj = new JsonObj("{\"index\":{\"_index\":\"chesstourney3\"}}", stringifer.getBoardAsString());
            //write post game into file
            writer.writeValue(new File("PostGame.json"), obj);
        }catch(FileNotFoundException | UnsupportedEncodingException e){
            
        }
        return stringifer.getBoardAsString();
    }
    
    
    @POST
    @Path("/postgame")
    @Produces({ MediaType.APPLICATION_JSON })
    public String postGame(@Context Request request, @Context Response response) throws IOException {
        System.out.print("Get Post Game!");
        GameState gameState = new GameState();
        gameState.reset();
        storeGameState(request, gameState);
        GameStateBean gamestatebean = getGameStateBean(request, response);
        GameState gamestate = GameStateBean.toGameState(gamestatebean);
        GameStateStringifier stringifer = new GameStateStringifier(gamestate);
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
        
        try{
            System.out.print("Creating json file...\n");
            JsonObj obj = new JsonObj("{\"index\":{\"_index\":\"chesstourney3\"}}", stringifer.getBoardAsString());
            //write post game into file
            writer.writeValue(new File("PostGame.json"), obj);
        }catch(FileNotFoundException | UnsupportedEncodingException e){
            
        }
        return stringifer.getBoardAsString();
    }
   
    @GET
    @Path("/getmoves")
    @Produces({ MediaType.APPLICATION_JSON })
    public List<MoveBean> getMovesList(@Context Request request, @Context Response response) throws IOException {
        System.out.print("Get Moves List!\n");
        GameState gameState = getGameState(request);
        addCustomHeaders(gameState, response);
        List<MoveBean> moves = getCurrentMoves(gameState);
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());

        try{
            System.out.print("Creating json file...\n");
            String str = "{\"index\":{\"_index\":\"chesstourney4\"}}\n";
            //write moves list in a json file
            JsonObj obj = new JsonObj(str, moves.toString());
            writer.writeValue(new File("GetMoves.json"), obj);
        }catch(FileNotFoundException | UnsupportedEncodingException e){
            
        }
        return moves;
    }
    
    
    @POST
    @Path("/postmoves")
    @Produces(MediaType.APPLICATION_JSON )
    @Consumes(MediaType.APPLICATION_JSON)
    public GameStateBean postMove(@Context Request request, @Context Response response, MoveBean move) {
        System.out.print("Post Move!\n");
        GameState currentState = getGameState(request);

        try {
            currentState.makeMove(move.toString());
            return getGameStateBean(request, response);
        } catch (Exception imex) {
            throw new BadRequestException(("Bad Move: " + imex.getMessage()));
        }
    }
    
    @GET
    @Path("/getpostmoves")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getPostMove(@Context Request request, @Context Response response, @QueryParam("pos") String position){
        System.out.print("Post New Move!\n");
        if(position.length() != 5){
            throw new InvalidMoveException();
        }
        else{
            Move move = new Move(position);
            MoveBean movebean = new MoveBean(move);
            GameState currentState = getGameState(request);
            
            try{
                currentState.makeMove(movebean.toString());
                GameStateBean gamestatebean = getGameStateBean(request, response);
                GameState gamestate = GameStateBean.toGameState(gamestatebean);
                GameStateStringifier stringifer = new GameStateStringifier(gamestate);
                ObjectMapper mapper = new ObjectMapper();
                ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());

        
                try{
                    System.out.print("Creating json file...\n");
                    //write post move into file
                    JsonObj obj = new JsonObj("{\"index\":{\"_index\":\"chesstourney5\"}}", stringifer.getBoardAsString());
                    writer.writeValue(new File("PostMove.json"), obj);
                }catch(FileNotFoundException | UnsupportedEncodingException e){

                }
                return stringifer.getBoardAsString();
            } catch (Exception imex) {
                throw new BadRequestException("Bad Move: " + imex.getMessage());
            }
        }
    }   
    
    @POST
    @Path("/moves")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String postMove(@Context Request request, @Context Response response, @QueryParam("pos") String position){
        System.out.print("Post New Move!\n");
        if(position.length() != 5){
            throw new InvalidMoveException();
        }
        else{
            Move move = new Move(position);
            MoveBean movebean = new MoveBean(move);
            GameState currentState = getGameState(request);
            
            try{
                currentState.makeMove(movebean.toString());
                GameStateBean gamestatebean = getGameStateBean(request, response);
                GameState gamestate = GameStateBean.toGameState(gamestatebean);
                GameStateStringifier stringifer = new GameStateStringifier(gamestate);
                ObjectMapper mapper = new ObjectMapper();
                ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());

        
                try{
                    System.out.print("Creating json file...\n");
                    //write post move into file
                    JsonObj obj = new JsonObj("{\"index\":{\"_index\":\"chesstourney5\"}}", stringifer.getBoardAsString());
                    writer.writeValue(new File("PostMove.json"), obj);
                }catch(FileNotFoundException | UnsupportedEncodingException e){

                }
                return stringifer.getBoardAsString();
            } catch (Exception imex) {
                throw new BadRequestException("Bad Move: " + imex.getMessage());
            }
        }
    }
    
    @GET
    @Path("/movesfrequency")
    @Produces(MediaType.APPLICATION_JSON)
    public List<MoveBean> getMovesFrequency(@Context Request request, @Context Response response) throws IOException{
        GameStateBean gamestatebean = getGameStateBean(request, response);
        GameState gamestate = GameStateBean.toGameState(gamestatebean);
        addCustomHeaders(gamestate, response);
        Map<Piece, Set<Move>> possibleMoves = gamestate.findPossibleMoves();
        List<MoveBean> moveBeans = new ArrayList<>();
        for (Piece piece : possibleMoves.keySet()) {
            Set<Move> moves = possibleMoves.get(piece);
            for (Move move : moves) {
                int freq1 = Collections.frequency(moves, move);
                int freq2 = Collections.frequency(possible_moves, move);
                int frequency = freq1 + freq2;
                moveBeans.add(new MoveBean(move, frequency));
            }
        }
        
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
        
        try{
            System.out.print("Creating json file...\n");
            String str = "{\"index\":{\"_index\":\"chesstourney6\"}}";
            String moves = new String();
            moves += moveBeans;
            //str += moveBeans;
            //write moves list in a json file
            JsonObj obj = new JsonObj(str, moves);
            writer.writeValue(new File("MovesFrequency.json"), obj);
        }catch(FileNotFoundException | UnsupportedEncodingException e){
            
        }
        
        return moveBeans;
    }
    
    @GET
    @Path("/piecefrequency")
    @Produces(MediaType.APPLICATION_JSON)
    public List<PieceBean> getPieceFrequency(@Context Request request, @Context Response response) throws IOException{
        GameStateBean gamestatebean = getGameStateBean(request, response);
        GameState gamestate = GameStateBean.toGameState(gamestatebean);
        addCustomHeaders(gamestate, response);
        Map<Piece, Set<Move>> possibleMoves = gamestate.findPossibleMoves();
        List<PieceBean> pieceBeans = new ArrayList<>();
        List<Character> pieceIDs = new ArrayList<>();
        
        List<Piece> pieces = new ArrayList<Piece>(possibleMoves.keySet());
        //look for identifiers
        for(int i = 0; i < pieces.size(); i++){
            pieceIDs.add(pieces.get(i).getIdentifier());
        }
        //get identifier frequency
        for(Piece piece : possibleMoves.keySet()){
            int frequency = Collections.frequency(pieceIDs, piece.getIdentifier());
            pieceBeans.add(new PieceBean(piece, frequency));
        }
        
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
        
        try{
            System.out.print("Creating json file...\n");
            String str = "{\"index\":{\"_index\":\"chesstourney7\"}}";
            //str += pieceBeans;
            //write moves list in a json file
            JsonObj obj = new JsonObj(str, pieceBeans.toString());
            writer.writeValue(new File("PiecesFrequency.json"), obj);
        }catch(FileNotFoundException | UnsupportedEncodingException e){
            
        }
        return pieceBeans;
    }
      
    private GameStateBean getGameStateBean(Request request, Response response) {
        System.out.print("Get Game State Bean function!\n");
        GameState gameState = getGameState(request);

        addCustomHeaders(gameState, response);

        return new GameStateBean(gameState);
    }

    /**
     * Convenience for debugging, we add the chess board string representation as
     * custom headers to the response, along with the current
     * @param gameState The current game state
     * @param response The response we are writing
     */
    private void addCustomHeaders(GameState gameState, Response response) {
        System.out.print("Add custom headers!\n");
        GameStateStringifier stringifier = new GameStateStringifier(gameState);

        String board = stringifier.getBoardAsString();
        StringTokenizer tokenizer = new StringTokenizer(board, GameStateStringifier.NEWLINE);

        int lineCount = 0;
        while (tokenizer.hasMoreElements()) {
            String headerName = getHeaderName(lineCount);

            // Most Header displays trim the header values.  Compensate for that by
            // padding the line value
            String lineValue = tokenizer.nextToken();
            String spacerValue = "";
            StringTokenizer lineTokenizer = new StringTokenizer(lineValue, " ", true);
            while (lineTokenizer.hasMoreElements() && (lineTokenizer.nextToken().equals(" "))) {
                spacerValue = "-" + spacerValue;
            }
            lineValue = spacerValue + lineValue.trim();

            response.addHeader(headerName, lineValue);
            lineCount++;
        }

        Player currentPlayer = gameState.getCurrentPlayer();
        if (gameState.isGameOver()) {
            response.addHeader(getHeaderName(lineCount), "The Game Is Over.  Congrats to " + currentPlayer.other());
        } else {
            if (gameState.isInCheck()) {
                response.addHeader(getHeaderName(lineCount), currentPlayer + " is in CHECK");
                lineCount++;
            }
            response.addHeader(getHeaderName(lineCount), "> " + currentPlayer + "'s Move");
        }
    }

    private String getHeaderName(int lineCount) {
        System.out.print("Get Header Name!\n");
        String headerOrdering = String.valueOf(lineCount);
        if (lineCount < 10) {
            headerOrdering = "00" + headerOrdering;
        } else if (lineCount < 100) {
            headerOrdering = "0" + headerOrdering;
        }
        return "X-Chess-" + headerOrdering;
    }

    private void storeGameState(Request request, GameState gameState) {
        System.out.print("Store Game State\n");
        Session session = request.getSession();
        session.setAttribute(CHESS_GAME, gameState);
    }

    private GameState getGameState(Request request) {
        System.out.print("Get Game State\n");
        Session session = request.getSession();
        GameState gameState = (GameState) session.getAttribute(CHESS_GAME);
        if (gameState == null) {
            gameState = createNewGame(session);
        }
        return gameState;
    }

    private GameState createNewGame(Session session) {
        System.out.print("Create new Game\n");
        GameState gameState = new GameState();
        gameState.reset();
        session.setAttribute(CHESS_GAME, gameState);
        return gameState;
    }

    private List<MoveBean> getCurrentMoves(GameState gameState) {
        System.out.print("Get Current Moves\n");
        Map<Piece, Set<Move>> possibleMoves = gameState.findPossibleMoves();
        List<MoveBean> moveBeans = new ArrayList<>();
        for (Piece piece : possibleMoves.keySet()) {
            Set<Move> moves = possibleMoves.get(piece);
            possible_moves = moves;
            for (Move move : moves) {
                moveBeans.add(new MoveBean(move));
            }
        }
        return moveBeans;
    }
}

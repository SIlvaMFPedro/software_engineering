package chess.web;


import chess.skeleton.GameState;
import chess.skeleton.GameStateStringifier;
import chess.skeleton.Move;
import chess.skeleton.Player;
import chess.skeleton.pieces.Piece;

import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.glassfish.grizzly.http.server.Session;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.*;

/**
 * REST Resource that exposes the Chess Game via
 */
@Path("api/chess")
public class ChessResource {
    static final String CHESS_GAME = "chess.web.game-state";
    private GameStateChanger gameChanger;

    @SuppressWarnings("UnusedDeclaration")
    public ChessResource() {
        this(new GameStateChanger());
        System.out.print("Created chess resource...\n");

    }
    public ChessResource(GameStateChanger gameChanger) {
        this.gameChanger = gameChanger;
        System.out.print("Created chess resource...\n");
    }

    @GET
    @Path("/game")
    @Produces({ MediaType.APPLICATION_JSON })
    public GameStateBean getGame(@Context Request request, @Context Response response) {
        System.out.println("Get GameStateBean");
        return getGameStateBean(request, response);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/game")
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

    @POST
    @Path("/game")
    @Produces({ MediaType.APPLICATION_JSON })
    public GameStateBean postGame(@Context Request request, @Context Response response) {
        System.out.print("Post Game!\n");
        GameState gameState = new GameState();
        gameState.reset();
        storeGameState(request, gameState);
        return getGameStateBean(request, response);
    }
    
    @GET
    @Path("/moves")
    @Produces({ MediaType.APPLICATION_JSON })
    public List<MoveBean> getMovesList(@Context Request request, @Context Response response) {
        System.out.print("Get Moves List!\n");
        GameState gameState = getGameState(request);
        addCustomHeaders(gameState, response);
        return getCurrentMoves(gameState);
    }

    @POST
    @Path("/moves")
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
            for (Move move : moves) {
                moveBeans.add(new MoveBean(move));
            }
        }

        return moveBeans;
    }
}

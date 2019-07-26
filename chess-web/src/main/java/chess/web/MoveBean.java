package chess.web;

import chess.skeleton.Move;

/**
 * Bean to encapsulate a Move object
 */
public class MoveBean {

    private String origin;
    private String destination;
    private int frequency;

    /**
     * Bean-chess constructor
     */
    @SuppressWarnings("UnusedDeclaration")
    public MoveBean() {
    }

    /**
     * Create a move bean from a given move
     * @param move The move to translate into a bean
     */
    public MoveBean(Move move) {
        origin = move.getOrigin().toString();
        destination = move.getDestination().toString();
    }
    
    /**
     * Create a move bean from a given move and frequency
     * @param move The move to translate into a bean
     * @param freq move frequency
     */
    public MoveBean(Move move, int freq){
        origin = move.getOrigin().toString();
        destination = move.getDestination().toString();
        frequency = freq;
    }
  
    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
    
    public int getFrequency(){
        return frequency;
    }
    
    public void setFrequency(int frequency){
        this.frequency = frequency;
    }

    @Override
    public String toString() {
        return origin + " " + destination;
    }
}

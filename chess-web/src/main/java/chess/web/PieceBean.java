package chess.web;

import chess.skeleton.Player;
import chess.skeleton.pieces.*;

/**
 * A bean representing a Piece
 */
public class PieceBean {
    private String owner;
    private String type;
    private int frequency;

    public PieceBean() {
    }

    public PieceBean(Piece piece) {
        owner = piece.getPlayer().name();
        type = String.valueOf(piece.getIdentifierCharacter());
    }
    
    public PieceBean(Piece piece, int frequency){
        owner = piece.getPlayer().name();
        type = String.valueOf(piece.getIdentifierCharacter());
        this.frequency = frequency;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public int getFrequency(){
        return frequency;
    }
    
    public void setFrequency(int frequency){
        this.frequency = frequency;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PieceBean pieceBean = (PieceBean) o;

        if (owner != null ? !owner.equals(pieceBean.owner) : pieceBean.owner != null) return false;
        if (type != null ? !type.equals(pieceBean.type) : pieceBean.type != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = owner != null ? owner.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    public static Piece toPiece(PieceBean bean) {
        Player player = Player.valueOf(bean.getOwner());

        String type = bean.getType();
        Piece piece;
        // U-G-L-Y  You ain't got ... you know ....
        switch (type) {
            case "p":
                piece = new Pawn(player);
                break;
            case "r":
                piece = new Rook(player);
                break;
            case "n":
                piece = new Knight(player);
                break;
            case "b":
                piece = new Bishop(player);
                break;
            case "q":
                piece = new Queen(player);
                break;
            case "k":
                piece = new King(player);
                break;
            default:
                throw new IllegalArgumentException("No such piece type: " + type);
        }

        return piece;
    }
    
    @Override
    public String toString(){
        return owner + " " + type + " " + frequency;
    }
}

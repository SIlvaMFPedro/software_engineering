/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.skeleton;

import chess.skeleton.pieces.Piece;

/**
 *
 * @author pedro
 */
public class Move {
    private Position origin;
    private Position destination;
    private Piece movedPiece = null;
    private Piece replacedPiece = null;

    public Move(Position origin, Position destination) {
      this.origin = origin;
      this.destination = destination;
    }

    public Move(String fromToStr) {
      if (fromToStr.length() != 5) {
        throw new InvalidMoveException();
      }

      String[] parts = fromToStr.split(" ");
      origin = new Position(parts[0]);
      destination = new Position(parts[1]);
    }

    public Move(Move move)
    {
      this(move.getOrigin(), move.getDestination());
    }

    public Position getOrigin() {
      return origin;
    }

    public Position getDestination() {
      return destination;
    }

    void record(Piece movedPiece, Piece replacedPiece) {
      this.movedPiece = movedPiece;
      this.replacedPiece = replacedPiece;
    }

    public String toString()
    {
      return origin + " " + destination;
    }

    public boolean equals(Object o)
    {
      if (this == o) return true;
      if ((o == null) || (getClass() != o.getClass())) { return false;
      }
      Move move = (Move)o;

      if (destination != null ? !destination.equals(destination) : destination != null) { return false;
      }

      if (origin != null ? !origin.equals(origin) : origin != null) { return false;
      }
      return true;
    }

    public int hashCode()
    {
      int result = origin != null ? origin.hashCode() : 0;
      result = 31 * result + (destination != null ? destination.hashCode() : 0);
      return result;
    }

    public Piece getReplacedPiece() {
      return replacedPiece;
    }

    public Piece getMovedPiece() {
      return movedPiece;
    }
}

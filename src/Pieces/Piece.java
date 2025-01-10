package Pieces;
import java.util.ArrayList;

public interface Piece {
    ArrayList<Integer> calcMoves(int pieceCoordinate, String pieceColor, boolean clickedKing);
    boolean checkIfOccupied(int tileCoordinate, String pieceColor);
}

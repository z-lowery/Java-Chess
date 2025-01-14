package Pieces;
import java.util.ArrayList;

public interface Piece {
    ArrayList<Integer> calcMoves(int pieceCoordinate, String pieceColor);
    boolean checkIfOccupied(int tileCoordinate, String pieceColor);
}

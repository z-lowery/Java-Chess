package Pieces;
import java.util.ArrayList;

public interface PieceInterface {
    

    /**
     * Calculates the moves a piece can make
     * @param pieceCoordinate - the tile coordinate of the piece
     * @param pieceColor - the color of the piece
     * @return - an ArrayList of the moves the piece can make
     */
    ArrayList<Integer> calcMoves(int pieceCoordinate, String pieceColor);
}

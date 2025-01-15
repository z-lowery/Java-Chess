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

    /**
     * Checks if a tile is occupied. If so, it will add the tile to the threaten list 
     * if the piece on the tile is empty or contains an enemy
     * @param tileCoordinate - the coordinate of the tile
     * @param pieceColor - the color of the piece
     * @return - true if the tile is occupied, false otherwise
     */
    boolean checkIfOccupied(int tileCoordinate, String pieceColor);
}

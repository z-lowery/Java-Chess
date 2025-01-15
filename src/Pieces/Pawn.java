package Pieces;

import Board.*;
import java.util.ArrayList;

public class Pawn extends Piece implements PieceInterface {
    
    /**
     * Calculates the moves a pawn can make
     */
    @Override
    public ArrayList<Integer> calcMoves(int pieceCoordinate, String pieceColor) {

        /*
         * There is no way for a pawn to move back to a previous position, so if a pawn is moving from
         * a square it started in, than it MUST be making its first move. The square it starts in will be a specific
         * row based on whether if it is black or white.
         * 
         * The following code handles the front movement of the pawn for the first and subsequent turns
         */

        // If the pawn is black
        ifStatement: if (pieceColor.equals("black") && pieceCoordinate < 56) {
            if (checkIfOccupied(pieceCoordinate + TILE_FORWARD_OFFSET, pieceColor)) { // Check tile in front of the pawn
                break ifStatement;
            }

            // If in its starting row, check tile two spaces in front of the pawn
            if (pieceCoordinate > 7 && pieceCoordinate < 16) { // "if the pawn is in the second row"
                checkIfOccupied(pieceCoordinate + TILE_FORWARD_OFFSET*2, pieceColor); // Square two spaces in front of the pawn
            }
        
        // If the pawn is white
        } else if ((pieceColor.equals("white") && pieceCoordinate > 7)) {
            if (checkIfOccupied(pieceCoordinate - TILE_FORWARD_OFFSET, pieceColor)) {
                break ifStatement;
            }
            // if in its starting row
            if (pieceCoordinate < 64 && pieceCoordinate > 47) { // "if the pawn is in the second to last row"
                checkIfOccupied(pieceCoordinate - TILE_FORWARD_OFFSET*2, pieceColor); // square in front of the pawn
            }
        }

        /*
         * The following code handles the case when an enemy piece is diagonal to the pawn.
         * 
         * If there is no piece diagonal to the pawn, it will still add it to the threaten list BUT when highlighting
         * tiles there is a check. If a diagonal tile has a piece on it, highlight it red, else, not. Not
         * highlighting it means the pawn can not move to that square. See the Tile class lines 88-127 for more info.
         */
        // black pawn
        if (pieceColor.equals("black") && pieceCoordinate < 56) {
            if (pieceCoordinate % 8 != 0) {
                checkIfOccupied(pieceCoordinate + TILE_FORWARD_OFFSET - 1, pieceColor); // Check southwest tile
            }
            if ((pieceCoordinate - 7) % 8 != 0) {
                checkIfOccupied(pieceCoordinate + TILE_FORWARD_OFFSET + 1, pieceColor); // Check southeast tile
            }
        // white pawn
        } else if (pieceColor.equals("white") && pieceCoordinate > 7) {
            if (pieceCoordinate % 8 != 0) {
                checkIfOccupied(pieceCoordinate - TILE_FORWARD_OFFSET + 1, pieceColor); // Check northwest tile
            }
            if ((pieceCoordinate - 7) % 8 != 0) {
                checkIfOccupied(pieceCoordinate - TILE_FORWARD_OFFSET - 1, pieceColor); // Check northeast tile
            }
        }
        return threatenList;
    }

    /**
     * Checks if a tile is occupied. If so, it will add the tile to the threaten list if the piece on the tile is an enemy
     * @param tileCoordinate - the coordinate of the tile
     * @param pieceColor - the color of the piece
     * @return - true if the tile is occupied, false otherwise
     */
    @Override
    public boolean checkIfOccupied(int tileCoordinate, String pieceColor) {
        // if the tile is empty. Pawns will only be able to move forward if the tile is empty
        Tile targetTile = Chessboard.tileList.get(tileCoordinate);
        if (targetTile.piece == null) {
            threatenList.add(tileCoordinate);
            return false;
        } else { // if the tile is not empty but contains an enemy piece
            if (!targetTile.pieceColor.equals(pieceColor)) {
                threatenList.add(tileCoordinate);
            }
            return true;
        }
    }
}

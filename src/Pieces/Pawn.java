package Pieces;

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

        // Black pawn
        ifStatement: if (pieceColor.equals("black") && !inLastRow(pieceCoordinate)) {
            if (checkIfOccupied(pieceCoordinate + TILE_SOUTH_OFFSET, pieceColor)) { // Check tile in front of the pawn
                break ifStatement;
            }

            // If in its starting row, check tile two spaces in front of the pawn
            if (pieceCoordinate > 7 && pieceCoordinate < 16) { // "if the pawn is in the second row"
                checkIfOccupied(pieceCoordinate + TILE_SOUTH_OFFSET*2, pieceColor); // Check square two spaces in front of the pawn
            }
        
        // White pawn
        } else if ((pieceColor.equals("white") && !inFirstRow(pieceCoordinate))) {
            if (checkIfOccupied(pieceCoordinate + TILE_NORTH_OFFSET, pieceColor)) {
                break ifStatement;
            }
            // if in its starting row
            if (pieceCoordinate < 64 && pieceCoordinate > 47) { // "if the pawn is in the second to last row"
                checkIfOccupied(pieceCoordinate + TILE_NORTH_OFFSET*2, pieceColor); // Check square in front of the pawn
            }
        }

        /*
         * The following code handles the case when an enemy piece is diagonal to the pawn.
         * 
         * If there is no piece diagonal to the pawn, it will still add it to the threaten list BUT when highlighting
         * tiles there is a check. If a diagonal tile has a piece on it, highlight it red, else, not. Not
         * highlighting it means the pawn can not move to that square. See the Tile class lines 88-127 for more info.
         */
        // Black pawn
        if (pieceColor.equals("black") && pieceCoordinate < 56) {
            if (!inLeftColumn(pieceCoordinate)) {
                checkIfOccupied(pieceCoordinate + TILE_SOUTH_OFFSET - 1, pieceColor); // Check southwest tile
            }
            if (!inRightColumn(pieceCoordinate)) {
                checkIfOccupied(pieceCoordinate + TILE_SOUTH_OFFSET + 1, pieceColor); // Check southeast tile
            }

        // White pawn
        } else if (pieceColor.equals("white") && pieceCoordinate > 7) {
            if (!inLeftColumn(pieceCoordinate)) {
                checkIfOccupied(pieceCoordinate + TILE_NORTH_OFFSET + 1, pieceColor); // Check northeast tile
            }
            if (!inRightColumn(pieceCoordinate)) {
                checkIfOccupied(pieceCoordinate + TILE_NORTH_OFFSET - 1, pieceColor); // Check northwest tile
            }
        }
        return threatenList;
    }
}

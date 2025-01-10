package Pieces;

import Board.*;
import java.util.ArrayList;

public class Pawn implements Piece{
    ArrayList<Integer> threatenList = new ArrayList<>();

    @Override
    public ArrayList<Integer> calcMoves(int pieceCoordinate, String pieceColor, boolean clickedKing) {
        /*
        There is no way for a pawn to move back to a previous position, so if a pawn is moving from
           a square it started in, than it MUST be making its first move. The square it starts in will be a specific
           row based on whether if it is black or white.

           The following code handles the front movement of the pawn for the first and subsequent turns
        */

        // If the pawn is black
        ifStatement: if (pieceColor.equals("black") && pieceCoordinate < 56) { // pulling from the tileStore array located in Board.Chessboard
            if (checkIfOccupied(pieceCoordinate + 8, pieceColor)) { // square in front of the pawn
                break ifStatement;
            }
            // if in its starting row
            if (pieceCoordinate > 7 && pieceCoordinate < 16) { // "if the pawn is in the second row"
                checkIfOccupied(pieceCoordinate + 16, pieceColor); // square two spaces in front of the pawn
            }
            // If the pawn is white
        } else if ((pieceColor.equals("white") && pieceCoordinate > 7)) {
            if (checkIfOccupied(pieceCoordinate - 8, pieceColor)) {
                break ifStatement;
            }
            // if in its starting row
            if (pieceCoordinate < 64 && pieceCoordinate > 47) { // "if the pawn is in the second to last row"
                checkIfOccupied(pieceCoordinate - 16, pieceColor); // square in front of the pawn
            }
        }

        /*
        The following code handles the case when an enemy piece is diagonal to the pawn.

        If there is no piece diagonal to the pawn, it will still add it to the threaten list BUT when highlighting
        tiles there is a check. If a diagonal tile has a piece on it, highlight it red, else, not. Not
        highlighting it means the pawn can not move to that square. See the Tile class lines 88-127 for more info.
         */
        // black pawn
        if (pieceColor.equals("black") && pieceCoordinate < 56) {
            if (pieceCoordinate % 8 != 0) {
                checkIfOccupied(pieceCoordinate + 7, pieceColor);
            }
            if ((pieceCoordinate - 7) % 8 != 0) {
                checkIfOccupied(pieceCoordinate + 9, pieceColor);
            }
        // white pawn
        } else if (pieceColor.equals("white") && pieceCoordinate > 7) {
            if ((pieceCoordinate - 7) % 8 != 0) {
                checkIfOccupied(pieceCoordinate - 7, pieceColor);
            }
            if (pieceCoordinate % 8 != 0) {
                checkIfOccupied(pieceCoordinate - 9, pieceColor);
            }
        }
        return threatenList;
    }

    @Override
    public boolean checkIfOccupied(int tileCoordinate, String pieceColor) {
        // if the tile is empty. Pawns will only be able to move forward if the tile is empty
        Tile targetTile = Chessboard.tileList.get(tileCoordinate);
        if (targetTile.pieceColor == null) {
            threatenList.add(tileCoordinate);
            return false;
            // if the tile is not empty
        } else {
            if (!targetTile.pieceColor.equals(pieceColor)) {
                threatenList.add(tileCoordinate);
            }
            return true;
        }
    }
}

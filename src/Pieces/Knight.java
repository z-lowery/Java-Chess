package Pieces;

import java.util.ArrayList;

public class Knight extends Piece implements PieceInterface{

    @Override
    public ArrayList<Integer> calcMoves(int pieceCoordinate, String pieceColor) {
        // North
        if (pieceCoordinate > 15) {
            // Top left
            if (!inLeftColumn(pieceCoordinate)) {
                checkIfOccupied(pieceCoordinate - 17, pieceColor);
            }
            // Top right
            if (!inRightColumn(pieceCoordinate)) {
                checkIfOccupied(pieceCoordinate - 15, pieceColor);
            }
        }

        // East
        if ((!inRightColumn(pieceCoordinate) && ((pieceCoordinate-6) % 8 != 0))) { // "if the knight is not in the 2 right most columns"
            // Top
            if (!inFirstRow(pieceCoordinate)) {
                checkIfOccupied(pieceCoordinate - 6, pieceColor);
            }
            // Bottom
            if (!inLastRow(pieceCoordinate)) {
                checkIfOccupied(pieceCoordinate + 10, pieceColor);
            }
        }

        // South
        if (pieceCoordinate < 48) {
            // Bottom right
            if (!inRightColumn(pieceCoordinate)) {
                checkIfOccupied(pieceCoordinate + 17, pieceColor);
            }
            // Bottom left
            if (!inLastRow(pieceCoordinate)) {
                checkIfOccupied(pieceCoordinate + 15, pieceColor);
            }
        }

        // West
        if (!inLeftColumn(pieceCoordinate) && ((pieceCoordinate-9) % 8 != 0)) { // "if the knight is not in the 2 left most columns"
            // Top
            if (!inFirstRow(pieceCoordinate)) {
                checkIfOccupied(pieceCoordinate - 10, pieceColor);
            }
            // Bottom
            if (!inLastRow(pieceCoordinate)) {
                checkIfOccupied(pieceCoordinate + 6, pieceColor);
            }
        }
        return threatenList;
    }
}

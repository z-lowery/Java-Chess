package Pieces;

import Board.Chessboard;
import java.util.ArrayList;

public class Knight implements Piece{
    ArrayList<Integer> threatenList = new ArrayList<>();

    @Override
    public ArrayList<Integer> calcMoves(int pieceCoordinate, String pieceColor, boolean clickedKing) {
        // north
        if (pieceCoordinate > 15) {
            // top left
            if (pieceCoordinate % 8 != 0) {
                checkIfOccupied(pieceCoordinate - 17, pieceColor);
            }
            // top right
            if ((pieceCoordinate-7) % 8 != 0) {
                checkIfOccupied(pieceCoordinate - 15, pieceColor);
            }
        }

        // east
        if (((pieceCoordinate-7) % 8 != 0) && ((pieceCoordinate-6) % 8 != 0)) { // "if the knight is not in the 2 right most columns"
            // top
            if (pieceCoordinate > 7) {
                checkIfOccupied(pieceCoordinate - 6, pieceColor);
            }
            // bottom
            if (pieceCoordinate < 56) {
                checkIfOccupied(pieceCoordinate + 10, pieceColor);
            }
        }

        // south
        if (pieceCoordinate < 48) {
            // bottom right
            if ((pieceCoordinate-7) % 8 != 0) {
                checkIfOccupied(pieceCoordinate + 17, pieceColor);
            }
            // bottom left
            if ((pieceCoordinate) % 8 != 0) {
                checkIfOccupied(pieceCoordinate + 15, pieceColor);
            }
        }

        // west
        if ((pieceCoordinate % 8 != 0) && ((pieceCoordinate-9) % 8 != 0)) { // "if the knight is not in the 2 left most columns"
            // top
            if (pieceCoordinate > 7) {
                checkIfOccupied(pieceCoordinate - 10, pieceColor);
            }
            // bottom
            if (pieceCoordinate < 56) {
                checkIfOccupied(pieceCoordinate + 6, pieceColor);
            }
        }

        return threatenList;
    }

    @Override
    public boolean checkIfOccupied(int tileCoordinate, String pieceColor) {
        // if the tile is empty
        if (Chessboard.tileList.get(tileCoordinate).pieceColor == null) {
            threatenList.add(tileCoordinate);
            return false;
            // if the tile is not empty
        } else {
            // if the piece on the tile is an enemy
            if (Chessboard.tileList.get(tileCoordinate).pieceColor != pieceColor) {
                threatenList.add(tileCoordinate);
            } else {
                if (pieceColor.equals("white")) {
                    Chessboard.tileList.get(tileCoordinate).whiteThreatened = true;
                } else {
                    Chessboard.tileList.get(tileCoordinate).blackThreatened = true;
                }
            }
            return true;
        }
    }
}

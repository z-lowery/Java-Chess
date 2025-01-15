package Pieces;

import Board.Chessboard;

import java.util.ArrayList;

public class Bishop extends Piece implements PieceInterface {

    @Override
     public ArrayList<Integer> calcMoves(int pieceCoordinate, String pieceColor) {
        int temp;
        // calc north-east moves
        temp = pieceCoordinate;
        while (temp > 7 && (temp+1) % 8 != 0) {
            temp -= 7;
            if (checkIfOccupied(temp, pieceColor)) {
                break;
            }
        }
        // calc south-east moves
        temp = pieceCoordinate;
        while (temp < 56 && (temp+1) % 8 != 0) {
            temp += 9;
            if (checkIfOccupied(temp, pieceColor)) {
                break;
            }
        }
        // calc south-west moves
        temp = pieceCoordinate;
        while (temp < 56 && (temp) % 8 != 0) {
            temp += 7;
            if (checkIfOccupied(temp, pieceColor)) {
                break;
            }
        }
        // calc north-west moves
        temp = pieceCoordinate;
        while (temp > 7 && (temp) % 8 != 0) {
            temp -= 9;
            if (checkIfOccupied(temp, pieceColor)) {
                break;
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

                /*
                 in the case that a king is on the tile, you still want to threaten squares "past" the king, so
                 we pretend that the king is not there. This code is only here for the rook & bishop as they have
                 continuous movement
                 */
                if (Chessboard.tileList.get(tileCoordinate).piece.equals("king")) {
                    return false;
                }
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

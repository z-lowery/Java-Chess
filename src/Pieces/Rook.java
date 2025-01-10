package Pieces;

import Board.Chessboard;
import java.util.ArrayList;

public class Rook implements Piece {
    ArrayList<Integer> threatenList = new ArrayList<>();

    @Override
    public ArrayList<Integer> calcMoves(int pieceCoordinate, String pieceColor, boolean clickedKing) {
        int temp;

        // calc north moves
        temp = pieceCoordinate;
        while (temp > 7) {
            temp -= 8;
            if (checkIfOccupied(temp, pieceColor)) {
                break;
            }
        }

        // calc east moves
        temp = pieceCoordinate;
        while ((temp-7) % 8 != 0) {
            temp++;
            if (checkIfOccupied(temp, pieceColor)) {
                break;
            }
        }

        // calc west moves
        temp = pieceCoordinate;
        while (temp % 8 != 0) {
            temp--;
            if (checkIfOccupied(temp, pieceColor)) {
                break;
            }
        }

        // calc south moves
        temp = pieceCoordinate;
        while (temp < 56) {
            temp += 8;
            if (checkIfOccupied(temp, pieceColor)) {
                break;
            } else if (clickedKing) {
                if (Chessboard.tileList.get(temp).piece != null) {
                    break;
                }
            }
        }
        return threatenList;
    }

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
                 in the case that a king was clicked on, you still want to threaten squares "past" the king, so
                 we pretend that the king is not there. This code is only here for the rook & bishop as they have
                 continuous movement
                 */
                if (Chessboard.tileList.get(tileCoordinate).piece.equals("king")) {
                    return false;
                }
            // if the piece on the tile is a friendly piece
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

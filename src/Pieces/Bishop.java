package Pieces;

import Board.Chessboard;
import Board.Tile;

import java.util.ArrayList;

public class Bishop extends Piece implements PieceInterface {

    @Override
     public ArrayList<Integer> calcMoves(int pieceCoordinate, String pieceColor) {
        int temp;
        // calc north-east moves
        temp = pieceCoordinate;
        while (temp > 7 && (temp+1) % 8 != 0) {
            temp += TILE_NORTHEAST_OFFSET;
            if (checkIfOccupied(temp, pieceColor) && !checkEnemyKing(pieceCoordinate, temp)) {
                break;
            }
        }
        // calc south-east moves
        temp = pieceCoordinate;
        while (!inLastRow(temp) && (temp+1) % 8 != 0) {
            temp += TILE_SOUTHEAST_OFFSET;
            if (checkIfOccupied(temp, pieceColor) && !checkEnemyKing(pieceCoordinate, temp)) {
                break;
            }
        }
        // calc south-west moves
        temp = pieceCoordinate;
        while (!inLastRow(temp) && (temp) % 8 != 0) {
            temp += TILE_SOUTHWEST_OFFSET;
            if (checkIfOccupied(temp, pieceColor) && !checkEnemyKing(pieceCoordinate, temp)) {
                break;
            }
        }
        // calc north-west moves
        temp = pieceCoordinate;
        while (temp > 7 && (temp) % 8 != 0) {
            temp += TILE_NORTHWEST_OFFSET;
            if (checkIfOccupied(temp, pieceColor) && !checkEnemyKing(pieceCoordinate, temp)) {
                break;
            }
        }

        return threatenList;
    }

    /**
     * Checks if a tile contains an enemy king
     * 
     * @param pieceCoordinate - location of the rook
     * @param tileCoordinate - location of the tile being checked
     * @return true if the tile contains an enemy king, false otherwise
     */
    public boolean checkEnemyKing(int pieceCoordinate, int tileCoordinate){
        Tile tile = Chessboard.tileList.get(tileCoordinate);
        Tile piece = Chessboard.tileList.get(pieceCoordinate);
        return (tile.piece.equals("king")) && !(tile.pieceColor == piece.pieceColor);
    }
}

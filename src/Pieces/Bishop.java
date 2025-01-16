package Pieces;

import java.util.ArrayList;

public class Bishop extends Piece implements PieceInterface {

    @Override
     public ArrayList<Integer> calcMoves(int pieceCoordinate, String pieceColor) {
        int temp;
        // Calc north-east moves
        temp = pieceCoordinate;
        while (!inFirstRow(temp) && !inRightColumn(temp)) {
            temp += TILE_NORTHEAST_OFFSET;
            if (checkIfOccupied(temp, pieceColor)) {
                /* 
                * For pieces with continuous movement - rooks, bishops, and queens - do an additional 
                * check to determine if the piece is an enemy king. If it is, we add the tile immediately 
                * past the king to the threatenList to prevent the king from being able to move there on 
                * its turn. We do this because if it did move there, it would be moving from check 
                * into check, which is not allowed.
                */
                if(checkEnemyKing(pieceCoordinate, temp) && !inFirstRow(temp + TILE_NORTHEAST_OFFSET) && !inRightColumn(temp + TILE_NORTHEAST_OFFSET)){
                    threatenList.add(temp + TILE_NORTHEAST_OFFSET);
                }
                break;
            }
        }

        // Calc south-east moves
        temp = pieceCoordinate;
        while (!inLastRow(temp) && !inRightColumn(temp)) {
            temp += TILE_SOUTHEAST_OFFSET;
            if (checkIfOccupied(temp, pieceColor)) {
                if(checkEnemyKing(pieceCoordinate, temp) && !inFirstRow(temp + TILE_SOUTHEAST_OFFSET) && !inRightColumn(temp + TILE_SOUTHEAST_OFFSET)){
                    threatenList.add(temp + TILE_SOUTHEAST_OFFSET);
                }
                break;
            }
        }

        // Calc south-west moves
        temp = pieceCoordinate;
        while (!inLastRow(temp) && !inLeftColumn(temp)) {
            temp += TILE_SOUTHWEST_OFFSET;
            if (checkIfOccupied(temp, pieceColor)) {
                if(checkEnemyKing(pieceCoordinate, temp) && !inFirstRow(temp + TILE_SOUTHWEST_OFFSET) && !inLeftColumn(temp + TILE_SOUTHWEST_OFFSET)){
                    threatenList.add(temp + TILE_SOUTHWEST_OFFSET);
                }
                break;
            }
        }
        
        // Calc north-west moves
        temp = pieceCoordinate;
        while (!inFirstRow(temp) && !inLeftColumn(temp)) {
            temp += TILE_NORTHWEST_OFFSET;
            if (checkIfOccupied(temp, pieceColor)) {
                if(checkEnemyKing(pieceCoordinate, temp) && !inFirstRow(temp + TILE_NORTHWEST_OFFSET) && !inLeftColumn(temp + TILE_NORTHWEST_OFFSET)){
                    threatenList.add(temp + TILE_NORTHWEST_OFFSET);
                }
                break;
            }
        }

        return threatenList;
    }
}

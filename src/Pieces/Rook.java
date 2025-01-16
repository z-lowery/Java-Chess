package Pieces;

import java.util.ArrayList;

public class Rook extends Piece implements PieceInterface {

    @Override
    public ArrayList<Integer> calcMoves(int pieceCoordinate, String pieceColor) {
        int temp;

        // Calc north moves
        temp = pieceCoordinate;
        while (!inFirstRow(temp)) {
            temp += TILE_NORTH_OFFSET;
            if (checkIfOccupied(temp, pieceColor)) {
                /* 
                 * For pieces with continuous movement - rooks, bishops, and queens - do an additional 
                 * check to determine if the piece is an enemy king. If it is, we add the tile immediately 
                 * past the king to the threatenList to prevent the king from being able to move there on 
                 * its turn. We do this because if it did move there, it would be moving from check 
                 * into check, which is not allowed.
                 */
                if(checkEnemyKing(pieceCoordinate, temp) && !inFirstRow(temp + TILE_NORTH_OFFSET)){
                    threatenList.add(temp + TILE_NORTH_OFFSET);
                }
                break;
            }
        }

        // Calc east moves
        temp = pieceCoordinate;
        while (!inRightColumn(temp)) {
            temp += TILE_EAST_OFFSET;
            if (checkIfOccupied(temp, pieceColor)) {
                if(checkEnemyKing(pieceCoordinate, temp) && !inRightColumn(temp + TILE_EAST_OFFSET)){
                    threatenList.add(temp + TILE_EAST_OFFSET);
                }
                break;
            }
        }

        // Calc west moves
        temp = pieceCoordinate;
        while (!inLeftColumn(temp)) {
            temp += TILE_WEST_OFFSET;
            if (checkIfOccupied(temp, pieceColor)) {
                if(checkEnemyKing(pieceCoordinate, temp) && !inLeftColumn(temp + TILE_WEST_OFFSET)){
                    threatenList.add(temp + TILE_WEST_OFFSET);
                }
                break;
            }
        }

        // Calc south moves
        temp = pieceCoordinate;
        while (!inLastRow(temp)) {
            temp += TILE_SOUTH_OFFSET;
            if (checkIfOccupied(temp, pieceColor)) {
                if(checkEnemyKing(pieceCoordinate, temp) && !inLastRow(temp + TILE_SOUTH_OFFSET)){
                    threatenList.add(temp + TILE_SOUTH_OFFSET);
                }
                break;
            }
        }
        return threatenList;
    }
}

package Pieces;

import java.util.ArrayList;

public class Bishop extends Piece implements PieceInterface {

    @Override
     public ArrayList<Integer> calcMoves(int pieceCoordinate, String pieceColor) {
        int temp;
        // Calc north-east moves
        temp = pieceCoordinate;
        while (temp > 7 && (temp+1) % 8 != 0) {
            temp += TILE_NORTHEAST_OFFSET;
            if(checkEnemyKing(pieceCoordinate, temp)){
                if(!inFirstRow(temp + TILE_NORTHEAST_OFFSET)){threatenList.add(temp + TILE_NORTHEAST_OFFSET);}
            }
            break;
        }
        // Calc south-east moves
        temp = pieceCoordinate;
        while (!inLastRow(temp) && (temp+1) % 8 != 0) {
            temp += TILE_SOUTHEAST_OFFSET;
            if(checkEnemyKing(pieceCoordinate, temp)){
                if(!inFirstRow(temp + TILE_SOUTHEAST_OFFSET)){threatenList.add(temp + TILE_SOUTHEAST_OFFSET);}
            }
            break;
        }
        // Calc south-west moves
        temp = pieceCoordinate;
        while (!inLastRow(temp) && (temp) % 8 != 0) {
            temp += TILE_SOUTHWEST_OFFSET;
            if(checkEnemyKing(pieceCoordinate, temp)){
                if(!inFirstRow(temp + TILE_SOUTHWEST_OFFSET)){threatenList.add(temp + TILE_SOUTHWEST_OFFSET);}
            }
            break;
        }
        // Calc north-west moves
        temp = pieceCoordinate;
        while (temp > 7 && (temp) % 8 != 0) {
            temp += TILE_NORTHWEST_OFFSET;
            if(checkEnemyKing(pieceCoordinate, temp)){
                if(!inFirstRow(temp + TILE_NORTHWEST_OFFSET)){threatenList.add(temp + TILE_NORTHWEST_OFFSET);}
            }
            break;
        }

        return threatenList;
    }
}

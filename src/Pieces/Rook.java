package Pieces;

import Board.Chessboard;
import Board.Tile;

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
                if(checkEnemyKing(pieceCoordinate, temp)){
                    if(!inFirstRow(temp + TILE_NORTH_OFFSET)){threatenList.add(temp + TILE_NORTH_OFFSET);}
                }
                break;
            }
        }

        // Calc east moves
        temp = pieceCoordinate;
        while (!inRightColumn(temp)) {
            temp += TILE_EAST_OFFSET;
            if (checkIfOccupied(temp, pieceColor)) {
                if(checkEnemyKing(pieceCoordinate, temp)){
                    if(!inFirstRow(temp + TILE_EAST_OFFSET)){threatenList.add(temp + TILE_EAST_OFFSET);}
                }
                break;
            }
        }

        // Calc west moves
        temp = pieceCoordinate;
        while (!inLeftColumn(temp)) {
            temp += TILE_WEST_OFFSET;
            if (checkIfOccupied(temp, pieceColor)) {
                if(checkEnemyKing(pieceCoordinate, temp)){
                    if(!inFirstRow(temp + TILE_WEST_OFFSET)){threatenList.add(temp + TILE_WEST_OFFSET);}
                }
                break;
            }
        }

        // Calc south moves
        temp = pieceCoordinate;
        while (!inLastRow(temp)) {
            temp += TILE_SOUTH_OFFSET;
            if (checkIfOccupied(temp, pieceColor)) {
                if(checkEnemyKing(pieceCoordinate, temp)){
                    if(!inFirstRow(temp + TILE_SOUTH_OFFSET)){threatenList.add(temp + TILE_SOUTH_OFFSET);}
                }
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

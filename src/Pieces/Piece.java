package Pieces;

import java.util.ArrayList;

import Board.Chessboard;
import Board.Tile;

public abstract class Piece {
    ArrayList<Integer> threatenList = new ArrayList<>(); // List of tiles the pawn can move to / is threatening
    protected static final int TILE_NORTH_OFFSET = -8; // Offset to get the coordinate of a tile in front of another tile
    protected static final int TILE_SOUTH_OFFSET = 8; // Offset to get the coordinate of a tile behind another tile

    protected static final int TILE_EAST_OFFSET = 1; // Offset to get the coordinate of a tile to the right of another tile
    protected static final int TILE_WEST_OFFSET = -1; // Offset to get the coordinate of a tile to the left of another tile

    protected static final int TILE_NORTHEAST_OFFSET = -7; // Offset to get the coordinate of a tile to the north-east of another tile
    protected static final int TILE_SOUTHEAST_OFFSET = 9; // Offset to get the coordinate of a tile to the south-east of another tile

    protected static final int TILE_NORTHWEST_OFFSET = -9; // Offset to get the coordinate of a tile to the north-west of another tile
    protected static final int TILE_SOUTHWEST_OFFSET = 7; // Offset to get the coordinate of a tile to the south-west of another tile

    /** Returns if the tile is in the first row of the chessboard */
    public boolean inFirstRow(int tileCoordinate){return tileCoordinate < 8;}
    /** Returns if the tile is in the last row of the chessboard */
    public boolean inLastRow(int tileCoordinate){return tileCoordinate > 55;}
    /** Returns if the tile is in the right-most column of the chessboard */
    public boolean inRightColumn(int tileCoordinate){return (tileCoordinate - 7) % 8 == 0;}
    /** Returns if the tile is in the left-most column of the chessboard */
    public boolean inLeftColumn(int tileCoordinate){return (tileCoordinate % 8 == 0);}

    /**
     * Checks if a tile is occupied. If so, it will add the tile to the threaten list if the piece on the tile is an enemy
     * @param tileCoordinate - the coordinate of the tile
     * @param pieceColor - the color of the piece
     * @return - true if the tile is occupied, false otherwise
     */
    public boolean checkIfOccupied(int tileCoordinate, String pieceColor) {
        // If the tile is empty. Pawns will only be able to move forward if the tile is empty
        Tile targetTile = Chessboard.tileList.get(tileCoordinate);
        threatenList.add(tileCoordinate);
        if (targetTile.piece == null) { // if the tile is empty
            return false;
        } else { // If the tile contains a piece
            return true;
        }
    }

    /**
     * Checks if a tile contains an enemy king. This is only by pieces with
     * continuous movement - rooks, bishops, and queens - so that they threaten
     * the square immediately past the enemy king instead of just stopping at it. 
     * This is so that a king can't move to a square that would be threatened 
     * by a piece with continuous movement after the king moves. 
     * 
     * @param pieceCoordinate - location of the piece checking
     * @param tileCoordinate - location of the tile being checked
     * @return true if the tile contains an enemy king, false otherwise
     */
    public boolean checkEnemyKing(int pieceCoordinate, int tileCoordinate){
        Tile tile = Chessboard.tileList.get(tileCoordinate);
        Tile piece = Chessboard.tileList.get(pieceCoordinate);
        return (!(tile.piece == null) && tile.piece.equals("king")) && !(tile.pieceColor == piece.pieceColor);
    }
    
}

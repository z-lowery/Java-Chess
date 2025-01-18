package Pieces;

import Board.*;
import java.awt.*;
import java.util.ArrayList;

public class King extends Piece implements PieceInterface {

    @Override
    public ArrayList<Integer> calcMoves(int pieceCoordinate, String pieceColor) {
        // North
        if (!inFirstRow(pieceCoordinate)) {
            if (!threatenedByEnemy(pieceCoordinate + TILE_NORTH_OFFSET, pieceColor)) {
                checkIfOccupied(pieceCoordinate + TILE_NORTH_OFFSET, pieceColor);
            }
        }
        // North-east
        if (!inFirstRow(pieceCoordinate) && !inRightColumn(pieceCoordinate)) {
            if (!threatenedByEnemy(pieceCoordinate + TILE_NORTHEAST_OFFSET, pieceColor)) {
                checkIfOccupied(pieceCoordinate + TILE_NORTHEAST_OFFSET, pieceColor);
            }
        }
        // East
        if (!inRightColumn(pieceCoordinate)) {
            if (!threatenedByEnemy(pieceCoordinate + TILE_EAST_OFFSET, pieceColor)) {
                checkIfOccupied(pieceCoordinate + TILE_EAST_OFFSET, pieceColor);
            }
        }
        // South-east
        if (!inLastRow(pieceCoordinate) && !inRightColumn(pieceCoordinate)) {
            if (!threatenedByEnemy(pieceCoordinate + TILE_SOUTHEAST_OFFSET, pieceColor)) {
                checkIfOccupied(pieceCoordinate + TILE_SOUTHEAST_OFFSET, pieceColor);
            }
        }
        // South
        if (!inLastRow(pieceCoordinate)) {
            if (!threatenedByEnemy(pieceCoordinate + TILE_SOUTH_OFFSET, pieceColor)) {
                checkIfOccupied(pieceCoordinate + TILE_SOUTH_OFFSET, pieceColor);
            }
        }
        // South-west
        if (!inLastRow(pieceCoordinate) && !inLeftColumn(pieceCoordinate)) {
            if (!threatenedByEnemy(pieceCoordinate + TILE_SOUTHWEST_OFFSET, pieceColor)) {
                checkIfOccupied(pieceCoordinate + TILE_SOUTHWEST_OFFSET, pieceColor);
            }
        }
        // West
        if (!inLeftColumn(pieceCoordinate)) {
            if (!threatenedByEnemy(pieceCoordinate + TILE_WEST_OFFSET, pieceColor)) {
                checkIfOccupied(pieceCoordinate + TILE_WEST_OFFSET, pieceColor);
            }
        }
        // North-west
        if (!inFirstRow(pieceCoordinate) && !inLeftColumn(pieceCoordinate)) {
            if (!threatenedByEnemy(pieceCoordinate + TILE_NORTHWEST_OFFSET, pieceColor)) {
                checkIfOccupied(pieceCoordinate + TILE_NORTHWEST_OFFSET, pieceColor);
            }
        }

        checkCastling(pieceCoordinate, pieceColor); // Check if it is possible for the king to castle 

        return threatenList;
    }

    /**
     * Checks if the king can castle left and/or right based off the following factors:
     *      1) The king is not in check
     *      2) The king and the left and/or right rook have not moved
     *      3) The space between the king and the rook is vacant and not threatened by an enemy piece 
     * 
     * If all conditions are met, the rooks that can castle with the kind will be highlighted. The castling 
     * process itself is handled in the tile class. 
     * 
     * @param pieceCoordinate - coordinate of the tile that the king is on on the board
     * @param kingColor - color of the king 
     */
    public void checkCastling(int pieceCoordinate, String kingColor){
        if (!Chessboard.tileList.get(pieceCoordinate).moved) { // Check that the king has not moved using the move variable on its tile
            int leftRookCord, rightRookCord, kingCord;

            kingCord = pieceCoordinate; // Coordinate of the king
            leftRookCord = kingCord - 3; // Coordinate of the left rook
            rightRookCord = kingCord + 4; // Coordinate of the right rook

            if (!threatenedByEnemy(kingCord, kingColor)) { // Checks that the king would not be castling out of check

                // Checks if castling to the left is possible. If so, highlight the left rook to indicate it is
                int targetTileCord = leftRookCord; // Cord of the left rook
                Tile targetTile = Chessboard.tileList.get(targetTileCord); // Tile containing the left rook

                CastleLeft: if (!targetTile.moved) { // If the left rook has not moved
                    // Makes sure the squares between the king and the rook are vacant and not being threatened by enemy pieces
                    for (int i = leftRookCord + 1; i < kingCord; i++) {
                        targetTileCord = i;
                        targetTile = Chessboard.tileList.get(targetTileCord);
                        if (targetTile.piece != null || threatenedByEnemy(targetTileCord, kingColor)) {
                            break CastleLeft;
                        }
                    }
                    highlightCastlingRook(leftRookCord); // Highlight rook to indicate castling is possible
                }    

                // Does the same for the right took
                targetTileCord = rightRookCord; // Cord of the right rook
                targetTile = Chessboard.tileList.get(targetTileCord); // Tile containing the right rook
                
                CastleRight: if (!targetTile.moved) {
                    for (int i = rightRookCord - 1; i > kingCord; i--) {
                        targetTileCord = i;
                        targetTile = Chessboard.tileList.get(targetTileCord);
                        if (targetTile.piece != null || threatenedByEnemy(targetTileCord, kingColor)) {
                            break CastleRight;
                        }
                    }
                    highlightCastlingRook(rightRookCord); 
                }
            }
        }
    }

    /**
     * Returns if a given tile is threatened by an enemy piece.
     * 
     * @param targetTile - tile being checked
     * @param pieceColor - color of the piece to determine if threatened by an enemy
     * @return - true if given tile is threatened by an enemy piece, false otherwise
     */
    public boolean threatenedByEnemy(int tileCoordinate, String pieceColor){
        if(pieceColor.equals("white")){
            return Chessboard.tileList.get(tileCoordinate).blackThreatened;
        } else {
            return Chessboard.tileList.get(tileCoordinate).whiteThreatened;
        }
    }

    /**
     * Highlight a rook given its coordinate on the board.
     * 
     * @param rookCoordinate - the tile coordinate that the rook is located
     */
    public void highlightCastlingRook(int rookCoordinate) {
        Chessboard.tileList.get(rookCoordinate).highlighted = true;
        Chessboard.tileList.get(rookCoordinate).setBackground(Color.magenta);
    }
}

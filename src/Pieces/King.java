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

        // Castling
        // If the king has not moved
        if (!Chessboard.tileList.get(pieceCoordinate).moved) {
            int leftRookCord, rightRookCord, kingCord;

            kingCord = pieceCoordinate; // coordinate of the king

            leftRookCord = kingCord - 4; // coordinate of the left rook
            rightRookCord = kingCord + 3; // coordinate of the right rook

            Tile targetTile = Chessboard.tileList.get(leftRookCord); // left rook tile
            
            // if the left rook has not moved
            castleLeft: if (!targetTile.moved) {
                // makes sure the king would not be castling out of check
                if (pieceColor.equals("white") && Chessboard.tileList.get(kingCord).blackThreatened
                        || pieceColor.equals("black") && Chessboard.tileList.get(kingCord).whiteThreatened) {
                    break castleLeft;
                }

                // makes sure the squares between the king and the rook are vacant and not being threatened by enemy pieces
                for (int j = leftRookCord + 1; j < kingCord; j++) {
                    targetTile = Chessboard.tileList.get(j);
                    if (targetTile.piece != null
                            // handles the cases where the enemy is threatening a square between the king and the rook
                            || (pieceColor.equals("white") && targetTile.blackThreatened)
                            || (pieceColor.equals("black") && targetTile.whiteThreatened)) {
                        break castleLeft;
                    }
                }
                highlightCastlingRook(leftRookCord);
            }

            targetTile = Chessboard.tileList.get(rightRookCord); // right rook tile
            castleRight: if (!targetTile.moved) {
                // makes sure the king would not be castling out of check
                if (pieceColor.equals("white") && Chessboard.tileList.get(kingCord).blackThreatened
                        || pieceColor.equals("black") && Chessboard.tileList.get(kingCord).whiteThreatened) {
                    break castleRight;
                }
                // makes sure the squares between the king and the rook are vacant and not being threatened by enemy pieces
                for (int j = rightRookCord - 1; j > kingCord; j--) {
                    targetTile = Chessboard.tileList.get(j);
                    if (targetTile.piece != null
                            || (pieceColor.equals("white") && targetTile.blackThreatened)
                            || (pieceColor.equals("black") && targetTile.whiteThreatened)) {
                        break castleRight;
                    }
                }
                highlightCastlingRook(rightRookCord);
            }
        }
        return threatenList;
    }

    public void highlightCastlingRook(int rookCoordinate) {
        Chessboard.tileList.get(rookCoordinate).highlighted = true;
        Chessboard.tileList.get(rookCoordinate).setBackground(Color.magenta);
    }

    public boolean threatenedByEnemy(int tileCoordinate, String pieceColor){
        if(pieceColor.equals("white")){
            return Chessboard.tileList.get(tileCoordinate).blackThreatened;
        } else {
            return Chessboard.tileList.get(tileCoordinate).whiteThreatened;
        }
    }
}

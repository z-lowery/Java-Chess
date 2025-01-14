package Pieces;

import Board.*;
import java.awt.*;
import java.util.ArrayList;

public class King implements Piece {
    ArrayList<Integer> threatenList = new ArrayList<>();

    boolean castled = false;

    @Override
    public ArrayList<Integer> calcMoves(int pieceCoordinate, String pieceColor, boolean clickedKing) {
        // north
        if (pieceCoordinate > 7) {
            checkIfOccupied(pieceCoordinate - 8, pieceColor);
        }
        // north-east
        if (pieceCoordinate > 7 && (pieceCoordinate + 1) % 8 != 0) {
            checkIfOccupied(pieceCoordinate - 7, pieceColor);
        }
        // east
        if ((pieceCoordinate - 7) % 8 != 0) {
            checkIfOccupied(pieceCoordinate + 1, pieceColor);
        }
        // south-east
        if (pieceCoordinate < 56 && ((pieceCoordinate - 7) % 8 != 0)) {
            checkIfOccupied(pieceCoordinate + 9, pieceColor);
        }
        // south
        if (pieceCoordinate < 56) {
            checkIfOccupied(pieceCoordinate + 8, pieceColor);
        }
        // south-west
        if (pieceCoordinate < 56 && (pieceCoordinate) % 8 != 0) {
            checkIfOccupied(pieceCoordinate + 7, pieceColor);
        }
        // west
        if (pieceCoordinate % 8 != 0) {
            checkIfOccupied(pieceCoordinate - 1, pieceColor);
        }
        // north-west
        if (pieceCoordinate > 7 && (pieceCoordinate) % 8 != 0) {
            checkIfOccupied(pieceCoordinate - 9, pieceColor);
        }

        // castling
        if (clickedKing
                && (pieceCoordinate == 4 || pieceCoordinate == 60)
                && (!Chessboard.tileList.get(pieceCoordinate).moved)
        ) {
            int leftRookCord, rightRookCord, kingCord;

            kingCord = pieceCoordinate;

            leftRookCord = kingCord - 4;
            rightRookCord = kingCord + 3;

            Tile targetTile = Chessboard.tileList.get(leftRookCord); // left rook tile
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

    @Override
    public boolean checkIfOccupied(int tileCoordinate, String pieceColor) {
        Tile targetTile = Chessboard.tileList.get(tileCoordinate);

        // if the tile is empty
        if (targetTile.pieceColor == null) {
            if (!targetTile.whiteThreatened && pieceColor.equals("black")) { // if tile is not threatened by white and the king is black
                threatenList.add(tileCoordinate);
            }

            if (!targetTile.blackThreatened && pieceColor.equals("white")) { // if tile is not threatened by black and the king is white
                threatenList.add(tileCoordinate);
            }
            return false;
            // if the tile is not empty
        } else {
            // if the piece on the tile is an enemy
            if (!targetTile.whiteThreatened && pieceColor.equals("black")) { // if tile is not threatened by white and the king is black
                threatenList.add(tileCoordinate);
            }

            if (!targetTile.blackThreatened && pieceColor.equals("white")) { // if tile is not threatened by black and the king is white
                threatenList.add(tileCoordinate);
            }
            return true;
        }
    }
}

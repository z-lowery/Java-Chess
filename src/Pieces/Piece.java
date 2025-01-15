package Pieces;

import java.util.ArrayList;

public abstract class Piece {
    ArrayList<Integer> threatenList = new ArrayList<>(); // List of tiles the pawn can move to (front) OR is threatening (diagonally)
    protected static final int TILE_FORWARD_OFFSET = 8; // Offset to get the coordinate of a tile in front of another tile
}

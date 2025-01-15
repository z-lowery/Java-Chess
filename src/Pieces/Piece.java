package Pieces;

import java.util.ArrayList;

public abstract class Piece {
    ArrayList<Integer> threatenList = new ArrayList<>(); // List of tiles the pawn can move to / is threatening
    protected static final int TILE_FORWARD_OFFSET = -8; // Offset to get the coordinate of a tile in front of another tile
    protected static final int TILE_BACKWARD_OFFSET = 8; // Offset to get the coordinate of a tile behind another tile
}

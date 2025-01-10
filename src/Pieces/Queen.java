package Pieces;

import java.util.ArrayList;

public class Queen implements Piece{
    Rook rook = new Rook();
    Bishop bishop = new Bishop();

    @Override
    public ArrayList<Integer> calcMoves(int pieceCoordinate, String pieceColor, boolean clickedKing) {
        ArrayList<Integer> highlightList = new ArrayList<>();

        // Call the rook and bishop calc move methods
        highlightList.addAll(rook.calcMoves(pieceCoordinate, pieceColor, clickedKing));
        highlightList.addAll(bishop.calcMoves(pieceCoordinate, pieceColor, clickedKing));

        return highlightList;
    }

    // this will not be used by the queen as she just uses the rook and bishop ones
    @Override
    public boolean checkIfOccupied(int tileCoordinate, String pieceColor) {
        return false;
    }
}

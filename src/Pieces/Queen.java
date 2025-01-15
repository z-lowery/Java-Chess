package Pieces;

import java.util.ArrayList;

public class Queen extends Piece implements PieceInterface{
    Rook rook = new Rook();
    Bishop bishop = new Bishop();

    @Override
    public ArrayList<Integer> calcMoves(int pieceCoordinate, String pieceColor) {
        ArrayList<Integer> highlightList = new ArrayList<>();

        // Call the rook and bishop calc move methods
        highlightList.addAll(rook.calcMoves(pieceCoordinate, pieceColor));
        highlightList.addAll(bishop.calcMoves(pieceCoordinate, pieceColor));

        return highlightList;
    }
}

import Board.Chessboard;

import java.io.IOException;

/*
The following program is meant to be a playable game of chess in all but 3 ways:
    - Pawns can not En passant.
    - The game still allows you to move any piece if the king is in check OR checkmate.
    - There is no visible indication when the king is in check or checkmate. The king can not
      move to spots that are threatened by enemy pieces, though.
 */
public class App {
    public static void main(String[] args) throws IOException {
        new Chessboard();
    }
}

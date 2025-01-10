package Board;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class Chessboard extends JFrame{
    JFrame frame;
    JPanel panel;
    public static ArrayList<Tile> tileList = new ArrayList<>(64);

    public static ArrayList<Integer> pieceLocations = new ArrayList<>();

    Tile tile;

    // default style for the chessboard
    public Chessboard() throws IOException {
        frame = new JFrame(); // create a JFrame
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.setLayout(new GridBagLayout()); // Use GridBagLayout for centering
        frame.setTitle("Chess");
        frame.setLocationRelativeTo(null); // Center the JFrame on screen

        panel = new JPanel(); // create a JPanel
        panel.setLayout(new GridLayout(8, 8)); // chessboard dimensions
        panel.setBorder(new LineBorder(Color.red));
        panel.setPreferredSize(new Dimension(450, 450));

        frame.add(panel);

        // adds squares to the chessboard (panel) and places them in an array
        ArrayList<String> pieceList = getPieces();

        int tileCoordinate = 0;
        int count = 0;
        boolean isTan = true; // var manipulated to change the colors of the tiles placed on the board
        String pieceColor = "black"; // colors the pieces that are places on the board

        for (int i = 0; i < 8; i++) {
            // changes the color of the pieces that gets placed on the board
            if (i >= 6) {
                pieceColor = "white";
            }
            // adds all tiles and pieces to the board
            for (int j = 0; j < 8; j++) {
                // if a piece should be on the tile
                if (i <= 1 || i >= 6) {
                    tile = new Tile(tileCoordinate, isTan, pieceList.get(count), pieceColor);
                    pieceLocations.add(tileCoordinate);
                    count++;
                // if the tile should be empty
                } else {
                    tile = new Tile(tileCoordinate, isTan, null, null);
                }
                tileList.add(tile);
                panel.add(tile);
                isTan = !isTan; // var is cycled so that every tile in a row is checkered
                tileCoordinate++;
            }
            isTan = !isTan; // var is cycled so that every tile in a column is checkered
        }
        frame.setVisible(true);
        ///////////////////////////////////////////////
    }

    private static ArrayList<String> getPieces() {
        ArrayList<String> pieceList = new ArrayList<String>();
        pieceList.add("rook");
        pieceList.add("knight");
        pieceList.add("bishop");
        pieceList.add("queen");
        pieceList.add("king");
        pieceList.add("bishop");
        pieceList.add("knight");
        pieceList.add("rook");
        for (int j = 0; j < 16; j++) {
            pieceList.add("pawn");
        }
        pieceList.add("rook");
        pieceList.add("knight");
        pieceList.add("bishop");
        pieceList.add("queen");
        pieceList.add("king");
        pieceList.add("bishop");
        pieceList.add("knight");
        pieceList.add("rook");
        return pieceList;
    }
}
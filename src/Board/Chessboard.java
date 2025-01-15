package Board;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

public class Chessboard extends JFrame{
    JFrame frame;
    JPanel panel;

    public static ArrayList<Tile> tileList = new ArrayList<>(64); // Stores all tiles and their information 
    public static ArrayList<Integer> pieceLocations = new ArrayList<>(); // Stores locations of tiles with pieces on them 

    protected final static int ROW_LENGTH = 8;
    protected final static int COL_LENGTH = 8;

    // Default style for the chessboard
    public Chessboard() throws IOException {
        frame = new JFrame(); // Create a JFrame
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.setLayout(new GridBagLayout()); // Use GridBagLayout for centering
        frame.setTitle("Chess");
        frame.setLocationRelativeTo(null); // Center the JFrame on screen

        panel = new JPanel(); // Create a JPanel
        panel.setLayout(new GridLayout(ROW_LENGTH, COL_LENGTH)); // Chessboard dimensions
        panel.setPreferredSize(new Dimension(450, 450));

        frame.add(panel);

        addTiles(this, panel);
        
        frame.setVisible(true);
    }

    private static void addTiles(Chessboard chessboard, JPanel panel){
        Stack<String> pieceList = getPieces(); // Get list of chess pieces in order of where they appear on the board

        int tileCoordinate = 0;
        boolean isTan = true; // Manipulated to change the colors of the tiles placed on the board
        String pieceColor = "black"; // Colors the pieces that are placed on the board

        Tile tile = null; // Tile being added 
        for (int ROW_NUM = 0; ROW_NUM < ROW_LENGTH; ROW_NUM++) { // For each row
            
            if (ROW_NUM >= 6) { pieceColor = "white";} // On row 6, change pieces being added to white

            // Adds all tiles and pieces to the board
            for (int COL_NUM = 0; COL_NUM < 8; COL_NUM++) {
                if (ROW_NUM <= 1 || ROW_NUM >= 6) { // Pieces are only added on the first and last two rows
                    try {
                        tile = new Tile(tileCoordinate, isTan, pieceList.pop(), pieceColor);
                        pieceLocations.add(tileCoordinate);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else { // If the tile should be empty
                    try {
                        tile = new Tile(tileCoordinate, isTan, null, null);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                tileList.add(tile);
                panel.add(tile);
                isTan = !isTan; // Var is cycled so that every tile in a row is checkered
                tileCoordinate++;
            }
            isTan = !isTan; // Var is cycled so that every tile in a column is checkered
        }
    }

    private static Stack<String> getPieces() {
        Stack<String> pieceList = new Stack<String>();
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
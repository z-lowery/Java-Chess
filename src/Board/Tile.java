package Board;

import Pieces.*;

import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;


/*
 * The Tile class is used to create the tiles on the board. Each tile is a JButton and implements the ActionListener interface
 * to respond to user actions. Depending on the last action of the user and the state of the tile when it is clicked on, different
 * events can happen. The class has a number of variables that store information about the tile, such as the piece
 * on the tile, the color of the piece, if the tile is highlighted, and if the tile is threatened by a piece of a particular
 * color.
 */
public class Tile extends JButton implements ActionListener {
    // Every tile has the following:
    public final int tileCoordinate; // Stores the location of the tile. Final = var can't be changed after first being set
    public boolean isTan; // Used to set and store the color of the tile. Used to construct board and clear highlights.

    public String piece; // If null, tile does not have a piece on it. Else, string indicates what piece is on the tile
    public String pieceColor; // Color of the piece on the tile

    public boolean highlighted = false; // tracks if the tile is highlighted or not. If highlighted, the tile can be moved to.

    public boolean whiteThreatened = false; // Tracks if the tile is threatened by a white piece on the board
    public boolean blackThreatened = false; // Tracks if the tile is threatened by a black piece on the board

    public boolean moved; // Indicates if a piece has moved from the tile. This variable only used to determine if a king can castle

    public static int lastCord; // Stores the location fo the last tile clicked on

    // Declaration for a tile:
    public Tile(int tileCoordinate, boolean isTan, String piece, String pieceColor) throws IOException {
        this.tileCoordinate = tileCoordinate;
        this.isTan = isTan;
        this.piece = piece;
        this.pieceColor = pieceColor;
        this.moved = false; // pieces on tiles have not moved at the beginning of the game.

        this.setBorder(new LineBorder(Color.black, 1));

        // set the icons of the piece
        setImage(this.piece);

        // Sets the color of the tile
        if (isTan) {
            this.setBackground(new Color(252, 211, 154)); // tan color
        } else {
            this.setBackground(Color.lightGray);
        }
        this.addActionListener(this); // runs code if the tile is clicked on.
    }

    /**
     * This method is called when the tile is clicked on. It will respond to the user's actions by highlighting
     * tiles that the piece on the tile can move to or moving the piece to a new tile.
     * @param e - the action event that is triggered when the tile is clicked on
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        /* DEBUG HELPER
         * The following print statements are used to help debug 
         * the program by printing out information about the tile
         */

        /* System.out.println("Tile location: " + tileCoordinate);
        System.out.println("moved = " + moved);
        System.out.println("black threatened = " + blackThreatened);
        System.out.println("white threatened = " + whiteThreatened);
        System.out.println("piece color = " + pieceColor); */

        /* 
         * If the tile clicked on is NOT highlighted. This would mean that you clicked on
         * an empty tile OR a different piece to move.
         */ 
        if (!this.highlighted) {
            resetTileColors(); // resets the tile colors by clearing highlighted tiles.

            /* 
             * In the case that the user clicked on a different piece to move, it is important to store the below
             * data in case the next tile the user clicked on IS highlighted as these variables will be used.
             */
            lastCord = this.tileCoordinate;

            // Sets clickedKing variable to true if the tile that was clicked on has a king on it
            if (this.piece != null) {
                if (this.piece.equals("king")) {
                    calcThreats();
                }
            }

            /* 
             * Creates a list of tiles that can be highlighted based on how a piece on the clicked tile moves
             * by calling that pieces' respective calcMoves() method.
             */
            ArrayList<Integer> highlightTiles = calcMovesOfPiece(this.piece, this.tileCoordinate, this.pieceColor);

            // Stores the color of the piece that was clicked on to determine what pieces it can capture
            String pieceColor = this.pieceColor;

            if (piece != null) {
                for (int i = 0; i < highlightTiles.size(); i++) {
                    Tile targetTile = Chessboard.tileList.get(highlightTiles.get(i));
                    if (targetTile.pieceColor != pieceColor) { // if the target tile is a friendly piece, then we don't highlight it
                        boolean isPawn = this.piece.equals("pawn"); // checks if the piece is a pawn

                        /* 
                         * Handles the case when the piece clicked on is a pawn. This is done because
                         * pawns capture and move to different tiles on the board. This is unlike other pieces
                         * that capture and move to the same tiles.
                         */
                        if(isPawn){
                            // If the target tile is in front of the pawn
                            if(pawnForward(this, targetTile)){ 
                                targetTile.setBackground(Color.magenta);
                            // If the target tile is diagonal to the pawn and has an enemy piece
                            } else if (targetTile.piece != null) { 
                                targetTile.setBackground(Color.red);
                            }
                        }

                        // Handles any other piece 
                        else if (targetTile.piece == null) { // "if the tile is empty"
                                targetTile.setBackground(Color.magenta);
                        } else { // if the tile contains an enemy piece
                            targetTile.setBackground(Color.red);
                        }

                        if(targetTile.getBackground() == Color.magenta || targetTile.getBackground() == Color.red){
                            targetTile.highlighted = true;
                        }
                    }
                }
            }
        } 
        
        // If the tile that was clicked on IS highlighted, then the piece will move to that tile, possibly capturing a piece
        else {
            if (this.getBackground() == Color.magenta || this.getBackground() == Color.RED) { // Error handling 
                Tile lastTile = Chessboard.tileList.get(lastCord); // tile that was clicked on before the current tile

                // Handles the case when a king is castling
                if (lastTile.piece != null && piece != null && lastTile.piece.equals("king") && piece.equals("rook")) {
                    int diff = 64; // Variable to store the difference between the king and rook tile coordinates
                    int newRookCord = 64, newKingCord = 64; // Set values to impossible tile coordinates
                    int kingCord = lastCord; // Location of the king that is castling
                    String pieceColor = lastTile.pieceColor; // Color of the pieces that are castling

                    Tile castlingKing = Chessboard.tileList.get(lastCord); // King that is castling
                    Tile castlingRook = Chessboard.tileList.get(tileCoordinate); // Rook that is castling

                    // Determines the locations of the rook and king after castling
                    if (tileCoordinate == 56 || tileCoordinate == 0) {
                        diff = 3; // Castling to the left
                        newRookCord = kingCord - 1;
                        newKingCord = kingCord - 2;
                    } else if (tileCoordinate == 63 || tileCoordinate == 7) {
                        diff = -2; // Castling to the right
                        newRookCord = kingCord + 1;
                        newKingCord = kingCord + 2;
                    }

                    /* 
                     * Set the icons, piece, & piece color of the tiles that will be occupied by the king
                     * and rook after castling 
                     */
                    try {
                        Chessboard.tileList.get(newRookCord).piece = "rook";
                        Chessboard.tileList.get(newRookCord).pieceColor = pieceColor;
                        Chessboard.tileList.get(newRookCord).setImage("rook");

                        Chessboard.tileList.get(newKingCord).piece = "king";
                        Chessboard.tileList.get(newKingCord).pieceColor = pieceColor;
                        Chessboard.tileList.get(newKingCord).setImage("king");
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    // Remove the old king and rook from the board
                    Chessboard.pieceLocations.remove(Chessboard.pieceLocations.indexOf(castlingRook.tileCoordinate)); // Remove the old location of the rook
                    Chessboard.pieceLocations.add(castlingRook.tileCoordinate + diff); // Add the new location of the rook
                    resetTile(castlingRook);

                    Chessboard.pieceLocations.remove(Chessboard.pieceLocations.indexOf(castlingKing.tileCoordinate)); // Remove the old location of the king
                    resetTile(castlingKing);

                    // Add the new location of the king based off of the difference between the king and rook
                    if (diff == 3) { // Castling to the left
                        Chessboard.pieceLocations.add(kingCord - 2);
                    } else if(diff == -2) { // Castling to the right
                        Chessboard.pieceLocations.add(kingCord + 2);
                    }
                } 
                
                // Handles any other case where a piece is moving 
                else {
                    // Set the icon of the tile that the piece is moving to
                    try {
                        this.setImage(lastTile.piece);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    /* 
                     * This will update the piece location list to accommodate the piece moving. 
                     * If you are taking a piece, then a piece is still on that tile so no addition 
                     * needs to be made. Just a removal of the tile that the piece is moving from.
                     */
                    if (this.getBackground() == Color.magenta) {
                        Chessboard.pieceLocations.add(this.tileCoordinate);
                    }
                    Chessboard.pieceLocations.remove((Integer) lastCord); // Remove the location of the tile the piece was moved from

                    /*
                     * Piece moves by taking the information from the last tile the piece is moving from
                     * and moving it to the new tile that was clicked on (where the piece is moving to).
                    */
                    this.setIcon(lastTile.getIcon());
                    this.piece = lastTile.piece;
                    this.pieceColor = lastTile.pieceColor;                    
                    this.moved = true;

                    // reset lastTile variables
                    resetTile(lastTile);
                }
            } else {
                System.out.println("THERE WAS AN ERROR! The tile that was just clicked on was " +
                        "labeled as being highlighted, but the background isn't one of the two " +
                        "colors it should be.");
            }
            lastCord = 64; // reset last tile coordinate by setting it to an impossible value
            resetTileColors(); // remove all highlights from tiles 
        }
        /* DEBUG HELPER
        for (int i = 0; i < Chessboard.tileList.size(); i++) {
            Tile testTile = Chessboard.tileList.get(i);
            if (testTile.blackThreatened && testTile.whiteThreatened) {
                testTile.setBorder(new LineBorder(Color.yellow, 4));
            } else if (testTile.blackThreatened) {
                testTile.setBorder((new LineBorder(Color.blue, 4)));
            } else if (testTile.whiteThreatened) {
                testTile.setBorder((new LineBorder(Color.red, 4)));
            } else {
                testTile.setBorder((new LineBorder(Color.green, 4)));
            }
        }
        */

    }
    
    public void resetTile(Tile tile){
        tile.setIcon(null);
        tile.piece = null;
        tile.pieceColor = null;
        tile.moved = true;
    }

    // reset all tile colors on the board. Used to clear highlighted tiles.
    public void resetTileColors() {
        Tile tile;
        for (int i = 0; i < Chessboard.tileList.size(); i++) {
            tile = Chessboard.tileList.get(i);
            if (tile.getBackground() == Color.magenta || tile.getBackground() == Color.RED) {
                tile.highlighted = false;
                if (tile.isTan) {
                    tile.setBackground(new Color(252, 211, 154)); // tan color
                } else {
                    tile.setBackground(Color.lightGray);
                }
            }
        }
    }

    // resets threatened status of all tiles on the board
    public void clearThreats() {
        Tile tile;
        for (int i = 0; i < Chessboard.tileList.size(); i++) {
            tile = Chessboard.tileList.get(i);
            tile.whiteThreatened = false;
            tile.blackThreatened = false;
        }
    }

    /*
    method that calculates ALL threats on the board from both white and black pieces for the purposes of
    determining where the kings can move
     */
    public void calcThreats() {
        clearThreats(); // clears the current threats on the board

        /*
         update what tiles are being threatened by other pieces by iterating through Chessboard.pieceLocations ArrayList. List
         stores the locations of all the pieces on the board.
         */
        for (int i = 0; i < Chessboard.pieceLocations.size(); i++) {
            // arrays to store tiles that are threatened by either color
            ArrayList<Integer> whiteThreatenedTiles = new ArrayList<>();
            ArrayList<Integer> blackThreatenedTiles = new ArrayList<>();

            // saves the tile the current piece being looked at is on
            Tile tile = Chessboard.tileList.get(Chessboard.pieceLocations.get(i));

            /*
            depending on the color of the piece, the program will calculate and store the locations of the tiles
            the piece is threatening in the respective arrays
             */
            if (tile.pieceColor != null) {
                if (tile.pieceColor.equals("white")) {
                    whiteThreatenedTiles.addAll(calcMovesOfPiece(tile.piece, tile.tileCoordinate, tile.pieceColor));
                } else if (tile.pieceColor.equals("black")) {
                    blackThreatenedTiles.addAll(calcMovesOfPiece(tile.piece, tile.tileCoordinate, tile.pieceColor));
                }

                for (int j = 0; j < whiteThreatenedTiles.size(); j++) {
                    Tile targetTile = Chessboard.tileList.get(whiteThreatenedTiles.get(j));
                    if ((!tile.piece.equals("pawn")) || !pawnForward(tile, targetTile)) {
                        targetTile.whiteThreatened = true;
                    }
                }
                for (int j = 0; j < blackThreatenedTiles.size(); j++) {
                    Tile targetTile = Chessboard.tileList.get(blackThreatenedTiles.get(j));
                    if ((!tile.piece.equals("pawn")) || !pawnForward(tile, targetTile)) {
                        targetTile.blackThreatened = true;
                    }
                }
            }
        }
    }

    public boolean pawnForward(Tile tile, Tile targetTile) {
            if ((tile.tileCoordinate + 8 == targetTile.tileCoordinate)
            || (tile.tileCoordinate + 16 == targetTile.tileCoordinate)
            || (tile.tileCoordinate - 8 == targetTile.tileCoordinate)
            || (tile.tileCoordinate - 16 == targetTile.tileCoordinate)){
                return true;
            } else {
                return false;
            }
    } 

    // calculates the available moves of a piece by calling their calcMoves() method which returns a list of tiles
    // they are threatening
    public ArrayList<Integer> calcMovesOfPiece(String piece, int tileCoordinate, String pieceColor) {
        ArrayList<Integer> moveList = new ArrayList<>(0);

        if (piece != null) {
            if (piece.equals("rook")) {
                Rook rook = new Rook();
                moveList = rook.calcMoves(tileCoordinate, pieceColor);
            }
            if (piece.equals("knight")) {
                Knight knight = new Knight();
                moveList = knight.calcMoves(tileCoordinate, pieceColor);
            }
            if (piece.equals("bishop")) {
                Bishop bishop = new Bishop();
                moveList = bishop.calcMoves(tileCoordinate, pieceColor);
            }
            if (piece.equals("queen")) {
                Queen queen = new Queen();
                moveList = queen.calcMoves(tileCoordinate, pieceColor);
            }
            if (piece.equals("king")) {
                King king = new King();
                moveList = king.calcMoves(tileCoordinate, pieceColor);
            }
            if (piece.equals("pawn")) {
                Pawn pawn = new Pawn();
                moveList = pawn.calcMoves(tileCoordinate, pieceColor);
            }
        }
        return moveList;
    }

    // method takes in a string called "label" that indicates the piece on the tile and subsequently the icon to use
    public void setImage (String label) throws IOException {
        // Gives chess icons to the correct tiles
        if (label != null) {
            if (pieceColor == "black") {
                this.setIcon(fitImage("src\\assets\\" + label + "_black.png"));
            } else {
                this.setIcon(fitImage("src\\assets\\" + label + "_white.png"));
            }
        } 
    }

    // scales the piece image to the center of the tile
    public Icon fitImage (String filePath) throws IOException {
        // Load the image
        BufferedImage img = ImageIO.read(new File(filePath));

        // Resize the image to fit the button (say, 50x50 pixels)
        Image scaledImg = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH);

        // Create an ImageIcon from the scaled image
        ImageIcon icon = new ImageIcon(scaledImg);

        return icon;
    }
}

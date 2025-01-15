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
                throw new RuntimeException("THERE WAS AN ERROR! The tile that was just clicked on was " +
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

    /** Resets threatened status' of all tiles on the board */
    public void clearThreats() {
        Tile tile;
        for (int i = 0; i < Chessboard.tileList.size(); i++) {
            tile = Chessboard.tileList.get(i);
            tile.whiteThreatened = false;
            tile.blackThreatened = false;
        }
    }

    /**
     * Determines what tiles are threatened by white and black pieces, modifying the tile's
     * "whiteThreatened" and "blackThreatened" variables respectively. This is used to
     * determine where a kings can move on the board. 
     */
    public void calcThreats() {
        clearThreats(); // Clears the current threats on the board

        /*
         * Update what tiles are being threatened by other pieces by iterating through Chessboard.pieceLocations ArrayList. List
         * stores the locations of all the pieces on the board.
         */
        for (int i = 0; i < Chessboard.pieceLocations.size(); i++) {
            // Stores the tile the piece being looked at is on
            Tile tile = Chessboard.tileList.get(Chessboard.pieceLocations.get(i));

            /*
             * Depending on the color of the piece, the program will calculate and store the locations of the tiles
             * the piece is threatening in the respective arrays
             */
            ArrayList<Integer> threatenedTiles = new ArrayList<>();  // Array to store tiles that are being threatened by a piece
            if (tile.piece != null) {
                // Calculate the tiles that are threatened by the piece
                threatenedTiles = calcMovesOfPiece(tile.piece, tile.tileCoordinate, tile.pieceColor);
                
                // Update the tiles that are threatened by the piece
                for (int j = 0; j < threatenedTiles.size(); j++) { // Iterate through the tiles that are threatened by the piece
                    Tile targetTile = Chessboard.tileList.get(threatenedTiles.get(j));
                    boolean isPawn = tile.piece.equals("pawn"); // checks if the piece is a pawn

                    /*
                     * Updates the tiles that are threatened by the piece. If the piece is a pawn, then the program
                     * will check if the target tile is in front of the pawn or diagonal to the pawn. If the target tile
                     * is diagonal to the pawn, then the tile is threatened. If the target tile is in front of the pawn,
                     * then the tile will NOT be threatened by the pawn. This is because pawns can only capture diagonally.
                     */
                    if ((!isPawn) || !pawnForward(tile, targetTile)) {
                        if (tile.pieceColor.equals("white")) {
                            targetTile.whiteThreatened = true;
                        } else if (tile.pieceColor.equals("black")) {
                            targetTile.blackThreatened = true;
                        } else {
                            throw new RuntimeException("The piece color of the tile being looked at is not white or black.");
                        }
                    }
                }
            } else {
                throw new RuntimeException("A tile in pieceLocationList indicates there is no piece on it.");
            }
        }
    }

    /**
     * Determines if targetTile is in front of a pawn. This is used to determine the state
     * of the targetTile given its location relative to the pawn. See the calcMoves() method
     * in the pawn class for more information as this method is built around that method.
     * @param tileWithPawn - the tile the pawn is on
     * @param targetTile - the tile the pawn is moving to
     * @return - true if the target tile is in front of the pawn, false otherwise
     */
    public boolean pawnForward(Tile tileWithPawn, Tile targetTile) {
            if ((tileWithPawn.tileCoordinate + 8 == targetTile.tileCoordinate)
            || (tileWithPawn.tileCoordinate + 16 == targetTile.tileCoordinate)
            || (tileWithPawn.tileCoordinate - 8 == targetTile.tileCoordinate)
            || (tileWithPawn.tileCoordinate - 16 == targetTile.tileCoordinate)){
                return true;
            } else {
                return false;
            }
    } 

    /**
     * Calculates the moves of a piece by calling the calcMoves() method of the respective piece class. The method
     * returns a list of tiles that the piece can move to / is threatening. 
     * @param piece - the piece that is having the calculation done
     * @param tileCoordinate - the location of the piece on the board
     * @param pieceColor - the color of the piece
     * @return - a list of tiles that the piece can move to / is threatening. 
     */
    public ArrayList<Integer> calcMovesOfPiece(String piece, int tileCoordinate, String pieceColor) {
        ArrayList<Integer> moveList = new ArrayList<>(0);

        // Calls the calcMoves() method of the respective piece class to obtain a list of tiles that the piece can move to
        if (piece != null) {
            switch (piece) {
            case "rook":
                moveList = new Rook().calcMoves(tileCoordinate, pieceColor);
                break;
            case "knight":
                moveList = new Knight().calcMoves(tileCoordinate, pieceColor);
                break;
            case "bishop":
                moveList = new Bishop().calcMoves(tileCoordinate, pieceColor);
                break;
            case "queen":
                moveList = new Queen().calcMoves(tileCoordinate, pieceColor);
                break;
            case "king":
                moveList = new King().calcMoves(tileCoordinate, pieceColor);
                break;
            case "pawn":
                moveList = new Pawn().calcMoves(tileCoordinate, pieceColor);
                break;
            }
        }
        return moveList;
    }

    /**
     * Sets the image of the piece on the tile. The image is set based on the piece's color and type.
     * @param label - the type of piece that is on the tile
     * @throws IOException - if the image file is not found
     */
    public void setImage (String label) throws IOException {        
        if (label != null) {
            if (pieceColor == "black") {
                this.setIcon(fitImage("src\\assets\\" + label + "_black.png"));
            } else {
                this.setIcon(fitImage("src\\assets\\" + label + "_white.png"));
            }
        } 
    }

    /**
     * Resizes the image of the piece on the tile to fit the tile. The image is resized to 50x50 pixels.
     * @param filePath - the file path of the image
     * @return - the resized image
     * @throws IOException - if the image file is not found
     */
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

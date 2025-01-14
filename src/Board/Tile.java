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

    public static String lastPiece; // Stores the last piece that was clicked on (empty/no piece if null)
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
        // DEBUG HELPER
        System.out.println("Tile location: " + tileCoordinate);
        System.out.println("moved = " + moved);
        System.out.println("black threatened = " + blackThreatened);
        System.out.println("white threatened = " + whiteThreatened);
        System.out.println("piece color = " + pieceColor);

        /*
        If the tile clicked on is NOT highlighted. This would mean that you clicked on an empty tile OR a different
        piece to move.
         */
        if (!this.highlighted) {
            resetTileColors(); // resets the tile colors by clearing highlighted tiles.

            /*
            In the case that the user clicked on a different piece to move, it is important to store the below
            data in case the next tile the user clicked on IS highlighted as these variables will be used.
            */
            lastPiece = this.piece; // piece = null if clicked an empty tile. Else, stores the piece on the tile.
            lastCord = this.tileCoordinate;

            // sets clickedKing variable to true if the tile that was clicked on has a king on it
            if (this.piece != null) {
                if (this.piece.equals("king")) {
                    calcThreats();
                }
            }

            /*
             creates a list of tiles that can be highlighted based on how a piece on the clicked tile moves
             by calling that pieces' respective calcMoves() method
             */
            ArrayList<Integer> highlightTiles = calcMovesOfPiece(this.piece, this.tileCoordinate, this.pieceColor);

            // stores the color of the piece that was clicked on to determine what pieces it can capture
            String pieceColor = this.pieceColor;

            if (piece != null) {
                for (int i = 0; i < highlightTiles.size(); i++) {
                    Tile targetTile = Chessboard.tileList.get(highlightTiles.get(i));
                    if (targetTile.pieceColor != pieceColor) { // if the target tile is a friendly piece, then we don't highlight it
                        boolean isPawn = this.piece.equals("pawn"); // checks if the piece is a pawn

                        // Handles the case when the piece clicked on is a pawn. This is done because
                        // pawns capture and move to different tiles on the board. This is unlike other pieces
                        // that capture and move to the same tiles.
                        if(isPawn){
                            // if the target tile is in front of the pawn
                            if(pawnForward(this, targetTile)){ 
                                targetTile.setBackground(Color.magenta);
                            // if the target tile is diagonal to the pawn and has an enemy piece
                            } else if (targetTile.piece != null) { 
                                targetTile.setBackground(Color.red);
                            }
                        }

                        // Handles any other piece 
                        else if (targetTile.piece == null) { // "if the tile is empty"
                                targetTile.setBackground(Color.magenta);
                        } else { // else if the tile contains an enemy piece
                            targetTile.setBackground(Color.red);
                        }

                        if(targetTile.getBackground() == Color.magenta || targetTile.getBackground() == Color.red){
                            targetTile.highlighted = true;
                        }
                    }
                }
            }

            /*
            If the tile that was clicked on IS highlighted
             */
        } else {
            if (this.getBackground() == Color.magenta || this.getBackground() == Color.RED) {
                Tile lastTile = Chessboard.tileList.get(lastCord);

                    /* ==================================== CASTLING STUFF ============================================
                changes the 'moved' variable to track if any of the rooks or kings had made its first move for the
                purposes of allowing or disallowing castling
                    */
                // handling of whether the rook moved yet or not is handled in the king class
                if (lastPiece != null && piece != null && lastPiece.equals("king") && piece.equals("rook")) {
                    int diff = 64, newRookCord = 64, newKingCord = 64;
                    int kingCord = 64;
                    String color = lastTile.pieceColor;

                    if (lastTile.pieceColor.equals("white")) {
                        kingCord = 60;
                    } else if (lastTile.pieceColor.equals("black")) {
                        kingCord = 4;
                    }

                    Tile castlingKing = Chessboard.tileList.get(kingCord); // king that is casting
                    Tile castlingRook = Chessboard.tileList.get(tileCoordinate); // rook that is casting

                    if (tileCoordinate == 56 || tileCoordinate == 0) {
                        diff = 3;
                        newRookCord = kingCord - 1;
                        newKingCord = kingCord - 2;
                    } else if (tileCoordinate == 63 || tileCoordinate == 7) {
                        diff = - 2;
                        newRookCord = kingCord + 1;
                        newKingCord = kingCord + 2;
                    }

                    Chessboard.tileList.get(newRookCord).piece = "rook";
                    Chessboard.tileList.get(newRookCord).pieceColor = color;
                    try {
                        Chessboard.tileList.get(newRookCord).setImage("rook");
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    Chessboard.tileList.get(newKingCord).piece = "king";
                    Chessboard.tileList.get(newKingCord).pieceColor = color;
                    try {
                        Chessboard.tileList.get(newKingCord).setImage("king");
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    if (castlingRook != null) {
                        castlingRook.setIcon(null);
                        castlingRook.piece = null;
                        castlingRook.pieceColor = null;
                        castlingRook.moved = true;
                        Chessboard.pieceLocations.remove((Integer) castlingRook.tileCoordinate);
                        Chessboard.pieceLocations.add(castlingRook.tileCoordinate + diff);
                    } else {
                        System.out.println("Castling rook is NULL!!!!");
                    }

                    castlingKing.setIcon(null);
                    castlingKing.piece = null;
                    castlingKing.pieceColor = null;
                    castlingKing.moved = true;
                    Chessboard.pieceLocations.remove((Integer) castlingKing.tileCoordinate);

                    if (diff == 3) {
                        if (kingCord == 60) {
                            Chessboard.pieceLocations.add(58);
                        } else if (kingCord == 4) {
                            Chessboard.pieceLocations.add(2);
                        }
                    } else { // dif = -2
                        if (kingCord == 60) {
                            Chessboard.pieceLocations.add(62);
                        } else if (kingCord == 4) {
                            Chessboard.pieceLocations.add(6);
                        }
                    }
                } else {
                    try {
                        this.setImage(lastPiece);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                        /*
                            piece is being moved, therefore the location of the piece is changing. This will update the
                            piece location list to accommodate these new changes. If you are just taking a piece, then
                            a piece is still on that tile so no addition needs to be made. Just the removal below.
                        */
                    if (this.getBackground() == Color.magenta) {
                        Chessboard.pieceLocations.add(this.tileCoordinate);
                    }
                    Chessboard.pieceLocations.remove((Integer) lastCord); // remove the location of the tile the piece was moved from

                        /*
                        Piece moves by taking the information from the last tile clicked on (which would be the piece
                        moving) and moving it to the new tile that was clicked on (where the piece is moving to).
                         */
                    this.piece = lastPiece;
                    this.pieceColor = lastTile.pieceColor;
                    this.highlighted = false;
                    this.setIcon(lastTile.getIcon());
                    this.moved = true;

                    // resets the lastTile variables
                    lastTile.setIcon(null);
                    lastTile.pieceColor = null;
                    lastTile.highlighted = false;
                    lastTile.piece = null;
                    lastTile.moved = true;
                }

                // reset lastPiece and lastCord variables
                lastPiece = null; // !!!!!!!!!!!! DELETE THIS AT SOME POINT AS IT IS PROBABLY UNNEEDED !!!!!!!!!!!!!!!!!!!!
                lastCord = 64; // impossible tile coordinate
            } else {
                System.out.println("THERE WAS AN ERROR! The tile that was just clicked on was " +
                        "labeled as being highlighted, but the background isn't one of the two " +
                        "colors it should be.");
            }
            resetTileColors();
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

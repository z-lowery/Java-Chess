## What is this project?

This project is intended to be a fully working Java implementation of the game Chess.

In the future I hope to improve code readability, specifically in the Tile class, and implement javafx to replace Jbuttons, Jframs, etc., and implement missing features.

## Missing features
1) Pawns can not En passant.
2) The game still allows you to move any piece if the king is in check OR checkmate.
3) There is no visible indication when the king is in check or checkmate.
4) There is nothing forcing either player to wait for the other to make a move.

## Known bugs
1) In certain instances, an adjacent tile to the king will be incorrectly highlighted (indicating the king can move there) when the enemy king is actually threatening that tile. 

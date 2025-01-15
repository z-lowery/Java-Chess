## What is this project?
This project is intended to be a fully working Java implementation of the game Chess.

## Missing features
1) Pawns can not En passant.
2) The game still allows you to move any piece if the king is in check OR checkmate.
3) There is no visible indication when the king is in check or checkmate.
4) There is nothing forcing either player to wait for the other to make a move.

## Known bugs
1) In certain instances, an adjacent tile to the king will be incorrectly highlighted (indicating the king can move there) when the enemy king is actually threatening that tile. 
2) Kings can castle through check
3) Attempting to move a king after it has castled will result in an error

## TO-DO
Personal to-do list
1) Change piece strings into piece objects 
2) Implement javafx to replace Jbuttons, Jframes, etc.,
package edu.kentisd.designlab.kipp;

import com.codegym.engine.cell.Color;
import com.codegym.engine.cell.Game;

import static java.lang.System.exit;

//
// TODO LIST:
// 1) Scout to move more than one place
// 2) Get reset button to work
// 3) Detect that a game is over, if a player has no moves left
// 4) Figure out network communications so two people can play on separate computers
//
public class StrategoGame extends Game {

    //stratego bord is 10 by 10
    private static final int SIDE = 10;

    //four different situation after an attack
    public static final int ATTACKERWINS = 1;
    public static final int DEFENDERWINS = 2;
    public static final int BOTHWINS = 3;
    public static final int FLAGGAMEWIN = 4;

    //different games states
    public static final int CREATED = 0;
    public static final int PLAYER_ONE_SETUP = 1;
    public static final int PLAYER_TWO_SETUP = 2;
    public static final int PLAYER_ONE_TURN = 3;
    public static final int PLAYER_TWO_TURN = 4;
    public static final int PLAYER_ONE_WAITING = 5;
    public static final int PLAYER_TWO_WAITING = 6;
    public static final int GAME_OVER = 7;

    // Button identifiers
    public static final int DONE_BUTTON_ID = 1;
    public static final int UNDO_BUTTON_ID = 2;
    public static final int RESTART_BUTTON_ID = 3;
    public static final int EXIT_BUTTON_ID = 4;

    private int currentGameState;
    private StrategoGameBoardSpace selectedPlace;
    private int currentPlayer = GamePiece.PLAYER1;

    private StrategoGameBoardSpace[][] gameBoard = new StrategoGameBoardSpace[SIDE][SIDE+1];

    private GamePiece[] playerOneGamePieces = new GamePiece[40];
    private GamePiece[] playerTwoGamePieces = new GamePiece[40];

    public void initialize(){
        // Add an extra row for game controls
        setScreenSize(SIDE+1, SIDE);
        createGame();
        currentGameState = CREATED;
        initializePlayerPieces(playerOneGamePieces, GamePiece.PLAYER1);
        initializePlayerPieces(playerTwoGamePieces, GamePiece.PLAYER2);
        placeAllPlayerPieces(playerOneGamePieces, GamePiece.PLAYER1);
        placeAllPlayerPieces(playerTwoGamePieces, GamePiece.PLAYER2);
        currentGameState = PLAYER_ONE_SETUP;
    }

    private void createGame(){
        for(int x = 0; x<SIDE+1; x++){
            for(int y = 0; y<SIDE; y++){
                gameBoard[y][x]= new StrategoGameBoardSpace(x, y);
                setCellColor(x, y, Color.DARKGREEN);
            }
        }
        //list of x,y coordinates for the location of water on the game board
        int[] waterSquares = {2,4, 3,5, 3,4, 2,5, 6,4, 7,4, 6,5, 7,5};
        setWater(waterSquares);

        // Set the control buttons
        for (int y=0; y<SIDE; y++) {
            gameBoard[y][SIDE].isActionCell = true;
            setCellColor(SIDE,y,Color.BLACK);
        }

        // Setup the undo button
        setCellValueEx(SIDE, SIDE-4, Color.BEIGE, "\u238C");
        gameBoard[SIDE-4][SIDE].actionID = UNDO_BUTTON_ID;

        //setup the done button
        setCellValueEx(SIDE, SIDE-3, Color.BEIGE, "Done");
        gameBoard[SIDE-3][SIDE].actionID = DONE_BUTTON_ID;

        //setup the restart button
        setCellValueEx(SIDE, SIDE-2, Color.BEIGE, "Restart");
        gameBoard[SIDE-2][SIDE].actionID = RESTART_BUTTON_ID;

        //setup the restart button
        setCellValueEx(SIDE, 1, Color.BEIGE, "Exit");
        gameBoard[1][SIDE].actionID = EXIT_BUTTON_ID;
    }

    private void setWater(int[] waterSquares){
        for(int i = 0; i<waterSquares.length; i+=2) {
            int x = waterSquares[i];
            int y = waterSquares[i+1];
            gameBoard[y][x].isWater = true;
            setCellColor(x, y, Color.AQUAMARINE);
        }
    }

    private void initializePlayerPieces(GamePiece[] playerPieces, int playerId) {
        for (int i=0; i<40; i++) {
            playerPieces[i] = new GamePiece();
            playerPieces[i].playerID = playerId;
        }
        playerPieces[0].createFlag();
        playerPieces[1].createMarshall();
        playerPieces[2].createGeneral();
        playerPieces[3].createColonel();
        playerPieces[4].createColonel();
        playerPieces[5].createMajor();
        playerPieces[6].createMajor();
        playerPieces[7].createMajor();
        playerPieces[8].createCaptain();
        playerPieces[9].createCaptain();
        playerPieces[10].createCaptain();
        playerPieces[11].createCaptain();
        playerPieces[12].createLieutenant();
        playerPieces[13].createLieutenant();
        playerPieces[14].createLieutenant();
        playerPieces[15].createLieutenant();
        playerPieces[16].createSergeant();
        playerPieces[17].createSergeant();
        playerPieces[18].createSergeant();
        playerPieces[19].createSergeant();
        playerPieces[20].createMiner();
        playerPieces[21].createMiner();
        playerPieces[22].createMiner();
        playerPieces[23].createMiner();
        playerPieces[24].createMiner();
        playerPieces[25].createScout();
        playerPieces[26].createScout();
        playerPieces[27].createScout();
        playerPieces[28].createScout();
        playerPieces[29].createScout();
        playerPieces[30].createScout();
        playerPieces[31].createScout();
        playerPieces[32].createScout();
        playerPieces[33].createSpy();
        playerPieces[34].createBomb();
        playerPieces[35].createBomb();
        playerPieces[36].createBomb();
        playerPieces[37].createBomb();
        playerPieces[38].createBomb();
        playerPieces[39].createBomb();
    }

    public static int attack(GamePiece attacker, GamePiece defender){
        //simple attack compare attack strength
        if (defender.isFlag){
            return FLAGGAMEWIN;
        } else if (defender.isBomb){
            if (attacker.isMiner){
                // attacker is miner
                return ATTACKERWINS;
            }else{
                return DEFENDERWINS;
            }
        } else if (attacker.isSpy && defender.strength == 10){
                return ATTACKERWINS;
        }
        else if (attacker.strength > defender.strength){
            return ATTACKERWINS;
        } else if (attacker.strength < defender.strength) {
            return DEFENDERWINS;
        } else {//(attacker.strength == defender.strength){
            return BOTHWINS;
        }
    }

    private void placeAllPlayerPieces(GamePiece[] pieces, int playerId) {
        int startingY = 0;
        if (playerId == GamePiece.PLAYER1) {
            startingY = 6;
        }

        int pieceCounter=0;
        for (int x=0; x<SIDE; x++) {
            for (int y=startingY; y<startingY+4; y++) {
                gameBoard[y][x].gamePiece = pieces[pieceCounter++];
                drawGameSpace(gameBoard[y][x]);
            }
        }
    }

    private void drawGameSpace(StrategoGameBoardSpace place) {
        if (currentGameState == PLAYER_ONE_WAITING || currentGameState == PLAYER_TWO_WAITING) {
            //Cover board while changing player
            setCellColor(place.x, place.y, Color.DARKGREY);
            setCellValue(place.x, place.y, "");
        } else {
            if (place.isWater) {
                // this is just a water space
                setCellColor(place.x, place.y, Color.AQUAMARINE);
                setCellValue(place.x, place.y, "");
            } else if (place.gamePiece == null) {
                // this is just a land space
                setCellColor(place.x, place.y, Color.DARKGREEN);
                setCellValue(place.x, place.y, "");
            } else {
                // this is a game piece space
                if (place.gamePiece.playerID == GamePiece.PLAYER1) {
                    setCellColor(place.x, place.y, Color.DARKMAGENTA);
                } else {
                    setCellColor(place.x, place.y, Color.ORANGERED);
                }

                // If it is not a player's turn, don't draw their pieces information
                if (currentPlayer != place.gamePiece.playerID && currentGameState != GAME_OVER) {
                    setCellValue(place.x, place.y, "");
                } else {
                    // If it is not a player's turn, set the values to blank;
                    if (place.gamePiece.isFlag) {
                        // draw a flag
                        setCellValue(place.x, place.y, "\uD83D\uDEA9");
                    } else if (place.gamePiece.isBomb) {
                        //draw a bomb
                        setCellValue(place.x, place.y, "\uD83D\uDCA3");
                    } else if (place.gamePiece.isSpy) {
                        //draw a spy
                        setCellValue(place.x, place.y, "S");
                    } else {
                        //draw all other game pieces
                        setCellNumber(place.x, place.y, place.gamePiece.strength);
                    }
                }
            }
        }
    }

    //private void createGame(){
    //        for(int x = 0; x<SIDE+1; x++){
    //            for(int y = 0; y<SIDE; y++){
    //                gameBoard[y][x]= new StrategoGameBoardSpace(x, y);
    //  KIPP WORK HERE NEXT look at the top of createGame for an example, don't create new spaces
    //
    private void redrawScreen() {
        // Loop through the gameboard draw that space
        for(int x = 0; x<SIDE; x++){
            for(int y = 0; y<SIDE; y++){
                drawGameSpace(gameBoard[y][x]);
            }
        }
    }

    public void swapPieces(StrategoGameBoardSpace placeOne, StrategoGameBoardSpace placeTwo){
        GamePiece tempPiece = placeOne.gamePiece;
        // Change piece one coordinate
        // Change piece two coordinate
        placeOne.gamePiece = placeTwo.gamePiece;
        placeTwo.gamePiece = tempPiece;
        // Update the screen image
        // Update the screen image
        drawGameSpace(placeOne);
        drawGameSpace(placeTwo);
    }

    //
    // During the players setup phase, this is what happens when the player clicks on the screen
    //
    private void handleSetupLeftClick(int x, int y) {
        if (gameBoard[y][x].gamePiece == null) {
            //showMessageDialog(Color.ANTIQUEWHITE, "Empty Space", Color.BLACK, 20);
            // Do nothing
        } else if (currentPlayer == gameBoard[y][x].gamePiece.playerID) {
            //showMessageDialog(Color.ANTIQUEWHITE, gameBoard[y][x].gamePiece.name, Color.BLACK, 20);
            setCellValueEx(SIDE,SIDE-5,Color.BLACK,gameBoard[y][x].gamePiece.name, Color.BEIGE);
            if (selectedPlace == null) {
                selectedPlace = gameBoard[y][x];
                setCellColor(x,y, Color.YELLOW);
            } else {
                swapPieces(selectedPlace, gameBoard[y][x]);
                selectedPlace = null;
            }
        }
    }

    //
    // Pieces can only move one space, and only onto empty spaces, or onto other player pieces
    //
    private boolean isValidMovement(int x, int y) {
        // only call if there is a selected piece
        if(selectedPlace != null) {
            // is space next to selected piece.
            if((selectedPlace.x-1 == x && selectedPlace.y == y)  ||
                    (selectedPlace.x+1 == x && selectedPlace.y == y) ||
                    (selectedPlace.y-1 == y && selectedPlace.x == x)  ||
                    (selectedPlace.y+1 == y && selectedPlace.x == x)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    //
    // Let's the game know if a piece is an enemy piece of the selected piece
    //
    private boolean isEnemyPiece(int x, int y){
        if (gameBoard[y][x].gamePiece != null) {
            return (gameBoard[y][x].gamePiece.playerID != selectedPlace.gamePiece.playerID);
        } else {
            return false;
        }
    }

    //
    // This is the logic on what happens when the player clicks on the board during game play
    //
    private void handleTurnLeftClick(int x, int y) {
        // player's first click
        if (selectedPlace == null && gameBoard[y][x].gamePiece != null && gameBoard[y][x].gamePiece.playerID == currentPlayer) {
            if (gameBoard[y][x].gamePiece.isFlag || gameBoard[y][x].gamePiece.isBomb) {
                // Do nothing don't select
            } else {
                selectedPlace = gameBoard[y][x];
                setCellColor(x, y, Color.YELLOW);
            }
        } else if (selectedPlace != null) {
            // is empty land
            if (gameBoard[y][x].gamePiece == null && isValidMovement(x,y)) {
                swapPieces(selectedPlace, gameBoard[y][x]);
                selectedPlace = null;
                changeGameState();
                redrawScreen();
            } else {
                if (isValidMovement(x,y) && isEnemyPiece(x,y)) {
                    // move onto enemy space
                    int results = attack(selectedPlace.gamePiece, gameBoard[y][x].gamePiece);
                    if (results == FLAGGAMEWIN) {
                        showMessageDialog(Color.ANTIQUEWHITE, "Player " + selectedPlace.gamePiece.playerID + " WINS" , Color.BLACK, 20);
                        currentGameState = GAME_OVER;
                        gameBoard[y][x].gamePiece = null;
                        swapPieces(selectedPlace, gameBoard[y][x]);
                        redrawScreen();
                    } else {
                        if (results == BOTHWINS) {
                            gameBoard[y][x].gamePiece = null;
                            selectedPlace.gamePiece = null;
                            drawGameSpace(gameBoard[y][x]);
                            drawGameSpace(selectedPlace);
                            selectedPlace = null;
                        } else if (results == DEFENDERWINS) {
                            selectedPlace.gamePiece = null;
                            drawGameSpace(selectedPlace);
                            selectedPlace = null;
                        } else {
                            // ATTACKERWINS
                            gameBoard[y][x].gamePiece = null;
                            swapPieces(selectedPlace, gameBoard[y][x]);
                            drawGameSpace(gameBoard[y][x]);
                            selectedPlace = null;
                        }
                        changeGameState();
                        redrawScreen();
                    }
                }
            }
            // trying to move on own piece - do nothing
            // water can never be clicked on - do nothing

        }

    }

    // When a person clicks on the done button, we need to change the game state
    private void changeGameState() {
        if (currentGameState == PLAYER_ONE_SETUP) {
            currentGameState = PLAYER_TWO_SETUP;
            currentPlayer = GamePiece.PLAYER2;
        } else if (currentGameState == PLAYER_TWO_SETUP) {
            currentGameState = PLAYER_ONE_WAITING;
            currentPlayer = GamePiece.PLAYER1;
        } else if (currentGameState == PLAYER_ONE_WAITING) {
            currentGameState = PLAYER_ONE_TURN;
        } else if (currentGameState == PLAYER_ONE_TURN) {
            currentGameState = PLAYER_TWO_WAITING;
            currentPlayer = GamePiece.PLAYER2;
        } else if (currentGameState == PLAYER_TWO_WAITING) {
            currentGameState = PLAYER_TWO_TURN;
        } else if (currentGameState == PLAYER_TWO_TURN) {
            currentGameState = PLAYER_ONE_WAITING;
            currentPlayer = GamePiece.PLAYER1;
        } else if (currentGameState == GAME_OVER) {
            currentGameState = CREATED;
            currentPlayer = GamePiece.PLAYER1;
        }
        this.setScore(currentGameState);
    }

    //
    // We replace the onMouseLeftClick of the codegym game class, so clicking will do what we want it to
    //
    @Override
    public void onMouseLeftClick(int x, int y){
        if(x<SIDE+1 && y<SIDE) {
            if (gameBoard[y][x].isWater) {
                // Do nothing
            } else if (gameBoard[y][x].isActionCell) {
                // Check if it is the done button clicked
                if (gameBoard[y][x].actionID == DONE_BUTTON_ID) {
                    if (currentGameState == PLAYER_ONE_WAITING ||
                            currentGameState == PLAYER_TWO_WAITING ||
                            currentGameState == PLAYER_ONE_SETUP ||
                            currentGameState == PLAYER_TWO_SETUP) {
                        // changing state, make sure we don't still have a selected place
                        selectedPlace = null;
                        changeGameState();
                        if (currentGameState == CREATED) {
                            initialize();
                        } else {
                            redrawScreen();
                        }
                    }
                } else if (gameBoard[y][x].actionID == UNDO_BUTTON_ID) {
                    if (selectedPlace != null) {
                        drawGameSpace(selectedPlace);
                        selectedPlace = null;
                    }
                } else if (gameBoard[y][x].actionID == RESTART_BUTTON_ID) {
                    initialize();
                } else if (gameBoard[y][x].actionID == EXIT_BUTTON_ID) {
                    exit(0);
                }
            } else if (currentGameState == PLAYER_ONE_SETUP || currentGameState == PLAYER_TWO_SETUP) {
                handleSetupLeftClick(x,y);
            } else if (currentGameState == PLAYER_ONE_TURN || currentGameState == PLAYER_TWO_TURN){
                handleTurnLeftClick(x,y);
            }
        }
    }
}

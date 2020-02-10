package edu.kentisd.designlab.kipp;

import com.codegym.engine.cell.Color;

//1,10m 1,9g 2,8c 3,7m 4,6c 4,5L 4,4s 5,3m 8,2s 1,s 1,f 6,b
public class GamePiece {
    public static final int PLAYER1 = 1;
    public static final int PLAYER2 = 2;
    public boolean isSpy;
    public boolean isFlag;
    public boolean isBomb;
    public boolean isMiner;
    public int maxMoves = 1;
    public int strength;
    public int playerID;
    public String name;

    private void resetDefaults() {
        isSpy = false;
        isFlag = false;
        isBomb = false;
        isMiner = false;
    }

    public void createMarshall() {
        resetDefaults();
        strength = 10;
        name = "Marshall";
    }

    public void createGeneral() {
        resetDefaults();
        strength = 9;
        name = "General";
    }

    public void createColonel() {
        resetDefaults();
        strength = 8;
        name = "Colonel";
    }

    public void createMajor() {
        resetDefaults();
        strength = 7;
        name = "Major";
    }

    public void createCaptain() {
        resetDefaults();
        strength = 6;
        name = "Captain";
    }

    public void createLieutenant() {
        resetDefaults();
        strength = 5;
        name = "Lieutenant";
    }

    public void createSergeant() {
        resetDefaults();
        strength = 4;
        name = "Sergeant";
    }

    public void createMiner() {
        resetDefaults();
        strength = 3;
        name = "Miner";
        isMiner = true;
    }

    public void createScout() {
        resetDefaults();
        strength = 2;
        name = "Scout";
    }

    public void createSpy() {
        resetDefaults();
        strength = 1;
        name = "Spy";
        isSpy = true;
    }

    public void createBomb() {
        resetDefaults();
        strength = 0;
        name = "Bomb";
        isBomb = true;
    }

    public void createFlag() {
        resetDefaults();
        strength = 0;
        isFlag = true;
        name = "Flag";
    }

    //public static GamePiece(int [] x){
      //  for(int i = 0; i<x.length; i+=2) {
        // somthing for traking the GamePiece
        //    xx[x][y].xxx = true;
        //    setCellColor(x, y, Color.LAWNGREEN);
    //}
}

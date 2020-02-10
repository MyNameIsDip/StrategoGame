package edu.kentisd.designlab.kipp;

public class StrategoGameBoardSpace {
    public int x;
    public int y;
    public boolean isWater;
    public boolean isActionCell;
    public int actionID;
    public GamePiece gamePiece = null;

    public StrategoGameBoardSpace(int x, int y){
        this.x = x;
        this.y = y;
    }
}

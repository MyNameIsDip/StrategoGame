package edu.kentisd.designlab.kipp;

public class TestAttack {
    public static void main(String[] arg){
        System.out.println("start of test");
        GamePiece attacker = new GamePiece();
        GamePiece defender = new GamePiece();
        defender.createGeneral();

        // attacker is stronger
        attacker.createMarshall();
        int results = StrategoGame.attack(attacker, defender);
        System.out.println("PASSED : " + (results == StrategoGame.ATTACKERWINS));

        // defender is stronger
        attacker.createMajor();
        results = StrategoGame.attack(attacker, defender);
        System.out.println("PASSED : " + (results == StrategoGame.DEFENDERWINS));

        // attacker and defender the same
        attacker.createGeneral();
        results = StrategoGame.attack(attacker, defender);
        System.out.println("PASSED : " + (results == StrategoGame.BOTHWINS));

        // flag attacked
        defender.createFlag();
        results = StrategoGame.attack(attacker, defender);
        System.out.println("PASSED : " + (results == StrategoGame.FLAGGAMEWIN));

        // bomb is attacked
        defender.createBomb();
        results = StrategoGame.attack(attacker, defender);
        System.out.println("PASSED : " + (results == StrategoGame.DEFENDERWINS));

        // bomb attacked by miner
        attacker.createMiner();
        results = StrategoGame.attack(attacker, defender);
        System.out.println("PASSED : " + (results == StrategoGame.ATTACKERWINS));

        // spy attacker 9g
        attacker.createSpy();
        defender.createGeneral();
        results = StrategoGame.attack(attacker, defender);
        System.out.println("PASSED : " + (results == StrategoGame.DEFENDERWINS));

        // spy attacker 10m
        defender.createMarshall();
        results = StrategoGame.attack(attacker, defender);
        System.out.println("PASSED : " + (results == StrategoGame.ATTACKERWINS));

        // 10m attacker spy
        attacker.createMarshall();
        defender.createSpy();
        results = StrategoGame.attack(attacker, defender);
        System.out.println("PASSED : " + (results == StrategoGame.ATTACKERWINS));
    }
}

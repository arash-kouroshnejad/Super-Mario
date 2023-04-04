package Control;

import Game.GameStat;

import java.util.ArrayList;

public class User {
    private final String USERNAME;
    private final byte[] PASSHASH;
    private int coins;

    private GameStat[] games = new GameStat[3];

    private ArrayList<GameStat> allGames = new ArrayList<>();

    public User(String USERNAME, byte[] PASSHASH) {
        this.USERNAME = USERNAME;
        this.PASSHASH = PASSHASH;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public byte[] getPASSHASH() {
        return PASSHASH;
    }

    public GameStat[] getGames() {
        return games;
    }

    public void setGame(int n, GameStat game) {
        games[n] = game;
    }

    public void setGames(GameStat[] games) {
        this.games = games;
    }

    public ArrayList<GameStat> getAllGames() {
        return allGames;
    }

    public void addGame(GameStat game) {
        allGames.add(game);
    }

    public void setAllGames(ArrayList<GameStat> allGames) {
        this.allGames = allGames;
    }
}

package control;

import game.model.GameStat;

import java.util.ArrayList;
import java.util.HashSet;

public class User {
    private final String USERNAME;
    private final byte[] PASSHASH;
    private int coins;

    private GameStat[] games;

    private int highestScore;

    private ArrayList<GameStat> allGames = new ArrayList<>();

    private HashSet<String> characters = new HashSet<>();

    public User(String USERNAME, byte[] PASSHASH) {
        this.USERNAME = USERNAME;
        this.PASSHASH = PASSHASH;
        characters.add("Mario");
        games = new GameStat[]{new GameStat(0, 3, 0, 0), new GameStat(0, 3, 0, 1),
            new GameStat(0, 3, 0, 2)};
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

    public void purchase(String character) {
        if (!characters.contains(character)) {
            coins -= 20;
            characters.add(character);
        }
    }

    public HashSet<String> getCharacters() {
        return characters;
    }

    public void setCharacters(HashSet<String> characters) {
        this.characters = characters;
    }

    public int getHighestScore() {
        return highestScore;
    }

    public void setHighestScore(int highestScore) {
        this.highestScore = highestScore;
    }
}

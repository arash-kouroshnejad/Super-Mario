package Persistence;

import Control.AccountManager;
import Control.User;
import Game.GameStat;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class Persistence{
    private final static Persistence instance = new Persistence();
    private final GsonBuilder gsonBuilder = new GsonBuilder().serializeNulls();
    private final  Gson gson = gsonBuilder.create();
    private final AccountManager accountManager = AccountManager.getInstance();
    private Persistence() {}
    public void saveUsers() {
        try {
            FileWriter writer = new FileWriter("./src/Resources/Users.json");
            gson.toJson(accountManager.getUsers(), writer);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public User findUser(String username) {
        for (User user : accountManager.getUsers()) {
            if (user.getUSERNAME().equals(username)) {
                return user;
            }
        }
        return null;
    }
    public void readUsers() {
        FileReader reader = null;
        try {
            reader = new FileReader("./src/Resources/Users.json");
            User[] allUsers = gson.fromJson(reader, User[].class);
            for (User user : allUsers) {
                if (user.getAllGames() == null) {
                    user.setAllGames(new ArrayList<>());
                }
            }
            accountManager.getUsers().addAll(Arrays.stream(allUsers).toList());
        } catch (FileNotFoundException e) {
            System.out.println("Error reading Users.json");
        }
    }

    public static Persistence getInstance() {
        return instance;
    }
}

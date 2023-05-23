package Game.Util.Loaders;

import Control.AccountManager;
import Game.Model.GameStat;
import Persistence.Config;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class GameLoader extends SpriteLoader {

    private static GameStat currentGame;

    public GameLoader() {
        super(Config.getInstance().getProperty("SavesDir") + AccountManager.getInstance().getCurrentUser().getUSERNAME() + "/");
    }

    public GameStat getGame(int ID) {
        Gson gson = new Gson();
        try {
            return gson.fromJson(new FileReader(PathToMaps + ID + "/stats.game"), GameStat.class);
        } catch (Exception e) {
            System.out.println("Failed To Get Game : " + ID);
        }
        return null;
    }

    public GameStat createGame(int ID) {
        GameStat game = new GameStat(0, 3, 0, ID);
        AccountManager.getInstance().getCurrentUser().addGame(game);
        saveGame(game);
        String defaultMaps = Config.getInstance().getProperty("DefaultMapsDir");
        try {
            Files.copy(java.nio.file.Path.of(defaultMaps + "0.map"), java.nio.file.Path.of(PathToMaps + ID + "/0.map"),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(java.nio.file.Path.of(defaultMaps + "1.map"), java.nio.file.Path.of(PathToMaps + ID + "/1.map"),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception ignored) {}
        return game;
    }

    public void saveGame(GameStat game) {
        try {
            File gameFile = new File(PathToMaps + game.getID() + "/stats.game");
            if (!gameFile.getParentFile().exists()) {
                gameFile.getParentFile().mkdirs();
            }
            FileWriter writer = new FileWriter(gameFile);
            Gson gson = new Gson();
            gson.toJson(game, writer);
            writer.close();
        } catch (Exception e) {
            System.out.println("Failed To Save Game " + game.getID());
        }
    }

    public void loadGame(int ID) {
        loadMap(ID, getGame(ID).getLevel());
    }
}

package Game;

import Control.AccountManager;
import Core.Util.Loader;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class GameLoader extends Loader {

    private static GameStat currentGame;

    public GameLoader() {
        super("./src/Resources/" + AccountManager.getInstance().getCurrentUser().getUSERNAME() + "/");
    }

    public GameStat getGame(int ID) {
        Gson gson = new Gson();
        try {
            return gson.fromJson(new FileReader(Path.substring(0, Path.length() - 5) + "Games/"), GameStat.class);
        } catch (Exception ignored) {}
        return null;
    }

    public GameStat createGame(int ID) {
        GameStat game = new GameStat(0, 3, 0, ID);
        AccountManager.getInstance().getCurrentUser().addGame(game);
        saveGame(game);
        try {
            Files.copy(java.nio.file.Path.of("./src/Resources/Maps/0.map"), java.nio.file.Path.of(Path + ID + "/0.map"),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(java.nio.file.Path.of("./src/Resources/Maps/1.map"), java.nio.file.Path.of(Path + ID + "/1.map"),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception ignored) {}
        return game;
    }

    public void saveGame(GameStat game) {
        try {
            FileWriter writer = new FileWriter(new File(Path + game.getID() + "/stats.game"));
            Gson gson = new Gson();
            gson.toJson(game, writer);
            writer.close();
        } catch (Exception ignored) {}
    }

    public void loadGame(int ID) {
        GameStat game = getGame(ID);
        int level = game.getLevel();
        loadMap(ID, level);
    }

    @Override
    public boolean isDynamic(String type) {
        return false;
    }

    @Override
    public boolean isLocked(String type) {
        return false;
    }
}

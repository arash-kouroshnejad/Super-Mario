import Control.GameManager;
import Core.Editor.LevelEditor;
import Core.Editor.MapLoader;
import Game.Creator;
import Game.MarioLogic;
import Persistence.*;

public class Driver {
    public static void main(String[] args) {
        Config.getInstance().setPath(args[0]);
        Persistence.getInstance().readUsers();
        /*MapLoader loader = new MapLoader("./src/Resources/Maps/");
        MarioLogic logic = new MarioLogic();
        LevelEditor.getInstance().init(loader, logic, new Creator());*/
        GameManager.getInstance().showWelcome();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Persistence.getInstance().saveUsers();
        }, "Shutdown-Hook"));
    }
}

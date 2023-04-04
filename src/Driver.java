import Control.GameManager;
import Core.Editor.LevelEditor;
import Game.Creator;
import Game.MarioLogic;
import Game.SpriteLoader;
import Persistence.Persistence;

public class Driver {
    public static void main(String[] args) {
        Persistence.getInstance().readUsers();
        /*SpriteLoader loader = new SpriteLoader("./src/Resources/Maps/") {
            @Override
            public boolean isLocked(String type) {
                return super.isLocked(type);
            }

            @Override
            public boolean isDynamic(String type) {
                return super.isDynamic(type);
            }
        };
        MarioLogic logic = new MarioLogic();
        LevelEditor.getInstance().init(loader, logic, new Creator());*/
        GameManager.getInstance().showWelcome();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Persistence.getInstance().saveUsers();
        }, "Shutdown-Hook"));
    }
}

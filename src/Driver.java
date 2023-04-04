import Control.GameManager;
import Persistence.Persistence;

public class Driver {
    public static void main(String[] args) {
        Persistence.getInstance().readUsers();
        /*MarioLoader loader = new MarioLoader();
        MarioLogic logic = new MarioLogic();
        LevelEditor.getInstance().init(loader, logic);*/
        // TimeThread.getInstance().start();
        GameManager.getInstance().showWelcome();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Persistence.getInstance().saveUsers();
        }, "Shutdown-Hook"));
        GameManager manager = GameManager.getInstance();
        // manager.showWelcome();
    }
}

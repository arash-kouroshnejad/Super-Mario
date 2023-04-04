package Control;

import Core.Render.GameEngine;
import Game.GameLoader;
import Game.MarioLogic;
import Game.GameStat;
import Game.SpriteLoader;
import UI.Login.LoginController;
import UI.MainMenu.MainMenuController;
import UI.NewGame.NewGameController;
import UI.Register.RegisterController;
import UI.Welcome.WelcomeController;

public class GameManager {

    private final static GameManager instance = new GameManager();
    private WelcomeController welcomeController;
    private LoginController loginController;
    private RegisterController registerController;
    private MainMenuController mainMenuController;
    private NewGameController newGameController;
    private final AccountManager accountManager = AccountManager.getInstance();
    private final MarioLogic gameLogic = new MarioLogic();
    private GameLoader statLoader;
    private final GameEngine engine = GameEngine.getInstance();
    private final SpriteLoader assetLoader = new SpriteLoader();
    private GameStat currentGame;
    private GameManager() {}
    public void showWelcome() {
        welcomeController = new WelcomeController();
        welcomeController.show();
    }
    public void showLogin() {
        loginController = new LoginController();
        loginController.show();
    }
    public void showRegister() {
        registerController = new RegisterController();
        registerController.show();
    }
    public void showMenu() {
        mainMenuController = new MainMenuController();
        mainMenuController.show();
    }
    public int getCoins() {
        return accountManager.getCurrentUser().getCoins();
    }
    public void showNewGame() {
        newGameController = new NewGameController(accountManager.getCurrentUser());
        newGameController.show();
    }

    public void newGame(int ID) {
        statLoader = new GameLoader();
        currentGame = statLoader.createGame(ID);
        statLoader.loadGame(ID);
        engine.init(gameLogic);
        engine.startGame();
        gameLogic.init(assetLoader);
    }

    public void resetGame() {
        assetLoader.loadMap(currentGame.getID(), currentGame.getLevel());
        gameLogic.init(assetLoader);
    }

    public void saveProgress() {
        assetLoader.saveMap(GameEngine.getInstance().getMap());
    }

    public boolean auth(String username, String password) {
        return accountManager.Login(username, password);
    }
    public boolean register(String username, String password) {
        return accountManager.createUser(username, password);
    }

    public static GameManager getInstance() {
        return instance;
    }

}

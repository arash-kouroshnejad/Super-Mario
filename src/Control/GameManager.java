package Control;

import Core.Render.GameEngine;
import Game.GameLoader;
import Game.MarioLogic;
import Game.GameStat;
import UI.Login.LoginController;
import UI.MainMenu.MainMenuController;
import UI.NewGame.NewGameController;
import UI.Register.RegisterController;
import UI.ResumeGame.ResumePageController;
import UI.Welcome.WelcomeController;

public class GameManager {

    private final static GameManager instance = new GameManager();
    private final AccountManager accountManager = AccountManager.getInstance();
    private MarioLogic gameLogic;
    private final GameEngine engine = GameEngine.getInstance();
    private GameLoader assetLoader;
    private GameStat currentGame;
    private GameManager() {}
    public void showWelcome() {
        WelcomeController welcomeController = new WelcomeController();
        welcomeController.show();
    }
    public void showLogin() {
        LoginController loginController = new LoginController();
        loginController.show();
    }
    public void showRegister() {
        RegisterController registerController = new RegisterController();
        registerController.show();
    }
    public void showMenu() {
        MainMenuController mainMenuController = new MainMenuController();
        mainMenuController.show();
    }
    public int getCoins() {
        return accountManager.getCurrentUser().getCoins();
    }
    public void showNewGame() {
        NewGameController newGameController = new NewGameController(accountManager.getCurrentUser());
        newGameController.show();
    }
    public void showResumeGame(){
        ResumePageController resumePageController = new ResumePageController(accountManager.getCurrentUser());
        resumePageController.show();
    }
    public void newGame(int ID) {
        gameLogic = new MarioLogic();
        assetLoader = new GameLoader();
        currentGame = assetLoader.createGame(ID);
        setUpFrame(gameLogic, assetLoader, ID);
    }
    public void resetGame() {
        assetLoader.loadMap(currentGame.getID(), currentGame.getLevel());
        gameLogic.init(assetLoader);
    }
    public void saveProgress() {
        assetLoader.saveMap(GameEngine.getInstance().getMap(), currentGame.getID());
        assetLoader.saveGame(currentGame);
        User usr = accountManager.getCurrentUser();
        usr.setGame(currentGame.getID(), currentGame); // TODO : remove game if its finished
        usr.setCoins(usr.getCoins() + currentGame.getCoinsEarned());
    }
    public void resumeGame(int ID) {
        gameLogic = new MarioLogic();
        assetLoader = new GameLoader();
        currentGame = assetLoader.getGame(ID);
        if (currentGame != null) {
            setUpFrame(gameLogic, assetLoader, ID);
        }
    }
    private void setUpFrame(MarioLogic logic, GameLoader loader, int ID) {
        assetLoader.loadGame(ID);
        engine.init(logic);
        engine.startGame();
        gameLogic.init(loader);
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
    public GameStat getCurrentGame() {
        return currentGame;
    }
}

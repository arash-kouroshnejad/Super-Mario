package Control;

import Core.Editor.LevelEditor;
import Core.Render.GameEngine;
import Game.Plugins.ElementManagers.ItemManager;
import Game.Util.Events.EventQueue;
import Game.Util.Handlers.*;
import Game.Util.Loaders.GameLoader;
import Game.MarioLogic;
import Game.Model.GameStat;
import UI.GameSetup.SetupController;
import UI.Leaderboard.LeaderBoardController;
import UI.Login.LoginController;
import UI.MainMenu.MainMenuController;
import UI.NewGame.NewGameController;
import UI.Profile.ProfileController;
import UI.Register.RegisterController;
import UI.ResumeGame.ResumePageController;
import UI.Store.StoreController;
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
        showSetup();
    }
    public void resetGame() {
        assetLoader.loadMap(currentGame.getID(), currentGame.getLevel());
        gameLogic.init(assetLoader);
    }
    public void showSetup() {
        SetupController setupController = new SetupController(accountManager.getCurrentUser());
        setupController.show();
    }
    public void getSetup(String character) {
        currentGame.setCharacter(character);
        setUpFrame(gameLogic, assetLoader, currentGame.getID());
    }
    public void saveProgress() {
        assetLoader.saveMap(GameEngine.getInstance().getMap(), currentGame.getID());
        assetLoader.saveGame(currentGame);
        User usr = accountManager.getCurrentUser();
        usr.setGame(currentGame.getID(), currentGame); // TODO : remove game if its finished
        usr.setCoins(usr.getCoins() + currentGame.getCoinsEarned());
        usr.setHighestScore(Math.max(usr.getHighestScore(), currentGame.getScore()));
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
        gameLogic.getModalTypes();
        gameLogic.init(loader);
        startHandlers();
        LevelEditor.getInstance().setLoader(loader);
        engine.init(logic);
        engine.startGame();
    }
    private void startHandlers() { // TODO : move this method to where it belongs !
        var keyHandler = new KeyToggled();
        var clickHandler = new MouseClicked();
        var generator = new ElementGenerator();
        var modalOptionHandler = new ModalOptionSelected();
        var modalTriggerHandler = new ModalTriggered();
        var eventQueue = EventQueue.getInstance();
        eventQueue.addHandler(keyHandler);
        eventQueue.addHandler(clickHandler);
        eventQueue.addHandler(generator);
        eventQueue.addHandler(modalOptionHandler);
        eventQueue.addHandler(modalTriggerHandler);
        eventQueue.startHandlers();
    }
    public void showStore() {
        StoreController storeController = new StoreController(accountManager.getCurrentUser());
        storeController.show();
    }
    public void showProfile() {
        ProfileController profileController = new ProfileController(accountManager.getCurrentUser());
        profileController.show();
    }
    public void showResults() {
        LeaderBoardController leaderBoardController = new LeaderBoardController(accountManager.getUsers());
        leaderBoardController.show();
    }
    public void removeGame(int ID) {
        assetLoader = new GameLoader();
        GameStat game = assetLoader.getGame(ID);
        if (game != null) {
            game.terminate();
        }
    }
    public void buyCharacter(String character) {
        accountManager.getCurrentUser().purchase(character);
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
    public MarioLogic getGameLogic() {
        return gameLogic;
    }
}

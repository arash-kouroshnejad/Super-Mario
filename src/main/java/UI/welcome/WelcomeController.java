package UI.welcome;

import control.GameManager;
import UI.FrameController;

public class WelcomeController extends FrameController {
    public WelcomeController() {
        super(new WelcomePage());
    }

    @Override
    public void select(String selection) {
        GameManager manager = GameManager.getInstance();
        hide();
        switch (selection) {
            case "LoginButton" -> manager.showLogin();
            case "RegisterButton" -> manager.showRegister();
            case "ExitButton" -> System.exit(0);
        }
    }
}

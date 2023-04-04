package UI.MainMenu;

import Control.GameManager;
import UI.FrameController;

import javax.swing.*;

public class MainMenuController extends FrameController {
    public MainMenuController() {
        super(new MainMenuPage());
        JPanel panel1 = (JPanel) frame.getContentPane().getComponent(0);
        ((JLabel)panel1.getComponent(5)).setText("Coins: " + GameManager.getInstance().getCoins());
    }

    @Override
    public void select(String selection) {
        frame.setVisible(false);
        switch (selection) {
            case "Resume":
            case "NewGame":
                GameManager.getInstance().showNewGame();
                break;
            case "Results":
            case "Profile":
            case "Store":
        }
    }
}

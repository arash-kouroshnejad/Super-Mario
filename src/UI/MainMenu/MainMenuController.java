package UI.MainMenu;

import Control.GameManager;
import UI.FrameController;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MainMenuController extends FrameController {
    public MainMenuController() {
        super(new MainMenuPage());
        JPanel panel1 = (JPanel) frame.getContentPane().getComponent(0);
        ((JLabel)panel1.getComponent(5)).setText("Coins: " + GameManager.getInstance().getCoins());
        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 27) {
                    frame.setVisible(false);
                    GameManager.getInstance().showWelcome();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }

    @Override
    public void select(String selection) {
        frame.setVisible(false);
        switch (selection) {
            case "Resume" -> GameManager.getInstance().showResumeGame();
            case "NewGame" -> GameManager.getInstance().showNewGame();
            case "Results", "Profile" -> GameManager.getInstance().showProfile();
            case "Store" -> GameManager.getInstance().showStore();
        }
    }
}

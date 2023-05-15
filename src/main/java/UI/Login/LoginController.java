package UI.Login;

import Control.GameManager;
import UI.FrameController;

import javax.swing.*;
import javax.swing.border.Border;


public class LoginController extends FrameController {
    public LoginController() {
        super(new LoginPage());
    }
    @Override
    public void select(String selection) {
        switch (selection) {
            case "LoginButton" -> {
                JPanel mainPanel = (JPanel) frame.getContentPane().getComponent(0);
                mainPanel = (JPanel) mainPanel.getComponent(0);
                JTextField usernameField = (JTextField) mainPanel.getComponent(0);
                JPasswordField passwordField = (JPasswordField) mainPanel.getComponent(1);
                boolean successful = GameManager.getInstance().auth(usernameField.getText(), String.valueOf(passwordField.getPassword()));
                if (successful) {
                    hide();
                    GameManager.getInstance().showMenu();
                }
                else {
                    ((LoginPage)frame).getErrorMsg().setVisible(true);
                }
            }
            case "CancelButton" -> {
                frame.setVisible(false);
                GameManager.getInstance().showWelcome();
            }
        }
    }
}

package UI.register;

import control.GameManager;
import UI.FrameController;

import javax.swing.*;

public class RegisterController extends FrameController {
    public RegisterController() {
        super(new RegisterPage());
    }

    @Override
    public void select(String selection) {
        switch (selection) {
            case "RegisterButton" -> {
                JPanel mainPanel = (JPanel) frame.getContentPane().getComponent(0);
                JTextField usernameField = (JTextField) mainPanel.getComponent(0);
                JPasswordField passwordField = (JPasswordField) mainPanel.getComponent(1);
                boolean successful = GameManager.getInstance().register(usernameField.getText(), String.valueOf(passwordField.getPassword()));
                if (successful) {
                    hide();
                    GameManager.getInstance().showWelcome();
                }
                else {
                    ((RegisterPage)frame).getErrorMsg().setVisible(true);
                }
            }
            case "CancelButton" -> {
                hide();
                GameManager.getInstance().showWelcome();
            }
        }
    }
}

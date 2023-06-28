package UI.login;

import UI.Navigable;

import javax.swing.*;
import java.awt.*;

public class LoginPage extends Navigable {
    private JPanel panel2;
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JButton LoginButton;
    private JButton cancelButton;
    private JPanel panel1;
    private JLabel ErrorMsg;

    public LoginPage() {
        mainPanel.add(panel2, BorderLayout.SOUTH);
        panel2.setPreferredSize(new Dimension(400, 300));
        panel2.setBackground(Color.BLACK);
        panel2.setBounds(getWidth() / 2, 500, 400, 300);
        LoginButton.addMouseListener(this);
        cancelButton.addMouseListener(this);
        LoginButton.setName("LoginButton");
        cancelButton.setName("CancelButton");
    }
    public JLabel getErrorMsg() {
        return ErrorMsg;
    }
}

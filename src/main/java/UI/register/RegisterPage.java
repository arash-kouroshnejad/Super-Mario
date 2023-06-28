package UI.register;

import UI.Navigable;

import javax.swing.*;
import java.awt.*;

public class RegisterPage extends Navigable {
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JButton registerButton;
    private JButton cancelButton;
    private JPanel panel1;
    private JLabel ErrorMsg;
    private JPanel headPanel;

    public RegisterPage() {
        headPanel.setBackground(Color.BLACK);
        mainPanel.add(panel1, BorderLayout.SOUTH);
        panel1.setPreferredSize(new Dimension(400, 300));
        panel1.setBackground(Color.BLACK);
        panel1.setBounds(getWidth() / 2, 500, 400, 300);
        registerButton.addMouseListener(this);
        cancelButton.addMouseListener(this);
        registerButton.setName("RegisterButton");
        cancelButton.setName("CancelButton");
    }
    public JLabel getErrorMsg() {
        return ErrorMsg;
    }
}

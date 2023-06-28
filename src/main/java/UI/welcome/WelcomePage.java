package UI.welcome;

import UI.Navigable;

import javax.swing.*;
import java.awt.*;

public class WelcomePage extends Navigable {
    private JLabel LoginButton;
    private JLabel RegisterButton;
    private JLabel ExitButton;
    private JPanel panel;

    public WelcomePage() {
        super();
        mainPanel.add(panel, BorderLayout.SOUTH);
        panel.setPreferredSize(new Dimension(200, 400));
        panel.setBackground(Color.BLACK);
        panel.setBounds(getWidth() / 2, 500, 200, 200);
        LoginButton.addMouseListener(this);
        RegisterButton.addMouseListener(this);
        ExitButton.addMouseListener(this);
        LoginButton.setName("LoginButton");
        RegisterButton.setName("RegisterButton");
        ExitButton.setName("ExitButton");
    }
}

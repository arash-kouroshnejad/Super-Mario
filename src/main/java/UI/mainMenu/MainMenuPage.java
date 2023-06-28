package UI.mainMenu;

import UI.Navigable;

import javax.swing.*;
import java.awt.*;

public class MainMenuPage extends Navigable {

    private JPanel panel1;
    private JLabel CoinsField;
    private JLabel ResumeButton;
    private JLabel NewGameButton;
    private JLabel ProfileButton;
    private JLabel StoreButton;
    private JLabel ResultsButton;

    public MainMenuPage() {
        mainPanel.add(panel1, BorderLayout.SOUTH);
        panel1.setPreferredSize(new Dimension(600, 400));
        panel1.setBounds(getWidth() / 2, 500, 600, 400);
        for (Component comp : panel1.getComponents()) {
            comp.addMouseListener(this);
        }
        CoinsField.setName("Coins");
        ResumeButton.setName("Resume");
        NewGameButton.setName("NewGame");
        ProfileButton.setName("Profile");
        StoreButton.setName("Store");
        ResultsButton.setName("Results");
    }
}

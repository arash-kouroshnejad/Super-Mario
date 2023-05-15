package UI.NewGame;

import UI.Navigable;

import javax.swing.*;
import java.awt.*;

public class NewGamePage extends Navigable {

    private JPanel panel1;
    private JPanel first;
    private JPanel second;
    private JPanel third;
    private JButton exit;

    public NewGamePage() {
        panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));
        panel1.setPreferredSize(new Dimension(600, 400));
        mainPanel.add(panel1, BorderLayout.SOUTH);
        panel1.setBounds(getWidth() / 2, 800, 600, 400);
        panel1.setBackground(Color.BLACK);
        exit.setName("ExitButton");
        exit.addMouseListener(this);
    }
}

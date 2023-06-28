package UI.resumeGame;

import UI.Navigable;

import javax.swing.*;
import java.awt.*;

public class ResumePage extends Navigable {

    private JPanel panel1;
    private JLabel Game3;
    private JLabel Game1;
    private JLabel Game2;
    private JLabel ExitButton;

    public ResumePage() {
        mainPanel.add(panel1, BorderLayout.SOUTH);
        panel1.setPreferredSize(new Dimension(200, 400));
        panel1.setBackground(Color.BLACK);
        panel1.setBounds(getWidth() / 2, 500, 200, 200);
        Game2.setName("game1");
        Game1.setName("game0");
        Game3.setName("game2");
        ExitButton.setName("ExitButton");
        ExitButton.addMouseListener(this);
    }
}

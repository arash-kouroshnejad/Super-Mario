package UI.Store;

import UI.Navigable;

import javax.swing.*;
import java.awt.*;

public class StorePage extends Navigable {

    private JPanel panel1;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JButton button4;
    private JPanel panel;
    private JLabel exitButton;

    public StorePage() {
        mainPanel.add(panel1, BorderLayout.SOUTH);
        panel1.setPreferredSize(new Dimension(300, 400));
        panel1.setBounds(getWidth() / 2, 500, 300 ,400);
        button1.setName("DonkeyKong");
        button2.setName("Monster");
        button3.setName("Steve");
        button4.setName("PacMan");
        exitButton.setName("ExitButton");
        exitButton.addMouseListener(this);
        panel1.setBackground(Color.BLACK);
        panel.setBackground(Color.BLACK);
    }
}

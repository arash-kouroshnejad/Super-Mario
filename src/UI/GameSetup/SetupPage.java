package UI.GameSetup;

import UI.Navigable;

import javax.swing.*;
import java.awt.*;

public class SetupPage extends Navigable {
    private JPanel panel1;
    private JButton startGameButton;
    private JComboBox comboBox1;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JButton button4;
    private JButton button5;
    private JButton exitButton;
    private JPanel panel2;
    private JPanel panel3;
    private JPanel panel4;
    private JPanel panel5;


    public SetupPage() {
        mainPanel.add(panel1, BorderLayout.SOUTH);
        panel1.setPreferredSize(new Dimension(300, 400));
        panel1.setBounds(getWidth() / 2, 500, 300, 400);
        button1.setName("Mario");
        button2.setName("Monster");
        button3.setName("Steve");
        button4.setName("DonkeyKong");
        button5.setName("PacMan");
        startGameButton.setName("Start");
        startGameButton.addMouseListener(this);
        exitButton.setName("ExitButton");
        exitButton.addMouseListener(this);
        panel1.setBackground(Color.BLACK);
        panel2.setBackground(Color.BLACK);
        panel3.setBackground(Color.BLACK);
        panel4.setBackground(Color.BLACK);
        panel5.setBackground(Color.BLACK);
    }
}

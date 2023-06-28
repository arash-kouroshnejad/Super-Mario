package UI.profile;

import UI.Navigable;

import javax.swing.*;
import java.awt.*;

public class ProfilePage extends Navigable {
    private JPanel panel1;
    private JLabel UserName;
    private JLabel Character1;
    private JLabel Character2;
    private JLabel Character3;
    private JLabel Character4;
    private JLabel Character5;
    private JLabel HighestScore;
    private JPanel panel2;
    private JPanel panel3;
    private JPanel panel4;

    public ProfilePage() {
        mainPanel.add(panel1, BorderLayout.SOUTH);
        panel1.setPreferredSize(new Dimension(400, 400));
        panel1.setBounds(getWidth() / 2, 700, 400, 400);
        UserName.setName("username");
        Character1.setName("Mario");
        Character2.setName("Monster");
        Character3.setName("PacMan");
        Character4.setName("Steve");
        Character5.setName("Ghost");
        HighestScore.setName("highestScore");
        panel1.setBackground(Color.BLACK);
        panel2.setBackground(Color.BLACK);
        panel3.setBackground(Color.BLACK);
        panel4.setBackground(Color.BLACK);
    }
}

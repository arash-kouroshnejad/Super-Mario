package UI.leaderboard;

import UI.Navigable;

import javax.swing.*;
import java.awt.*;

public class LeaderBoardPage extends Navigable {
    private JPanel panel1;
    private JLabel third;
    private JLabel second;
    private JLabel first;

    public LeaderBoardPage() {
        mainPanel.add(panel1, BorderLayout.SOUTH);
        panel1.setPreferredSize(new Dimension(400, 300));
        panel1.setBackground(Color.BLACK);
        panel1.setBounds(getWidth() / 2, 500, 400, 300);
    }
}

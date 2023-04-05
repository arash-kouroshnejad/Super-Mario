package UI.Leaderboard;

import Control.GameManager;
import Control.User;
import Control.UserSorter;
import UI.FrameController;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class LeaderBoardController extends FrameController {
    public LeaderBoardController(ArrayList<User> users) {
        super(new LeaderBoardPage());
        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 27) {
                    hide();
                    GameManager.getInstance().showMenu();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        users.sort(new UserSorter());
        JPanel panel1 = (JPanel) frame.getContentPane().getComponent(0);
        JLabel label = (JLabel) panel1.getComponent(0);
        int size = users.size();
        User usr;
        if (size > 0) {
            usr = users.get(0);
            label.setText(usr.getUSERNAME() + " -- " + usr.getHighestScore());
        }
        if (size > 1) {
            usr = users.get(1);
            label = (JLabel) panel1.getComponent(3);
            label.setText(usr.getUSERNAME() + " -- " + usr.getHighestScore());
        }
        if (size > 2) {
            usr = users.get(2);
            label = (JLabel) panel1.getComponent(5);
            label.setText(usr.getUSERNAME() + " -- " + usr.getHighestScore());
        }
    }

    @Override
    public void select(String selection) {

    }
}

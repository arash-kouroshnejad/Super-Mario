package UI.NewGame;

import Control.GameManager;
import Control.User;
import Game.GameStat;
import UI.FrameController;

import javax.swing.*;
import java.awt.*;

public class NewGameController extends FrameController {
    public NewGameController(User user) {
        super(new NewGamePage());
        JPanel mainPanel = (JPanel) frame.getContentPane();
        GameStat[] usrGames =  user.getGames();
        if (usrGames == null) {
            usrGames = new GameStat[]{new GameStat(0, 3, 0, 0), new GameStat(3, 0, 0, 0), new GameStat(0, 3, 0, 0)};
            user.setGames(usrGames);
        }
        for (int i=0;i<3;i++) {
            if (usrGames[i] != null) {
                JPanel panel = (JPanel) ((JPanel)mainPanel.getComponent(0)).getComponent(i);
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                JButton selectButton = new JButton("Game " + (i + 1));
                panel.add(selectButton);
                selectButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                selectButton.addMouseListener(frame);
                selectButton.setName("select " + i);
                JButton removeButton = new JButton("remove");
                removeButton.setName("remove " + i);
                panel.add(removeButton);
                removeButton.addMouseListener(frame);
                removeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            }
        }
    }

    @Override
    public void select(String selection) {
        frame.setVisible(false);
        switch (selection.split(" ")[0]) {
            case "select":
                // TODO : start game
                GameManager.getInstance().newGame(Integer.parseInt(selection.split(" ")[1]));
                break;
            case "remove":
                // TODO : remove game
                break;
        }
    }
}

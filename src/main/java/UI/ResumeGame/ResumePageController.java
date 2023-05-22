package UI.ResumeGame;

import Control.GameManager;
import Control.User;
import Game.Model.GameStat;
import UI.FrameController;

import javax.swing.*;

public class ResumePageController extends FrameController {
    public ResumePageController(User user) {
        super(new ResumePage());
        GameStat[] games = user.getGames();
        JPanel panel1 = (JPanel) frame.getContentPane().getComponent(0);
        for (int i=0;i<3;i++) {
            if (games[i].inProgress() && !games[i].isFinished()) {
                panel1.getComponent(i).addMouseListener(frame);
            }
        }
    }

    @Override
    public void select(String selection) {
        frame.setVisible(false);
        switch (selection) {
            case "game0" -> GameManager.getInstance().resumeGame(0);
            case "game1" -> GameManager.getInstance().resumeGame(1);
            case "game2" -> GameManager.getInstance().resumeGame(2);
            case "ExitButton" -> GameManager.getInstance().showMenu();
        }
    }
}

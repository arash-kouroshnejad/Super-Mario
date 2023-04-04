package UI.GameSetup;

import Control.GameManager;
import Control.User;
import UI.FrameController;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;


public class SetupController extends FrameController {
    String character;

    public SetupController(User usr) {
        super(new SetupPage());
        JPanel panel1 = (JPanel) frame.getContentPane().getComponent(0);
        String difficulty = (String) ((JComboBox)((JPanel) panel1.getComponent(0)).getComponent(3)).getSelectedItem(); // TODO : difficulty will not get updated !
        JPanel panel = (JPanel) ((JPanel) panel1.getComponent(1)).getComponent(0);
        HashSet<String> usrChars = usr.getCharacters();
        for (Component btn : panel.getComponents()) {
            if (usrChars.contains(btn.getName())) {
                btn.addMouseListener(frame);
                btn.setVisible(true);
                try {
                    ((JButton)btn).setIcon(new ImageIcon(ImageIO.read(new File("./src/Resources/Sprites/"+btn.getName()+".png")).getScaledInstance(40, 40, Image.SCALE_DEFAULT)));
                } catch (IOException e) {
                    System.out.println("Error reading characters");
                }
            }
        }
    }

    @Override
    public void select(String selection) {
        switch (selection) {
            case "Start" -> {
                // TODO : start the game with the specifies character
                frame.setVisible(false);
                GameManager.getInstance().getSetup(character);
            }
            case "ExitButton" -> {
                frame.setVisible(false);
                GameManager.getInstance().showMenu();
            }
            default -> character = selection;
        }
    }
}

package UI.Profile;

import Control.GameManager;
import Control.User;
import Persistence.Config;
import UI.FrameController;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

public class ProfileController extends FrameController {
    public ProfileController(User usr){
        super(new ProfilePage());
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
        JPanel panel1 = (JPanel) frame.getContentPane().getComponent(0);
        JPanel panel = (JPanel) panel1.getComponent(0);
        JLabel username = (JLabel) panel.getComponent(0);
        username.setText(usr.getUSERNAME());
        panel = (JPanel) panel1.getComponent(1);
        for (Component comp : panel.getComponents()) {
            JLabel label = (JLabel) comp;
            if (usr.getCharacters().contains(label.getName())) {
                try {
                    label.setIcon(new ImageIcon(ImageIO.read(new File(Config.getInstance().getProperty("SpritesDir")
                            + label.getName() + ".png")).getScaledInstance(40, 40, Image.SCALE_DEFAULT)));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                label.setVisible(true);
            }
        }
        panel = (JPanel) panel1.getComponent(2);
        JLabel label = (JLabel) panel.getComponent(0);
        label.setText("Highest : " + usr.getCoins());
    }

    @Override
    public void select(String selection) {

    }
}

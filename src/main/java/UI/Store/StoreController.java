package UI.Store;

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

public class StoreController extends FrameController {
    public StoreController(User usr) {
        super(new StorePage());
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
        JPanel panel = (JPanel) ((JPanel)frame.getContentPane().getComponent(0)).getComponent(1);
        JButton jmp = (JButton) panel.getComponent(0);
        JButton ammo = (JButton) panel.getComponent(1);
        JButton speed = (JButton) panel.getComponent(2);
        JButton coin = (JButton) panel.getComponent(3);
        try {
            String dir = Config.getInstance().getProperty("SpritesDir");
            jmp.setIcon(new ImageIcon(ImageIO.read(new File(dir + "DonkeyKong.png")).getScaledInstance(50 ,50, Image.SCALE_DEFAULT)));
            ammo.setIcon(new ImageIcon(ImageIO.read(new File(dir + "Monster.png")).getScaledInstance(50, 50, Image.SCALE_DEFAULT)));
            speed.setIcon(new ImageIcon(ImageIO.read(new File(dir + "Steve.png")).getScaledInstance(50, 50, Image.SCALE_DEFAULT)));
            coin.setIcon(new ImageIcon(ImageIO.read(new File(dir + "PacMan.png")).getScaledInstance(50, 50, Image.SCALE_DEFAULT)));
        } catch (Exception e) {
            System.out.println("Error Reading Images");
        }
        int coins = usr.getCoins();
        if (coins > 20) {
            jmp.addMouseListener(frame);
            ammo.addMouseListener(frame);
            speed.addMouseListener(frame);
            coin.addMouseListener(frame);
        }
    }

    @Override
    public void select(String selection) {
        // TODO : add selection logic
        frame.setVisible(false);
        if (!selection.equals("ExitButton")) {
            GameManager.getInstance().buyCharacter(selection);
        }
        GameManager.getInstance().showMenu();
    }
}

package UI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.HashMap;

public abstract class Navigable extends JFrame implements MouseListener {
    protected FrameController controller;
    private final Image background;
    protected final JPanel mainPanel;
    public Navigable() {
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        Image temp = null;
        try {
            temp = ImageIO.read(new File("./src/Resources/Sprites/Logo.png"));
        }
        catch(Exception e) {
            System.out.println("Error reading background image! ");
        }
        background = temp;
        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if(background != null) {
                    g.drawImage(background, getWidth() / 2 - background.getWidth(this) /2, 100,this);
                }
            }
        };
        setContentPane(mainPanel);
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.BLACK);
        setBackground(Color.BLACK);
    }

    public void setController(FrameController controller) {
        this.controller = controller;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        controller.select(e.getComponent().getName());
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        e.getComponent().setForeground(Color.WHITE);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        e.getComponent().setForeground(Color.DARK_GRAY);
    }
}

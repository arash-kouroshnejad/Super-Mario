package Core.Render;

import Core.Util.Logic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameFrame extends JFrame {
    GameEngine engine;
    Logic gameLogic;
    public GameFrame() {
        engine = GameEngine.getInstance();
        gameLogic = engine.getGameLogic();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setVisible(true);
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                engine.paint(g);
            }
        };
        setContentPane(mainPanel);
        mainPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                engine.resize(getSize());
            }
        });
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                gameLogic.handleKeyPress(e.getKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                gameLogic.handleKeyRelease(e.getKeyCode());
            }
        });
    }

    public JPanel getPanel() {
        return (JPanel) getContentPane();
    }
}

package core.editor;

import core.util.Loader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class SpritePicker extends JFrame {
    private final JTextField speedX = new JTextField("0");

    private final JTextField speedY = new JTextField("0");

    private final JTextField x = new JTextField("0");

    private final JTextField y = new JTextField("0");

    private final JTextField layer = new JTextField("0");

    private final LevelEditor editor = LevelEditor.getInstance();
    public SpritePicker() {
        JPanel mainPanel = new JPanel();
        JScrollPane scrollable = new JScrollPane(mainPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        setContentPane(scrollable);
        mainPanel.setLayout(new GridLayout(0, 5));
        mainPanel.add(speedX);
        mainPanel.add(speedY);
        mainPanel.add(x);
        mainPanel.add(y);
        mainPanel.add(layer);
        Loader loader = LevelEditor.getInstance().getLoader();
        String[] types = loader.getTYPES();
        for (String str : types) {
            JButton button = new JButton();
            button.setName(str);
            button.setSize(new Dimension(40, 40));
            button.setIcon(new ImageIcon(loader.getSprite(str).get(0).getScaledInstance(40, 40, Image.SCALE_DEFAULT)));
            button.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    ArrayList<Image> images = loader.getSprite(str);
                    JButton[] btns = new JButton[images.size()];
                    for (int i=0;i<btns.length;i++) {
                        btns[i] = new JButton(new ImageIcon(images.get(i).getScaledInstance(40, 40, Image.SCALE_DEFAULT)));
                        btns[i].setName(String.valueOf(i));
                    }
                    // int state = JOptionPane.showOptionDialog(mainPanel, "Select State", null, JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, btns, btns[0]);
                    StatePicker sp = new StatePicker(btns);
                    Thread waitThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (sp.isVisible()) {
                                try {
                                    Thread.sleep(200);
                                } catch (InterruptedException ex) {
                                    throw new RuntimeException(ex);
                                }
                            }
                            editor.insertAt(str, getXPos(), getYPos(), sp.getChoice(), getLayer());
                        }
                    });
                    waitThread.start();
                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });
            mainPanel.add(button);
        }
        addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                editor.createMap();
            }

            @Override
            public void windowClosed(WindowEvent e) {
            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });
        pack();
        setVisible(true);
    }

    public int getSpeedX() {
        return Integer.parseInt(speedX.getText());
    }

    public int getSpeedY() {
        return Integer.parseInt(speedY.getText());
    }

    public int getXPos() {
        return Integer.parseInt(x.getText());
    }

    public int getYPos() {
        return Integer.parseInt(y.getText());
    }

    public int getLayer() {
        return Integer.parseInt(layer.getText());
    }
}

class StatePicker extends JFrame {
    private int choice;
    public StatePicker (JButton[] buttons) {
        JPanel mainPanel = (JPanel) getContentPane();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        for (JButton button : buttons) {
            mainPanel.add(button);
            button.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    setVisible(false);
                    choice = Integer.parseInt(button.getName());
                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });
        }
        pack();
        setVisible(true);
    }
    public int getChoice() {
        return choice;
    }
}

package core.render;

import core.objects.DynamicElement;
import core.objects.StaticElement;

import javax.swing.*;
import java.awt.*;

public class ViewPort {
    private final static ViewPort instance = new ViewPort();
    private ViewPort() {}
    private int x, y, width, height;
    private JFrame frame;
    private DynamicElement lockedElement;

    public DynamicElement getLockedElement() {
        return lockedElement;
    }

    public void setLockedElement(DynamicElement lockedElement) {
        this.lockedElement = lockedElement;
        x = lockedElement.getX() - width / 2;
    }

    public static ViewPort getInstance() {
        return instance;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
        Dimension size = frame.getSize();
        width = size.width;
        height = size.height;
    }

    public boolean inView(StaticElement element) {
        if (lockedElement != null) {
            if (Math.min(Math.abs(element.getBounds().LEFT - lockedElement.getX()),
                    Math.abs(element.getBounds().RIGHT - lockedElement.getX())) > width / 2)
                return false;
            return element.getY() - y <= height;
        }
        return true;
    }

    public void update() {
        if (lockedElement != null)
            x = lockedElement.getX() - width / 2;
        frame.repaint();
        frame.revalidate();
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Dimension getDimension() {
        return new Dimension(width, height);
    }
}

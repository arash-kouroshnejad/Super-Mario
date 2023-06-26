package Core.Objects;

import java.awt.*;
import java.util.ArrayList;

public class StaticElement {
    protected int x,y, width, height;
    protected transient Image image;
    private transient ArrayList<Image> images;
    private final String type;

    private boolean hidden;

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public StaticElement(int x, int y, int width, int height, String type) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.type = type;
    }

    public void swapImage(int index) {
        if (images.size() > index) {
            image = images.get(index);
        }
    }

    public void setImages(ArrayList<Image> images) {
        this.images = images;
        if (images.size() > 0) {
            image = images.get(0);
        }
    }

    public void rotateImage() {
        int index = images.indexOf(image);
        image = images.get(index == images.size() - 1 ? 0 : index + 1);
    }
    public Image getImage() {
        return image;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Bounds getBounds() {
        return new Bounds(y, y + height, x + width, x);
    }

    public boolean collidesHorizontally(StaticElement element) {
        Bounds bounds = element.getBounds();
        Bounds bounds1 = getBounds();
        return !(Math.abs(bounds.TOP - bounds1.BOTTOM)  < 11 || Math.abs(bounds.BOTTOM - bounds1.TOP) < 11);
        // return element.getBounds().LEFT >= getBounds().RIGHT || element.getBounds().RIGHT <= getBounds().LEFT;
    }

    public boolean collidesWith(StaticElement element) {
        Bounds bounds = element.getBounds();
        if (bounds.RIGHT >= x && bounds.LEFT <= x + width) {
            return bounds.BOTTOM >= y && bounds.TOP <= y + height;
        }
        return false;
    }

    public boolean beneath(StaticElement element) {
        return Math.abs(getBounds().TOP - element.getBounds().BOTTOM) < 10;
    }

    public String getType() {
        return type;
    }

    public Dimension getDimensions() {
        return new Dimension(width, height);
    }

    public Point getPosition() {
        return new Point(x, y);
    }
}

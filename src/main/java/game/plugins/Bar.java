package game.plugins;


import core.editor.LevelEditor;
import core.objects.StaticElement;
import core.render.ViewPort;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Bar {
    private static final Map<String, Bar> allBars = new HashMap<>();
    private Bar(String name) {
        this.name = name;
        index = allBars.size();
        generateContent();
        allBars.put(name, this);
    }
    private final String name;
    private int percentage;
    private StaticElement content;
    private StaticElement container;
    private Dimension contentDimension;
    private Dimension containerDimension;
    private int index;

    public static Bar getBar(String name) {
        if (allBars.containsKey(name))
            return allBars.get(name);
        return new Bar(name);
    }
    public static void resetAllBars() {
        allBars.clear();
    }

    private void generateContent() {
        var editor = LevelEditor.getInstance();
        var dimension = ViewPort.getInstance().getDimension();
        Point deploy = new Point((dimension.width - 400) / 2, 330 + index * 50);
        containerDimension = editor.getLoader().getDimension("Bar");
        contentDimension = editor.getLoader().getDimension("FilledBar");
        int delta = containerDimension.height - contentDimension.height;
        editor.staticInsert("Bar", deploy.x, deploy.y, 0, 2);
        editor.staticInsert("FilledBar", deploy.x, deploy.y + delta / 2, 0, 2);
        content = editor.getStaticElement("FilledBar", 2, index).orElseThrow();
        container = editor.getStaticElement("Bar", 2, index).orElseThrow();
    }
    public Point getTilePosition() {
        var dimension = ViewPort.getInstance().getDimension();
        return new Point((dimension.width - name.length() * 10) / 2, 300 + index * 70);
    }
    public void setPercentage(int percentage) {
        this.percentage = percentage;
        content.setWidth((int) (containerDimension.width * (percentage / (double) 100)) - 8);
    }
    public int getPercentage() {
        content.setX(ViewPort.getInstance().getLockedElement().getX()); // TODO : minus some delta
        container.setX(ViewPort.getInstance().getLockedElement().getX());
        return percentage;
    }
    private void reLocate() {
        var editor = LevelEditor.getInstance();
        editor.removeElement(editor.getStaticElement("Bar", 2, index - 1).orElseThrow());
        index--;
        generateContent();
    }
    public void remove() {
        allBars.remove(name);
        var editor = LevelEditor.getInstance();
        editor.removeElement(editor.getStaticElement("Bar", 2, index).orElseThrow());
        editor.removeElement(editor.getStaticElement("FilledBar", 2, index).orElseThrow());
        for (var entry : allBars.entrySet())
            if (entry.getValue().index > index)
                entry.getValue().reLocate();
    }
}

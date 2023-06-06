package Game.Plugins;


import Core.Editor.LevelEditor;
import Core.Objects.StaticElement;
import Core.Render.ViewPort;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Bar {
    private static final Map<String, Bar> allBars = new HashMap<>();
    private Bar(String name) {
        this.name = name;
        index = allBars.size();
        content = generateContent();
        allBars.put(name, this);
    }
    private final String name;
    private int percentage;
    private StaticElement content;
    private int index;
    public static Bar getBar(String name) {
        return allBars.getOrDefault(name, new Bar(name));
    }
    private StaticElement generateContent() {
        var editor = LevelEditor.getInstance();
        var dimension = ViewPort.getInstance().getDimension();
        Point deploy = new Point((dimension.width - 400) / 2, 330 + index * 50);
        editor.staticInsert("Bar", deploy.x, deploy.y, 0, 2);
        editor.staticInsert("FilledBar", deploy.x, deploy.y, 0, 2);
        return editor.getStaticElement("FilledBar", 2, index).orElseThrow();
    }
    public Point getTilePosition() {
        var dimension = ViewPort.getInstance().getDimension();
        return new Point((dimension.width - name.length() * 10) / 2, 300 + index * 50);
    }
    public void setPercentage(int percentage) {
        this.percentage = percentage;
        content.setWidth((int) (600 * (percentage / (double) 100)));
    }
    public int getPercentage() {
        content.setX(ViewPort.getInstance().getLockedElement().getX()); // TODO : minus some delta
        return percentage;
    }
    private void reLocate() {
        var editor = LevelEditor.getInstance();
        editor.removeElement(editor.getStaticElement("Bar", 2, index - 1).orElseThrow());
        index--;
        content = generateContent();
    }
    public void remove() {
        allBars.remove(name);
        var editor = LevelEditor.getInstance();
        editor.removeElement(editor.getStaticElement("Bar", 2, index).orElseThrow());
        for (var entry : allBars.entrySet())
            if (entry.getValue().index > index)
                entry.getValue().reLocate();
    }
}

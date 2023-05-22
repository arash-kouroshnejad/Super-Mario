package Game.Plugins;

import Core.Editor.LevelEditor;
import Game.Util.Event;
import Game.Util.EventQueue;
import Game.Util.EventType;
import Persistence.Config;

import java.awt.*;

public class ModalPanel {
    private static final ModalPanel instance = new ModalPanel();
    private ModalPanel(){}
    private Point coordinates;
    private int layer;
    private String[] visibles;
    private int optionWidth;
    private int optionHeight;
    private int modalHeight;
    private int modalWidth;
    private int defaultPadding;
    private int margin_Top_Bottom;
    private int margin_Left_Right;
    private boolean up;

    public void init(Point coordinates, String[] visibles) {
        this.coordinates = coordinates;
        LevelEditor editor = LevelEditor.getInstance();
        Config c = Config.getInstance();
        this.visibles = visibles;
        // create the main panel
        layer = c.getProperty("ModalLayer", Integer.class);
        optionWidth = c.getProperty("ModalOptionWidth", Integer.class);
        optionHeight = c.getProperty("ModalOptionHeight", Integer.class);
        modalHeight = c.getProperty("ModalHeight", Integer.class);
        modalWidth = c.getProperty("ModalWidth", Integer.class);
        defaultPadding = Config.getInstance().getProperty("ModalOptionPadding", Integer.class);
        margin_Top_Bottom = (modalHeight - optionHeight) / 2;
        margin_Left_Right = (modalWidth - visibles.length * optionWidth - (visibles.length - 1) * 40) / 2;
        coordinates.x = coordinates.x - (modalWidth) / 2;
        editor.insertAt("Modal", coordinates.x, coordinates.y,
                0, layer - 1);

        // add elements to the panel
        addOptions();
        up = true;
    }

    private void addOptions() {
        var editor = LevelEditor.getInstance();
        int padding = 0;
        coordinates.x = coordinates.x + margin_Left_Right;
        coordinates.y = coordinates.y + margin_Top_Bottom;
        for (int i = 0; i < visibles.length; i++) {
            editor.insertAt(visibles[i], coordinates.x + i * optionWidth + padding, coordinates.y, 0,
                    layer - 1);
            padding += defaultPadding;
        }
    }

    public void modifyOptions(String[] visibles) {
        removeOptions();
        this.visibles = visibles;
        addOptions();
    }

    public void removeModal() {
        var editor = LevelEditor.getInstance();
        editor.removeElement("Modal", layer - 1);
        removeOptions();
        up = false;
    }

    private void removeOptions() {
        var editor = LevelEditor.getInstance();
        for (String option : visibles)
            editor.removeElement(option, layer - 1);
    }

    public void pick (int x, int y) {
        double indexRaw = (x - coordinates.x) / (double)(optionWidth + defaultPadding);
        double ratio = defaultPadding / (double)(defaultPadding + optionWidth);
        int index = (int) indexRaw;
        if (y >= coordinates.y && y <= coordinates.y + optionHeight) {
            if (index >= 0 && (indexRaw - index) <= ratio) {
                EventQueue.getInstance().publish(new Event(EventType.ModalOptionClicked, visibles[index]));
            }
        }
    }

    public boolean isUp() {
        return up;
    }

    public static ModalPanel getInstance() {
        return instance;
    }
}

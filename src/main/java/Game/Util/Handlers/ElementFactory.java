package Game.Util.Handlers;

import Core.Editor.LevelEditor;
import Core.Render.ViewPort;
import Game.Model.Mario;
import Game.Plugins.ElementManagers.ItemManager;
import Game.Plugins.ElementManagers.MarioThread;
import Game.Plugins.ElementManagers.ShieldThread;
import Game.Util.Events.Event;
import Game.Util.Events.EventHandler;
import Persistence.Config;

import java.awt.*;


public class ElementFactory extends EventHandler{
    private final RandomItemGenerator rIg = new RandomItemGenerator();

    public ElementFactory(Event event) {
        super(event);
    }

    public void run() {
        String command = event.attribute().split(",")[1];
        String[] rawCoordinates = event.attribute().split(",")[0].split("x");
        Point point = new Point(Integer.parseInt(rawCoordinates[0]), Integer.parseInt(rawCoordinates[1]));
        var editor = LevelEditor.getInstance();
        int layerIndex = Config.getInstance().getProperty("DynamicsLayer",
                Integer.class);
        if (command.equals("Item")) {
            // editor.insertAt(rIg.getRandomItem(), point.x, point.y, 0, 0, 0, layerIndex);
            editor.insertAt("Star", point.x, point.y, 0, 0, 0, layerIndex);
            editor.attachManager(ItemManager.class);
        } else if (command.split("-")[0].equals("Mario")){
            var manager = ViewPort.getInstance().getLockedElement().getManager();
            if (manager != null)
                manager.kill();
            /*int lastState = Integer.parseInt(elementType.split("-")[1].split("/")[0]);
            switch (Mario.getInstance().getMarioState(lastState)) {
                case MiniMario -> editor.removeElement(editor.getDynamicElement("MiniMario",
                        layerIndex, -1).orElseThrow());
                case MegaMario -> editor.removeElement(editor.getDynamicElement("MegaMario",
                        layerIndex, -1).orElseThrow());
                case FireMario -> editor.removeElement(editor.getDynamicElement("FireMario",
                        layerIndex, -1).orElseThrow());
            }*/
            editor.removeElement(ViewPort.getInstance().getLockedElement());
            int newState = Integer.parseInt(command.split("-")[1].split("/")[1]);
            switch (Mario.getInstance().getMarioState(newState)) {
                case MiniMario -> editor.insertAt("MiniMario", point.x, point.y, 0, 0,
                        0, layerIndex);
                case MegaMario -> editor.insertAt("MegaMario", point.x, point.y, 0, 0,
                        0, layerIndex);
                case FireMario -> editor.insertAt("FireMario", point.x, point.y, 0, 0,
                        0, layerIndex);
            }
            editor.attachManager(MarioThread.class);
        } else if (command.equals("GenerateShield")) {
            // editor.insertAt("GoldenRing", point.x, point.y, 0, 0, 0, 3);
            // editor.attachManager(ShieldThread.class);
        } else if (command.equals("RemoveShield")) {
            // editor.removeElement("GolderRing", 3);
        }
    }
    /*protected void register(Queue<Event> queue) {
        for (var event : queue) {
            if (event.type().equals(EventType.PowerUpTriggered)) {
                String[] coordinates = event.attribute().split("x");
                Point point = new Point(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1]));
                var editor = LevelEditor.getInstance();
                int layerIndex = Config.getInstance().getProperty("DynamicsLayer",
                        Integer.class);
                editor.insertAt(rIg.getRandomItem(), point.x, point.y, 0, 0, 0, layerIndex);
                editor.attachManager(ItemManager.class);
                EventQueue.getInstance().consume(event);
            }
        }
        semaphore.forceLock();
    }*/
    private static class RandomItemGenerator {
        public String getRandomItem() {
            int rand = (int) (Math.random() * 3);
            return switch (rand) {
                case 1 -> "Flower";
                case 2 -> "Star";
                case 0 -> "Mushroom";
                default -> null;
            };
        }
    }
}

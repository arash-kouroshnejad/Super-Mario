package Game.Util.Handlers;

import Core.Editor.LevelEditor;
import Core.Objects.DynamicElement;
import Game.Plugins.ElementManagers.ItemManager;
import Game.Plugins.ElementManagers.MarioThread;
import Game.Util.Events.Event;
import Game.Util.Events.EventHandler;
import Game.Util.Events.EventQueue;
import Game.Util.Events.EventType;
import Persistence.Config;


import java.awt.*;
import java.util.Queue;

public class ElementGenerator extends EventHandler {
    private final RandomItemGenerator rIg = new RandomItemGenerator();
    @Override
    protected void register(Queue<Event> queue) {
        for (var event : queue) {
            if (event.type().equals(EventType.PowerUpTriggered)) {
                String[] coordinates = event.attribute().split("x");
                Point point = new Point(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1]));
                var editor = LevelEditor.getInstance();
                int layerIndex = Config.getInstance().getProperty("DynamicsLayer",
                        Integer.class);
                editor.insertAt(rIg.getRandomItem(), point.x, point.y, 0, 2, -10, layerIndex);
                // editor.attachManager(ItemManager.class);
                EventQueue.getInstance().consume(event);
            }
        }
        semaphore.forceLock();
    }
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

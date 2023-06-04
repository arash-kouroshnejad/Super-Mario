package Game.Util.Handlers;

import Core.Editor.LevelEditor;
import Core.Render.ViewPort;
import Game.Model.Mario;
import Game.Plugins.ElementManagers.*;
import Game.Util.Events.Event;
import Game.Util.Events.EventHandler;
import Persistence.Config;

import java.awt.*;


public class ElementFactory extends EventHandler{

    static int layerIndex = Config.getInstance().getProperty("DynamicsLayer",
            Integer.class);
    static int staticLayer = Config.getInstance().getProperty("StaticsLayer", Integer.class);

    private final RandomItemGenerator rIg = new RandomItemGenerator();

    public ElementFactory(Event event) {
        super(event);
    }

    public void run() {
        String command = event.attribute().split(",")[1];
        String[] rawCoordinates = event.attribute().split(",")[0].split("x");
        Point point = new Point(Integer.parseInt(rawCoordinates[0]), Integer.parseInt(rawCoordinates[1]));
        var editor = LevelEditor.getInstance();
        switch (command) {
            case "Item" -> {
                // editor.insertAt(rIg.getRandomItem(), point.x, point.y, 0, 0, 0, layerIndex);
                editor.insertAt("Star", point.x, point.y, 0, 0, 0, layerIndex);
                editor.attachManager(ItemManager.class);
            }
            case "MiniMario", "MegaMario", "FireMario" -> {
                editor.removeElement(ViewPort.getInstance().getLockedElement());
                editor.insertAt(command, point.x, point.y, 0, 0, 0, layerIndex);
                editor.attachManager(MarioThread.class);
            }
            case "GenerateShield" -> {
                // editor.staticInsert("GoldenRing", point.x, point.y, 0,0);
                editor.insertAt("GoldenRing", point.x, point.y, 0, 0, 0, layerIndex);
                editor.attachManager(ShieldThread.class);
            }
            case "RemoveShield" -> editor.removeElement("GoldenRing", layerIndex);
            case "Bullet" -> {
                var mario = ViewPort.getInstance().getLockedElement();
                boolean mirrored = mario.getManager().isMirrored();
                Point deploy = new Point((mirrored) ? mario.getX() - 10 : mario.getX() + 60, mario.getY() +
                        mario.getHeight() / 2 - 20);
                editor.insertAt("Bullet", deploy.x, deploy.y, (mirrored) ? 1 : 0, (mirrored) ? -2 : 2, 0,
                        layerIndex);
                editor.attachManager(BulletThread.class);
            }
            case "Sword" -> {
                var coordinates = ViewPort.getInstance().getLockedElement().getPosition();
                editor.insertAt("PipeSword", coordinates.x, coordinates.y, 0, 0, 0, layerIndex);
                editor.attachManager(SwordThread.class);
            }
            case "Coin" -> {
                editor.insertAt("Coin", point.x, point.y - 40, 0, 0, 0, layerIndex);
                editor.attachManager(CoinThread.class);
            }
            case "Brick" -> editor.staticInsert("Brick", point.x, point.y, 0, staticLayer);
            case "FilledBlock" -> editor.staticInsert("FilledBlock", point.x, point.y, 0, staticLayer);
        }
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

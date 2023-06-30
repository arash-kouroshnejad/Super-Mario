package game.util.handlers;

import core.editor.LevelEditor;
import core.render.ViewPort;
import game.animations.items.*;
import game.animations.mario.MarioThread;
import game.util.events.Event;
import game.util.events.EventHandler;
import persistence.Config;

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
                editor.attachManager(ItemsThread.class);
            }
            case "MiniMario", "MegaMario", "FireMario" -> {
                editor.removeElement(ViewPort.getInstance().getLockedElement());
                editor.insertAt(command, point.x, point.y - 40, 0, 0, 0, layerIndex);
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
                editor.insertAt("PipeSword", point.x, point.y, 0, 0, 0, layerIndex);
                editor.attachManager(SwordThread.class);
            }
            case "Coin" -> {
                editor.insertAt("Coin", point.x, point.y - 40, 0, 0, 0, layerIndex);
                editor.attachManager(CoinThread.class);
            }
            case "Brick" -> editor.staticInsert("Brick", point.x, point.y, 0, staticLayer);
            case "FilledBlock" -> editor.staticInsert("FilledBlock", point.x, point.y, 0, staticLayer);
            case "Bomb" -> editor.insertAt("Bomb", point.x, point.y, 0, 0, 0, layerIndex);
            case "FireBall" -> {
                var bowser = editor.getDynamicElement("Bowser", layerIndex, 0).orElseThrow();
                boolean mirrored = bowser.getManager().isMirrored();
                Point deploy = new Point(mirrored ? point.x - 40 : bowser.getBounds().RIGHT + 20, point.y);
                editor.insertAt("FireBall", deploy.x, deploy.y, 0, mirrored ? -2 : 2, 0, layerIndex);
            }
            case "HPBar" -> {
                /*var bar = Bar.getBar("HPBar");
                bar.setPercentage(100);*/
            }
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

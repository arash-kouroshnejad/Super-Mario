package Game;

import Core.Util.Loader;

import java.awt.*;

public abstract class SpriteLoader extends Loader {

    public SpriteLoader(String PathToMaps) {
        super("./src/Resources/Sprites/", PathToMaps);
        TYPES = new String[]{"Mario", "Steve", "PacMan", "Ghost", "Monster", "Star", "Goomba", "Mushroom", "Plant", "Coin", "BlueBackground", "Grass", "Logo",
                "PowerUpBlock", "SingleCloud", "TwoClouds", "Pipe", "Floor", "Stair", "Brick", "PipeExtension", "FlagPole"};
        references.put("Mario", new String[]{"MarioStill", "MarioSprint1", "MarioSprint2", "MarioSprint3", "MarioMirrored", "MarioJump"});
        references.put("Mushroom", new String[]{"Mushroom"});
        references.put("Star", new String[]{"Star"});
        references.put("Stair", new String[]{"Stair"});
        references.put("BlueBackground", new String[]{"BlueBackground"});
        references.put("Goomba", new String[]{"Goomba"});
        references.put("Coin", new String[]{"Coin", "CoinMirrored"});
        references.put("Grass", new String[]{"Grass"});
        references.put("Logo", new String[]{"Logo", "NewLogo"});
        references.put("PowerUpBlock", new String[]{"PowerUpReady", "PowerUpTaken"});
        references.put("SingleCloud", new String[]{"SingleCloud"});
        references.put("TwoClouds", new String[]{"TwoClouds"});
        references.put("Pipe", new String[]{"VerticalPipe", "HorizontalPipe"});
        references.put("Brick", new String[]{"Brick"});
        references.put("Floor", new String[]{"BlockFloor"});
        references.put("Plant", new String[]{"Plant", "Plant2"});
        references.put("PipeExtension", new String[]{"PipeExtension"});
        references.put("FlagPole", new String[]{"FlagPole"});
        references.put("Steve", new String[]{"Steve"});
        references.put("PacMan", new String[]{"PacMan"});
        references.put("Ghost", new String[]{"Ghost"});
        references.put("Monster", new String[]{"Monster"});
        Dimension[] dimensions1 = new Dimension[]{new Dimension(30, 40), new Dimension(20, 40),
                new Dimension(40, 40), new Dimension(30, 40), new Dimension(30, 30),
                    new Dimension(30, 30), new Dimension(30, 30), new Dimension(30, 30),
                        new Dimension(30, 30), new Dimension(30, 30), new Dimension(1600, 1600),
                            new Dimension(30 ,30), new Dimension(950, 350), new Dimension(30, 30),
                                new Dimension(70, 40), new Dimension(120 ,80), new Dimension(100, 100),
                                    new Dimension(634, 228), new Dimension(30, 30), new Dimension(30 ,30),
                                        new Dimension(40, 50), new Dimension(50, 200)};
        for (int i=0;i<TYPES.length;i++) {
            dimensions.put(TYPES[i], dimensions1[i]);
        }
        loadSprites();
    }

    @Override
    public boolean isLocked(String type) {
        return type.equals("Mario") || type.equals("PacMan") || type.equals("Ghost") || type.equals("Steve");
    }

    @Override
    public boolean isDynamic(String type) {
        for (int i=0;i<TYPES.length; i++) {
            if (TYPES[i].equals(type)) {
                if (i < 9) {
                    return true;
                }
            }
        }
        return false;
    }
}

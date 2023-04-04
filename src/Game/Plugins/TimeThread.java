package Game.Plugins;

import Core.Render.GameEngine;
import Core.Util.Loader;
import Core.Util.Logic;
import Game.SpriteLoader;
import Game.MarioLogic;

public class TimeThread extends Thread{
    private static final TimeThread instance = new TimeThread();
    private TimeThread(){}
    public static TimeThread getInstance(){return instance;}
    private final int seconds = 2;
    private final Logic gameLogic = new MarioLogic();
    private final Loader marioLoader = new SpriteLoader();
    private int mapID = 1;

    public void run () {
        try {
            marioLoader.loadMap(mapID, 0);
            GameEngine.getInstance().init(gameLogic);
            GameEngine.getInstance().startGame();
            gameLogic.init(marioLoader);
            Thread.sleep(seconds);
            /*if (Map.getCurrentId() != 1) {
                mapID = 1;
                run();
            }*/
        } catch (Exception e) {
            throw new RuntimeException();
        }

    }

    public int getMapID() {
        return mapID;
    }

    public void setMapID(int mapID) {
        this.mapID = mapID;
    }
}

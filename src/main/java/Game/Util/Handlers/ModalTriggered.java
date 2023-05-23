package Game.Util.Handlers;

import Control.GameManager;
import Core.Render.GameEngine;
import Game.Plugins.ModalPanel;
import Game.Util.Events.Event;
import Game.Util.Events.EventHandler;
import Game.Util.Events.EventQueue;
import Game.Util.Events.EventType;



import java.util.Queue;

public class ModalTriggered extends EventHandler {

    private final ModalPanel modal = ModalPanel.getInstance();

    protected void register(Queue<Event> events) {
        for (var event : events) {
            if (event.type().equals(EventType.ModalTriggered)) {
                if (!modal.isUp()) {
                    modal.init(GameManager.getInstance().getGameLogic().getModalPosition(),
                            GameManager.getInstance().getGameLogic().getModalOptions(event.attribute()));
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    GameEngine.getInstance().getGameLogic().pauseElementManagers();
                    GameEngine.getInstance().pauseAnimation();
                } else {
                    modal.removeModal();
                    GameEngine.getInstance().getGameLogic().resumeElementManagers();
                    GameEngine.getInstance().resumeAnimation();
                }
                EventQueue.getInstance().consume(event);
            }
        }
        semaphore.forceLock();
    }
}

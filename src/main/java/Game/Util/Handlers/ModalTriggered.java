package Game.Util.Handlers;

import Core.Render.GameEngine;
import Game.Plugins.ModalPanel;
import Game.Util.Event;
import Game.Util.EventHandler;
import Game.Util.EventQueue;
import Game.Util.EventType;



import java.util.Queue;

public class ModalTriggered extends EventHandler {

    private final ModalPanel modal = ModalPanel.getInstance();

    protected void register(Queue<Event> events) {
        for (var event : events) {
            if (event.type().equals(EventType.ModalTriggered)) {
                if (!modal.isUp()) {
                    modal.init(GameEngine.getInstance().getGameLogic().getModalPosition(),
                            GameEngine.getInstance().getGameLogic().getModalOptions(event.attribute()));
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    GameEngine.getInstance().pauseAnimation();
                } else {
                    modal.removeModal();
                    GameEngine.getInstance().resumeAnimation();
                }
                EventQueue.getInstance().consume(event);
            }
        }
        semaphore.forceLock();
    }
}

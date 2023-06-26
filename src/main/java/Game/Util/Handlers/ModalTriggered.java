package Game.Util.Handlers;

import Control.GameManager;
import Core.Render.GameEngine;
import Game.Plugins.ModalPanel;
import Game.Util.Events.Event;
import Game.Util.Events.EventHandler;
import Game.Util.Events.EventQueue;
import Game.Util.Events.EventType;



import java.util.Queue;
import java.util.Set;

public class ModalTriggered extends EventHandler {

    public ModalTriggered(Event event) {
        super(event);
    }

    private final ModalPanel modal = ModalPanel.getInstance();

    public void run() {
        if (!modal.isUp()) {
            modal.init(GameManager.getInstance().getGameLogic().getModalPosition(),
                    GameManager.getInstance().getGameLogic().getModalOptions(event.attribute()));
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            GameManager.getInstance().pause();

        } else {
            modal.removeModal();
            GameManager.getInstance().resume();
        }
    }

    /*public ModalTriggered() {
        setName("Modal Triggered");
    }

    protected void register(Set<Event> events) {
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
                    GameEngine.getInstance().pauseAnimation();
                } else {
                    modal.removeModal();
                    GameEngine.getInstance().resumeAnimation();
                }
                EventQueue.getInstance().consume(event);
            }
        }
        semaphore.forceLock();
    }*/
}

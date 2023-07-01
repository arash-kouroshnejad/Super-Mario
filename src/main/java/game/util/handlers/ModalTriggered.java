package game.util.handlers;

import control.GameManager;
import game.plugins.ModalPanel;
import game.util.events.Event;
import game.util.events.EventHandler;

public class ModalTriggered extends EventHandler {

    public ModalTriggered(Event event) {
        super(event);
    }

    public void run() {
        ModalPanel modal = ModalPanel.getInstance();
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
}

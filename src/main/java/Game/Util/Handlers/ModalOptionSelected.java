package Game.Util.Handlers;

import Control.GameManager;
import Core.Render.GameEngine;
import Game.Plugins.ModalPanel;
import Game.Plugins.SoundQueue;
import Game.Util.Event;
import Game.Util.EventHandler;
import Game.Util.EventQueue;
import Game.Util.EventType;

import java.util.Queue;

public class ModalOptionSelected extends EventHandler {
    @Override
    protected void register(Queue<Event> queue) {
        for (var event : queue) {
            if (event.type().equals(EventType.ModalOptionClicked)) {
                switch (event.attribute()) {
                    case "Save" -> {

                    }
                    case "Pass" -> {

                    }
                    case "AudioOut" -> {
                        SoundQueue.getInstance().unmute();
                        SoundQueue.getInstance().play("Background", true, true);
                    }
                    case "Mute" -> {
                        SoundQueue.getInstance().mute();
                    }
                    case "Exit" -> {
                        SoundQueue.getInstance().pause();
                        GameEngine.getInstance().closeGame();
                        GameManager.getInstance().showMenu();
                    }
                    case "Resume" -> {
                        ModalPanel.getInstance().removeModal();
                        GameEngine.getInstance().resumeAnimation();
                    }
                }
                EventQueue.getInstance().consume(event);
            }
        }
        semaphore.forceLock();
    }
}

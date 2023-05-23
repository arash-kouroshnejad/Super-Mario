package Game.Util.Handlers;

import Control.GameManager;
import Core.Render.GameEngine;
import Game.Plugins.ModalPanel;
import Game.Plugins.SoundQueue;
import Game.Util.Events.Event;
import Game.Util.Events.EventHandler;
import Game.Util.Events.EventQueue;
import Game.Util.Events.EventType;

import java.util.Queue;

public class ModalOptionSelected extends EventHandler {
    @Override
    protected void register(Queue<Event> queue) {
        for (var event : queue) {
            if (event.type().equals(EventType.ModalOptionClicked)) {
                switch (event.attribute()) {
                    case "Save" -> {
                        ModalPanel.getInstance().removeModal();
                        GameEngine.getInstance().getGameLogic().saveGame();
                        GameManager.getInstance().saveProgress();
                        GameEngine.getInstance().resumeAnimation();
                    }
                    case "Pass" -> {
                        GameManager.getInstance().getGameLogic().withdrawCheckpoint();
                        ModalPanel.getInstance().removeModal();
                        GameEngine.getInstance().resumeAnimation();
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

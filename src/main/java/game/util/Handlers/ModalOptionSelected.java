package game.util.Handlers;

import control.GameManager;
import core.render.GameEngine;
import game.plugins.ModalPanel;
import game.plugins.SoundQueue;
import game.util.Events.Event;
import game.util.Events.EventHandler;

public class ModalOptionSelected extends EventHandler {

    public ModalOptionSelected(Event event) {
        super(event);
    }

    public void run() {
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
    }

    /*public ModalOptionSelected() {
        setName("Modal Option Selected");
    }
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
    }*/
}

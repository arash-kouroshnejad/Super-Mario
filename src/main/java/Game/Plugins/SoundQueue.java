package Game.Plugins;

import Core.Util.Logic;
import Core.Util.Semaphore;
import Persistence.Config;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.util.HashMap;

public class SoundQueue {
    private static final SoundQueue instance = new SoundQueue();
    private final String[] types;
    private final HashMap<String, byte[]> sounds = new HashMap<>();
    private final HashMap<String, Long> positions = new HashMap<>();
    private String currentTrack;
    private Clip clip;
    private final Listener listener = new Listener();
    private final Semaphore semaphore = new Semaphore(0);
    private boolean muted;

    private SoundQueue() {
        Config c = Config.getInstance();
        String dir = c.getProperty("SFXDir");
        types = c.getProperty("AllSounds").split(",");
        FileInputStream input;
        for (String type : types) {
            try {
                input = new FileInputStream(dir + c.getProperty(type));
                sounds.put(type, input.readAllBytes());
            } catch (Exception e) {
                System.out.println("Could not open " + dir + type);
            }
        }
        try {
            clip = AudioSystem.getClip();
        } catch (Exception e) {
            System.out.println("Could Not get Clip");
        }
    }

    public void init(Logic l) {
    }

    public void play(String type, boolean looped, boolean continued) {
        if (!muted) {
            currentTrack = type;
            if (clip.isOpen()) {
                pause();
            }
            try {
                AudioInputStream stream = AudioSystem.getAudioInputStream(new ByteArrayInputStream(sounds.get(type)));
                clip.open(stream);
                if (continued)
                    clip.setMicrosecondPosition(positions.get(type));
                clip.start();
                if (looped) {
                    clip.removeLineListener(listener);
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                }
                else {
                    clip.addLineListener(listener);
                    semaphore.forceLock();
                }
            } catch (Exception ignored) {}
        }
    }

    public void pause() {
        saveLastTrackPosition();
        clip.stop();
        clip.close();
    }

    private void saveLastTrackPosition() {positions.put(currentTrack, clip.getMicrosecondPosition());}

    public void mute() {
        pause();
        muted = true;
    }

    public void unmute() {
        muted = false;
    }

    public static SoundQueue getInstance() {
        return instance;
    }

    private class Listener implements LineListener{
        @Override
        public void update(LineEvent event) {
            if (event.getType() == LineEvent.Type.STOP)
                    semaphore.forceRelease();
        }
    }
}

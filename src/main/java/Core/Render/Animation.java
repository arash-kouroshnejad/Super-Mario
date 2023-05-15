package Core.Render;

import Core.Util.Semaphore;

public class Animation extends Thread{
    private final int FPS;
    private boolean killed;
    private final Semaphore semaphore = new Semaphore(0);
    private boolean paused;

    public Animation(int FPS) {
        this.FPS = FPS;
    }

    public void run() {
        long currentTime = System.nanoTime();
        long sleepTime;
        while(!killed) {
            if (paused) {
                semaphore.forceLock();
            }
            if (System.nanoTime() - currentTime >= (double) 1000000 / FPS ) {
                currentTime = System.nanoTime();
                ViewPort.getInstance().update();
                sleepTime =  (long) ((1000 / ((double) FPS)) -  (System.nanoTime() - currentTime) / 1000000);
                try {
                    sleep(Math.max(sleepTime, 0));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void kill() {
        killed = true;
    }
    public void restart() {
        paused = false;
        semaphore.forceRelease();
    }
    public void pause() {
        paused = true;
    }
}

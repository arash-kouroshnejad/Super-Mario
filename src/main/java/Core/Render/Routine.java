package Core.Render;

import Core.Util.Semaphore;

public class Routine extends Thread{
    private final int FPS;
    private boolean killed;
    private final Semaphore semaphore = new Semaphore(0);
    private boolean paused;
    private final Runnable task;

    public Routine(int FPS, Runnable task) {
        this.FPS = FPS;
        this.task = task;
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
                task.run();
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

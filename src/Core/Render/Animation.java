package Core.Render;

public class Animation extends Thread{
    private final int FPS;
    private boolean killed;

    public Animation(int FPS) {
        this.FPS = FPS;
    }

    public void run() {
        long currentTime = System.nanoTime();
        long sleepTime;
        while(!killed) {
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

    public void start() {
        killed = false;
        super.start();
    }

    public void pause() {
        killed = true;
    }
}

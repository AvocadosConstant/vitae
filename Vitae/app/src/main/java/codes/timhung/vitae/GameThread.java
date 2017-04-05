package codes.timhung.vitae;

import android.util.Log;

public class GameThread extends Thread {
    private Game game;
    private volatile boolean running = true;

    // Frames per second
    private final long FRAME_RATE = 1;

    // Seconds per frame
    private final long FRAME_DURATION = 1000 / FRAME_RATE;

    public GameThread(Game game) {
        this.game = game;
    }

    @Override
    public void run() {
        long nextGameTick = System.currentTimeMillis();
        long sleepTime;

        // Game loop
        while (running) {
            Log.d("THREAD", "New frame at time " + nextGameTick);
            game.update();
            game.draw();

            nextGameTick += FRAME_DURATION;
            sleepTime = nextGameTick - System.currentTimeMillis();
            if(sleepTime >= 0) {
                try {
                    Thread.sleep(sleepTime);
                }
                catch (InterruptedException e) {
                    throw new RuntimeException("Sleep interrupted", e);
                }
            }
        }
    }

    public void shutdown() {
        running = false;
    }
}

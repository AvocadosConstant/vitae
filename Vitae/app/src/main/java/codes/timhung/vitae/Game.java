package codes.timhung.vitae;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import java.util.HashSet;

public class Game {

    private enum GameState {
        START, PAUSED, RUNNING
    }

    private static final int CELL_DIMENSION = 50;

    private Context context;
    private SurfaceHolder holder;
    private Rect screen;
    private Resources resources;
    private GameState state = GameState.START;
    private BitmapFactory.Options options;

    private Paint cellPaint;
    private HashSet<Cell> cells;

    public Game(Context context, Rect screen, SurfaceHolder holder, Resources resources) {
        this.context = context;
        this.screen = screen;
        this.holder = holder;
        this.resources = resources;

        restartGame();
    }

    private void restartGame() {
        options = new BitmapFactory.Options();
        options.inScaled = false;

        cellPaint = new Paint();
        cellPaint.setColor(resources.getColor(R.color.colorCell));

        cells = new HashSet<>(1000, 0.5f);

        state = GameState.PAUSED;
    }

    public void pause() {
        Log.d("PAUSE", "Pausing game");
        if(state == GameState.RUNNING) state = GameState.PAUSED;
        else if(state == GameState.PAUSED) state = GameState.RUNNING;
    }

    public void onTouchEvent(MotionEvent event) {
        if (state == GameState.RUNNING) {
            // TODO
        } else if(state == GameState.START) {
            state = GameState.RUNNING;
        } else if(state == GameState.PAUSED) {
            // TODO
            cells.add(new Cell((int)(event.getX() / CELL_DIMENSION), (int)(event.getY() / CELL_DIMENSION)));
        }
    }

    /**
     * Game logic is checked here! Hitboxes, movement, etc.
     */
    public void update() {
        if(state == GameState.RUNNING){
            // Do stuff

            // Randomly add cells for testing purposes
            cells.add(new Cell((int)(Math.random() * 30), (int)(Math.random() * 50)));
        }
    }

    /**
     * Decides whether to draw
     */
    public void draw() {
        //Log.d("GAME_DRAW", "Locking canvas");
        Canvas canvas = holder.lockCanvas();
        if (canvas != null) {
            if(state == GameState.RUNNING) canvas.drawColor(resources.getColor(R.color.colorGameBG));
            else canvas.drawColor(Color.RED);
            switch (state) {
                case RUNNING:
                    drawGame(canvas);
                    break;
                case PAUSED:
                    drawGame(canvas);
                    break;
                case START:
                    drawGame(canvas);
                    break;
            }
            holder.unlockCanvasAndPost(canvas);
        }
    }

    /**
     * Draws game resources
     * @param canvas Canvas to be drawn on
     */
    private void drawGame(Canvas canvas) {
        //Log.d("GAME_DRAWGAME", "Trying to draw everything in the game!");

        // Draw all cells
        for(Cell cell : cells) {
            canvas.drawRect(new Rect(
                    cell.getX() * CELL_DIMENSION,
                    cell.getY() * CELL_DIMENSION,
                    cell.getX() * CELL_DIMENSION + CELL_DIMENSION,
                    cell.getY() * CELL_DIMENSION + CELL_DIMENSION
            ), cellPaint);
        }
    }
}

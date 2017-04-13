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
    private static final int GRID_LINES_RATIO = 20;
    private static int GRID_LINES_WIDTH = 0;

    private static final int LIFE_MIN_THRESH = 2;
    private static final int LIFE_MAX_THRESH = 3;
    private static final int LIFE_SPAWN_THRESH = 3;

    private Context context;
    private SurfaceHolder holder;
    private Rect screen;
    private Resources resources;
    private GameState state = GameState.START;
    private BitmapFactory.Options options;

    private Paint cellPaint;
    private Paint drawnCellPaint;
    private HashSet<Cell> cells;
    private HashSet<Cell> drawnCells;

    public Game(Context context, Rect screen, SurfaceHolder holder, Resources resources) {
        this.context = context;
        this.screen = screen;
        this.holder = holder;
        this.resources = resources;

        restartGame();
    }

    public void restartGame() {
        options = new BitmapFactory.Options();
        options.inScaled = false;

        cellPaint = new Paint();
        cellPaint.setColor(resources.getColor(R.color.colorCell));

        drawnCellPaint = new Paint();
        drawnCellPaint.setColor(resources.getColor(R.color.colorDrawnCell));

        cells = new HashSet<>(1000, 0.5f);
        drawnCells = new HashSet<>(200, 0.5f);

        state = GameState.PAUSED;
    }

    // Returns true if paused
    public boolean togglePauseResume() {
        if(state == GameState.RUNNING) state = GameState.PAUSED;
        else if(state == GameState.PAUSED) state = GameState.RUNNING;
        return state == GameState.PAUSED;
    }

    public void pause() {
        state = GameState.PAUSED;
    }

    public boolean isPaused() { return state == GameState.PAUSED; }

    /**
     * Toggle cell grid on or off
     * @return true if the grid is enabled
     */
    public boolean toggleGrid() {
        if(GRID_LINES_WIDTH > 0) {
            GRID_LINES_WIDTH = 0;
            return false;
        }
        GRID_LINES_WIDTH = CELL_DIMENSION / GRID_LINES_RATIO;
        return true;
    }

    public void onTouchEvent(MotionEvent event) {
        if (state == GameState.RUNNING) {
            // TODO
        } else if(state == GameState.PAUSED) {
            // TODO
        } else if(state == GameState.START) {
            state = GameState.RUNNING;
        }

        // Handle hand drawn cells
        synchronized (drawnCells) {
            drawnCells.add(new Cell((int) (event.getX() / CELL_DIMENSION), (int) (event.getY() / CELL_DIMENSION)));
        }
    }

    /**
     * Game logic is checked here! Hitboxes, movement, etc.
     */
    public void update() {
        if(state == GameState.RUNNING){
            synchronized(drawnCells) {
                cells.addAll(drawnCells);
                drawnCells.clear();
            }

            // Handle Life logic
            HashSet<Cell> spawnCandidates = new HashSet<>(1000, .5f);
            HashSet<Cell> nextCells = new HashSet<>(1000, .5f);
            int neighborCount;
            int cellsStayedAlive = 0;

            for(Cell liveCell : cells) {
                // Count neighbors
                neighborCount = liveCell.countNeighbors(cells);
                spawnCandidates.addAll(liveCell.getDeadNeighbors(cells));

                // Satisfiles remaining alive rule
                if(LIFE_MIN_THRESH <= neighborCount && neighborCount <= LIFE_MAX_THRESH) {
                    nextCells.add(liveCell);
                    cellsStayedAlive++;
                }
            }

            for(Cell candidate : spawnCandidates) {
                neighborCount = candidate.countNeighbors(cells);
                if(neighborCount == LIFE_SPAWN_THRESH) nextCells.add(candidate);
            }

            Log.d("UPDATE", "Update report: Originally " + cells.size() + " cells. " + cellsStayedAlive
                    + " remained alive and " + (cells.size() - cellsStayedAlive) + " died.");
            cells = nextCells;
        }
    }

    /**
     * Decides what to draw
     */
    public void draw() {
        //Log.d("GAME_DRAW", "Locking canvas");
        Canvas canvas = holder.lockCanvas();
        if (canvas != null) {
            switch (state) {
                case RUNNING:
                    canvas.drawColor(resources.getColor(R.color.colorGameBG));
                    break;
                case PAUSED:
                    canvas.drawColor(Color.BLACK);
                    break;
                case START:
                    break;
            }
            drawGame(canvas);
            holder.unlockCanvasAndPost(canvas);
        }
    }

    /**
     * Draws game resources
     * @param canvas Canvas to be drawn on
     */
    private void drawGame(Canvas canvas) {
        // Draw hand drawn cells
        synchronized(drawnCells) {
            for (Cell cell : drawnCells) {
                cell.draw(canvas, CELL_DIMENSION, GRID_LINES_WIDTH, drawnCellPaint);
            }
        }

        // Draw processed cells
        for(Cell cell : cells) {
            cell.draw(canvas, CELL_DIMENSION, GRID_LINES_WIDTH, cellPaint);
        }
    }
}
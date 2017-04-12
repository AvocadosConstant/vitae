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
    private static final int GRID_LINES_WIDTH = CELL_DIMENSION / 10;
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

    private void restartGame() {
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

    public void togglePauseResume() {
        if(state == GameState.RUNNING) state = GameState.PAUSED;
        else if(state == GameState.PAUSED) state = GameState.RUNNING;
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
     * Decides whether to draw
     */
    public void draw() {
        //Log.d("GAME_DRAW", "Locking canvas");
        Canvas canvas = holder.lockCanvas();
        if (canvas != null) {
            if(state == GameState.RUNNING) canvas.drawColor(resources.getColor(R.color.colorGameBG));
            else canvas.drawColor(Color.BLACK);
            drawGame(canvas);
            /*
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
            */
            holder.unlockCanvasAndPost(canvas);
        }
    }

    /**
     * Draws game resources
     * @param canvas Canvas to be drawn on
     */
    private void drawGame(Canvas canvas) {
        //Log.d("GAME_DRAWGAME", "Trying to draw everything in the game!");

        synchronized(drawnCells) {
            // Draw all cells
            for (Cell cell : drawnCells) {
                canvas.drawRect(new Rect(
                        cell.getX() * CELL_DIMENSION,
                        cell.getY() * CELL_DIMENSION,
                        cell.getX() * CELL_DIMENSION + CELL_DIMENSION - GRID_LINES_WIDTH,
                        cell.getY() * CELL_DIMENSION + CELL_DIMENSION - GRID_LINES_WIDTH
                ), drawnCellPaint);
            }
        }

        for(Cell cell : cells) {
            canvas.drawRect(new Rect(
                    cell.getX() * CELL_DIMENSION,
                    cell.getY() * CELL_DIMENSION,
                    cell.getX() * CELL_DIMENSION + CELL_DIMENSION - GRID_LINES_WIDTH,
                    cell.getY() * CELL_DIMENSION + CELL_DIMENSION - GRID_LINES_WIDTH
            ), cellPaint);
        }
    }
}

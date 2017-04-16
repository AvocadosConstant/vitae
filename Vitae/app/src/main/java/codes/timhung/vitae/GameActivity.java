package codes.timhung.vitae;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class GameActivity extends ActionBarActivity {

    private Toolbar toolbar;
    private GameView gameView;

    private MenuItem pauseMenuItem;
    private MenuItem gridMenuItem;
    private MenuItem zoomMenuItem;
    private boolean isPaused;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gameView = (GameView) findViewById(R.id.gameView);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_toolbar);
        toolbar.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        isPaused = true;
    }

    public void handleToolbarAction(int itemId) {
        Log.d("handleToolbarAction", "Touched item " + itemId);
        switch(itemId) {
            case R.id.action_pause_resume:
                // Toggle pause or resume
                gameView.game.togglePauseResume();

                // Change resume/pause button icon. If paused, show resume, vice versa.
                // TODO: Move to onPrepareOptionsMenu?
                if(gameView.game.isPaused()) setPauseResumeIcon(R.drawable.ic_action_resume);
                else setPauseResumeIcon(R.drawable.ic_action_pause);

                break;
            case R.id.action_toggle_grid:
                if(gameView.game.toggleGrid()) gridMenuItem.setIcon(R.drawable.ic_action_grid_off);
                else gridMenuItem.setIcon(R.drawable.ic_action_grid_on);
                break;
            case R.id.action_restart:
                gameView.game.restartGame();
                setPauseResumeIcon(R.drawable.ic_action_resume);
                break;
            case R.id.action_toggle_zoom:
                if(gameView.game.toggleZoom()) zoomMenuItem.setIcon(R.drawable.ic_action_zoom_out);
                else zoomMenuItem.setIcon(R.drawable.ic_action_zoom_in);
                break;
            case R.id.action_draw:
                gameView.game.setToolDraw();
                break;
            case R.id.action_pan:
                gameView.game.setToolPan();
                break;
        }
    }

    public void setPauseResumeIcon(int id) {
        if(pauseMenuItem != null) pauseMenuItem.setIcon(id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        pauseMenuItem = menu.findItem(R.id.action_pause_resume);
        gridMenuItem = menu.findItem(R.id.action_toggle_grid);
        zoomMenuItem = menu.findItem(R.id.action_toggle_zoom);

        //pauseMenuItem.setIcon(R.drawable.ic_action_resume);
        isPaused = true;

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                handleToolbarAction(item.getItemId());
                return false;
            }
        });
        return true;
    }
}

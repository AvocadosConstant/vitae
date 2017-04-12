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
                isPaused = !isPaused;

                // Change resume/pause button icon. If paused, show resume, vice versa.
                // TODO: Move to onPrepareOptionsMenu?
                if(isPaused) pauseMenuItem.setIcon(R.drawable.ic_action_resume);
                else pauseMenuItem.setIcon(R.drawable.ic_action_pause);

                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        pauseMenuItem = menu.findItem(R.id.action_pause_resume);
        pauseMenuItem.setIcon(R.drawable.ic_action_resume);
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

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
    }

    public void handleToolbarAction(int itemId) {
        Log.d("handleToolbarAction", "Touched item " + itemId);
        switch(itemId) {
            case R.id.action_pause:
                gameView.game.pause();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
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

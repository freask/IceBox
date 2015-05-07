package ru.freask.studyjam.icebox;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.octo.android.robospice.SpiceManager;

import ru.freask.studyjam.icebox.http.ClientService;

/**
 * Created by Alexander.Kashin01 on 30.04.2015.
 */
public class BaseActivity extends ActionBarActivity implements NavigationDrawerCallbacks {

    SpiceManager spcMngr = new SpiceManager(ClientService.class);
    private NavigationDrawerFragment mNavigationDrawerFragment;
    Context context;
    private Toolbar mToolbar;
    private int activityLayoutRes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_base);
        FrameLayout mFrame = (FrameLayout) findViewById(R.id.container);
        getLayoutInflater().inflate(activityLayoutRes, mFrame, true);
        navigationDrawerSetUp();
    }

    @Override
    protected void onStart() {
        super.onStart();
        spcMngr.start(this);
    }

    @Override
    protected void onStop() {
        spcMngr.shouldStop();
        super.onStop();
    }

    public SpiceManager getSpiceManager() {
        return this.spcMngr;
    }

    public void setActivityLayoutRes(int activityLayoutRes) {
        this.activityLayoutRes = activityLayoutRes;
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    public void navigationDrawerSetUp() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.fragment_drawer);

        // Set up the drawer.
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.container);
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar, frameLayout);
        // populate the navigation drawer
        mNavigationDrawerFragment.setUserData(getResources().getString(R.string.app_name), "", BitmapFactory.decodeResource(getResources(), R.drawable.avatar));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments

        if (position == 0 || position == 1) {
            Class cl;
            switch (position) {
                case 0: cl = MainActivity.class; break;
                case 1: cl = NoticeActivity.class; break;
                default: return;
            }

            Intent intent = new Intent(context, cl);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }
    }


    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen())
            mNavigationDrawerFragment.closeDrawer();
        else
            super.onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
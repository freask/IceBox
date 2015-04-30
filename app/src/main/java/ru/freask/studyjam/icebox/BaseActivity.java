package ru.freask.studyjam.icebox;

import android.app.Activity;

import com.octo.android.robospice.SpiceManager;

import ru.freask.studyjam.icebox.http.ClientService;

/**
 * Created by Alexander.Kashin01 on 30.04.2015.
 */
public class BaseActivity extends Activity {

    SpiceManager spcMngr = new SpiceManager(ClientService.class);

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
}
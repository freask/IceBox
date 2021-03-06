package ru.freask.studyjam.icebox.http;

import android.util.Log;
import com.octo.android.robospice.retrofit.RetrofitGsonSpiceService;

import roboguice.util.temp.Ln;

/**
 * Created by Alexander.Kashin01 on 30.04.2015.
 */
public class ClientService extends RetrofitGsonSpiceService {
    public static final Boolean DEBUG = false;
    public static final String API_URL = "https://api.edamam.com";
    public static final String API_URL_DEBUG = "http://appiwish.com";
    public static final String API_APP_ID = "72a4f9b7";
    public static final String API_APP_KEY = "ea9bfbb253b89e3b8fee9dcf6189351c";

    @Override
    protected String getServerUrl() {
        return DEBUG ? API_URL_DEBUG : API_URL;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Ln.getConfig().setLoggingLevel(Log.ERROR);
        addRetrofitInterface(RecipeService.class);
    }
}

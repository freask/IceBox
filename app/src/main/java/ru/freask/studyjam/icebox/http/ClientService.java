package ru.freask.studyjam.icebox.http;

import android.app.Application;
import android.util.Log;

import com.octo.android.robospice.persistence.CacheManager;
import com.octo.android.robospice.persistence.exception.CacheCreationException;
import com.octo.android.robospice.persistence.retrofit.GsonRetrofitObjectPersisterFactory;
import com.octo.android.robospice.retrofit.RetrofitGsonSpiceService;

import roboguice.util.temp.Ln;

/**
 * Created by Alexander.Kashin01 on 30.04.2015.
 */
public class ClientService extends RetrofitGsonSpiceService {
    //public static final String API_URL = "https://api.edamam.com";
    public static final String API_URL = "http://appiwish.com";
    public static final String API_APP_ID = "72a4f9b7";
    public static final String API_APP_KEY = "ea9bfbb253b89e3b8fee9dcf6189351c";
    private static final String DB_NAME = "icebox.db";
    private static final Integer DB_VERSION = 1;

    @Override
    protected String getServerUrl() {
        return API_URL;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Ln.getConfig().setLoggingLevel(Log.VERBOSE);
        addRetrofitInterface(RecipeService.class);
    }
}

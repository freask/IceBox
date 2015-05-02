package ru.freask.studyjam.icebox.db;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;

public class HelperFactory{

    private static OrmHelper ormHelper;

    public static OrmHelper getHelper(){
        return ormHelper;
    }
    public static void setHelper(Context context){
        ormHelper = OpenHelperManager.getHelper(context, OrmHelper.class);
    }
    public static void releaseHelper(){
        OpenHelperManager.releaseHelper();
        ormHelper = null;
    }
}
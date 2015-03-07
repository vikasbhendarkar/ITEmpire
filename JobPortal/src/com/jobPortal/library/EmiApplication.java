package com.jobPortal.library;

import java.io.File;

import android.app.Application;
import android.content.pm.PackageManager;
import android.graphics.Typeface;

public class EmiApplication extends Application
{

    public static final String MONTH_NAME[] = {
        "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", 
        "Nov", "Dec"
    };
    public static Typeface arialFont;
    public static File config;
    public static DatabaseHandler dbHandler;
    public static File dispHtmlFile;

    public EmiApplication()
    {
    }

    public void onCreate()
    {
        super.onCreate();
        PackageManager packagemanager = getPackageManager();
        String s = getPackageName();
        try
        {
            @SuppressWarnings("unused")
			String _tmp = packagemanager.getPackageInfo(s, 0).applicationInfo.dataDir;
        }
        catch (android.content.pm.PackageManager.NameNotFoundException namenotfoundexception) { }
        dbHandler = new DatabaseHandler(getBaseContext());
        arialFont = Typeface.createFromAsset(getAssets(), "fonts/arial.ttf");
    }

}

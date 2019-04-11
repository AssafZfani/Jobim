package zfani.assaf.jobim;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class App extends Application {

    public static SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit().putString("PhoneNumber", "0509907979").apply();
    }
}
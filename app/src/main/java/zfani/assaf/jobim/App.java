package zfani.assaf.jobim;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

import androidx.annotation.NonNull;
import zfani.assaf.jobim.models.JobType;

@SuppressWarnings("unused")

public class App extends Application {

    public static SharedPreferences sharedPreferences;

    public static String loadJSONFromAsset(InputStream is) {
        String json = null;
        try {
            int size = is.available();
            byte[] buffer = new byte[size];
            int num = is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return json;
    }

    public static void loadJobsTypes(final Activity activity) {
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("jobs_types");
        ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChildren()) {
                    InputStream is = null;
                    try {
                        is = activity.getAssets().open("jobs_types.json");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        JSONObject obj = new JSONObject(loadJSONFromAsset(is));
                        JSONArray jsonArray = obj.getJSONArray("jobs_types");
                        for (int i = 0; i <= jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            DatabaseReference child = ref.child(i + "");
                            JSONArray colorArray = object.getJSONArray("Color");
                            ArrayList<Integer> color = new ArrayList<>(Arrays.asList(colorArray.getInt(0), colorArray.getInt(1), colorArray.getInt(2)));
                            child.setValue(new JobType(color, i + 1, object.getString("JobType")
                            ));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit().putString("PhoneNumber", "0509907979").apply();
        try {
            Typeface typeface = Typeface.createFromAsset(getAssets(), "open_sans.ttf");
            Field staticField = Typeface.class.getDeclaredField("MONOSPACE");
            staticField.setAccessible(true);
            staticField.set(null, typeface);
            typeface = Typeface.createFromAsset(getAssets(), "open_sans_bold.ttf");
            staticField = Typeface.class.getDeclaredField("SERIF");
            staticField.setAccessible(true);
            staticField.set(null, typeface);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
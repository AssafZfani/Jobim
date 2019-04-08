package zfani.assaf.jobim.viewmodels;

import android.app.Application;

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

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import zfani.assaf.jobim.App;
import zfani.assaf.jobim.models.Job;
import zfani.assaf.jobim.utils.GPSTracker;

public class AllJobsViewModel extends AndroidViewModel {

    public AllJobsViewModel(@NonNull Application application) {
        super(application);
    }

    public void loadJobs() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("jobs");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChildren()) {
                    InputStream inputStream = null;
                    try {
                        inputStream = getApplication().getAssets().open("jobs.json");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (inputStream == null) {
                            return;
                        }
                        JSONObject obj = new JSONObject(App.loadJSONFromAsset(inputStream));
                        JSONArray jsonArray = obj.getJSONArray("jobs");
                        for (int i = 0; i <= jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String address = object.getString("Address");
                            DatabaseReference job = ref.push();
                            job.setValue(new Job(
                                    address,
                                    object.getBoolean("Applied"),
                                    object.getInt("BusinessNumber"),
                                    GPSTracker.getDistanceFromAddress(getApplication(), address),
                                    object.getBoolean("Favorite"),
                                    object.getString("Firm"),
                                    job.getKey(),
                                    false,
                                    object.getString("Title")
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
}

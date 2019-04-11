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
import zfani.assaf.jobim.models.JobType;
import zfani.assaf.jobim.utils.JsonHelper;

public class JobTypesViewModel extends AndroidViewModel {

    public JobTypesViewModel(@NonNull Application application) {
        super(application);
    }

    public void loadJobsTypes() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("jobs_types");
        ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChildren()) {
                    InputStream inputStream = null;
                    try {
                        inputStream = getApplication().getAssets().open("jobs_types.json");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (inputStream == null) {
                        return;
                    }
                    try {
                        JSONObject obj = new JSONObject(JsonHelper.loadJSONFromAsset(inputStream));
                        JSONArray jsonArray = obj.getJSONArray("jobs_types");
                        for (int i = 0; i <= jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            DatabaseReference child = ref.child(i + "");
                            child.setValue(new JobType(i + 1, object.getString("JobType")));
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

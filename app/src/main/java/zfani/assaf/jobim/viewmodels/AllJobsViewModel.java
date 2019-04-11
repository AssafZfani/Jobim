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
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import zfani.assaf.jobim.App;
import zfani.assaf.jobim.models.Job;
import zfani.assaf.jobim.models.JobType;
import zfani.assaf.jobim.utils.GPSTracker;

public class AllJobsViewModel extends AndroidViewModel {

    private MutableLiveData<List<JobType>> jobTypeLiveList;

    public AllJobsViewModel(@NonNull Application application) {
        super(application);
        jobTypeLiveList = new MutableLiveData<>();
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
                            String address = object.getString("address");
                            DatabaseReference job = ref.push();
                            JSONArray colorArray = object.getJSONArray("color");
                            job.setValue(new Job(
                                    address,
                                    object.getBoolean("applied"),
                                    object.getInt("business_number"),
                                    colorArray.getInt(0), colorArray.getInt(1), colorArray.getInt(2),
                                    GPSTracker.getDistanceFromAddress(getApplication(), address),
                                    object.getBoolean("is_favorite"),
                                    object.getString("firm"),
                                    job.getKey(),
                                    false,
                                    object.getString("title"),
                                    object.getString("type")
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

    public void loadJobsTypes() {
        FirebaseDatabase.getInstance().getReference().child("jobs_types").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<JobType> jobTypeList = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    jobTypeList.add(data.getValue(JobType.class));
                }
                jobTypeLiveList.setValue(jobTypeList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public MutableLiveData<List<JobType>> getJobTypeLiveList() {
        return jobTypeLiveList;
    }
}

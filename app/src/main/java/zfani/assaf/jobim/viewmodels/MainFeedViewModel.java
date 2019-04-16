package zfani.assaf.jobim.viewmodels;

import android.app.Application;

import com.google.firebase.database.ChildEventListener;
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
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import zfani.assaf.jobim.models.FilterItem;
import zfani.assaf.jobim.models.Job;
import zfani.assaf.jobim.repositories.JobRepository;
import zfani.assaf.jobim.utils.GPSTracker;
import zfani.assaf.jobim.utils.JsonHelper;

public class MainFeedViewModel extends AndroidViewModel {

    private MutableLiveData<FilterItem> filterItem;
    private MutableLiveData<Boolean> shouldCheckPermission;
    private JobRepository jobRepository;

    public MainFeedViewModel(@NonNull Application application) {
        super(application);
        filterItem = new MutableLiveData<>(null);
        shouldCheckPermission = new MutableLiveData<>(true);
        jobRepository = new JobRepository(application);
    }

    public MutableLiveData<FilterItem> getFilterItem() {
        return filterItem;
    }

    public void setFilterItem(FilterItem filterItem) {
        this.filterItem.setValue(filterItem);
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
                        JSONObject obj = new JSONObject(JsonHelper.loadJSONFromAsset(inputStream));
                        JSONArray jsonArray = obj.getJSONArray("jobs");
                        for (int i = 0; i <= jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String address = object.getString("address");
                            JSONArray colorArray = object.getJSONArray("color");
                            ref.push().setValue(new Job(
                                    address,
                                    object.getBoolean("applied"),
                                    object.getInt("business_number"),
                                    colorArray.getInt(0), colorArray.getInt(1), colorArray.getInt(2),
                                    GPSTracker.getDistanceFromAddress(getApplication(), address),
                                    object.getBoolean("is_favorite"),
                                    object.getString("firm"),
                                    i + 1,
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
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                jobRepository.insert(dataSnapshot.getValue(Job.class));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                jobRepository.delete(dataSnapshot.getValue(Job.class));
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public MutableLiveData<Boolean> getShouldCheckPermission() {
        return shouldCheckPermission;
    }

    public void setShouldCheckPermission(boolean shouldCheckPermission) {
        this.shouldCheckPermission.setValue(shouldCheckPermission);
    }

    public LiveData<List<Job>> getJobLiveList() {
        return jobRepository.getAllJobs();
    }

    public List<Job> getJobLiveList(List<String> jobTypeList, String jobLocation, String jobFirm) {
        if ((jobTypeList != null && !jobTypeList.isEmpty()) || (jobLocation != null && !jobLocation.isEmpty()) || (jobFirm != null && !jobFirm.isEmpty())) {
            setFilterItem(new FilterItem(jobTypeList != null && jobTypeList.size() == 1 ? jobRepository.getColorByJobType(jobTypeList.get(0)) : 0,
                    "מציג " + (jobTypeList == null || jobTypeList.isEmpty() ? "" : (jobTypeList.size() == 1 ? jobTypeList.get(0) : jobTypeList.size() + " ג'ובים")) +
                            (jobFirm != null && !jobFirm.isEmpty() ? " ב" + jobFirm : "") + (jobLocation != null && !jobLocation.isEmpty() ? " ב" + jobLocation : "")));
        }
        return jobRepository.getAllJobs(jobTypeList, jobLocation, jobFirm);
    }
}

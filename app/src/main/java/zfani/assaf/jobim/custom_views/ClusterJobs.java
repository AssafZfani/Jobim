package zfani.assaf.jobim.custom_views;

import android.app.Activity;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import zfani.assaf.jobim.models.Job;
import zfani.assaf.jobim.utils.GPSTracker;

public class ClusterJobs implements ClusterItem {

    Job job;
    private Activity activity;

    public ClusterJobs(Activity activity, Job job) {
        this.activity = activity;
        this.job = job;
    }

    @Override
    public LatLng getPosition() {
        return GPSTracker.getLatLngFromAddress(activity.getApplication(), job.getAddress());
    }

    @Override
    public String getTitle() {
        return "";
    }

    @Override
    public String getSnippet() {
        return "";
    }
}
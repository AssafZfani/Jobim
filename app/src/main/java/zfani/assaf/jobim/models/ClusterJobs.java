package zfani.assaf.jobim.models;

import android.app.Activity;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

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

        return GPSTracker.getLatLngFromAddress(activity, job.getAddress());
    }
}
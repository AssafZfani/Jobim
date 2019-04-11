package zfani.assaf.jobim.views.fragments.FeedFragments;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import zfani.assaf.jobim.R;
import zfani.assaf.jobim.models.ClusterJobs;
import zfani.assaf.jobim.models.ClusterJobsRenderer;
import zfani.assaf.jobim.models.Job;
import zfani.assaf.jobim.utils.GPSTracker;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private static GoogleMap googleMap;

    public static MapFragment newInstance(LatLng latLng) {
        MapFragment mapFragment = new MapFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("LatLng", latLng);
        mapFragment.setArguments(bundle);
        return mapFragment;
    }

    public static void changeCamera(LatLng latLng) {
        googleMap.clear();
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
        googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromAsset("location_marker.png")).position(latLng));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map, container, false);
        ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        LatLng latLng = getArguments().getParcelable("LatLng");
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
        Activity activity = getActivity();
        String activityName = activity.getLocalClassName();
        if (activityName.equalsIgnoreCase("views.activities.MainActivity")) {
            map.setMyLocationEnabled(true);
            map.setOnMyLocationButtonClickListener(() -> {
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
                return false;
            });
            googleMap.setOnMapLoadedCallback(() -> loadMarkersToMap(activity));
        } else if (activityName.equalsIgnoreCase("views.activities.ShowByActivity")) {
            EditText editText = activity.findViewById(R.id.etJobLocation);
            googleMap.setOnMapClickListener(latLng1 -> {
                editText.setText(GPSTracker.getAddressFromLatLng(activity, latLng1, null));
                changeCamera(latLng1);
            });
        }
    }

    private void loadMarkersToMap(Activity activity) {
        ClusterManager<ClusterJobs> clusterManager = new ClusterManager<>(activity, googleMap);
        HashMap<ClusterJobs, Job> hashMap = new HashMap<>();
        ClusterJobsRenderer renderer = new ClusterJobsRenderer(activity, googleMap, clusterManager, hashMap);
        clusterManager.setOnClusterItemClickListener(renderer);
        clusterManager.setOnClusterClickListener(renderer);
        /*for (Job job : FilteredAdapter.filteredList == null ? JobsAdapter.jobsList : FilteredAdapter.filteredList) {
            hashMap.put(new ClusterJobs(activity, job), job);
        }*/
        clusterManager.addItems(hashMap.keySet());
        clusterManager.setRenderer(renderer);
        googleMap.setOnMarkerClickListener(clusterManager);
    }
}
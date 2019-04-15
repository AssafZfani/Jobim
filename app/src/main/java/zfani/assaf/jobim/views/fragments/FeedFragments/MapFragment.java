package zfani.assaf.jobim.views.fragments.FeedFragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

import java.util.HashMap;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import zfani.assaf.jobim.R;
import zfani.assaf.jobim.custom_views.ClusterJobs;
import zfani.assaf.jobim.custom_views.ClusterJobsRenderer;
import zfani.assaf.jobim.models.Job;
import zfani.assaf.jobim.utils.GPSTracker;
import zfani.assaf.jobim.viewmodels.ShowByBottomSheetViewModel;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap googleMap;

    public static MapFragment newInstance(int screenType, LatLng latLng) {
        MapFragment mapFragment = new MapFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("Screen_Type", screenType);
        bundle.putParcelable("LatLng", latLng);
        mapFragment.setArguments(bundle);
        return mapFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map, container, false);
        ((SupportMapFragment) Objects.requireNonNull(getChildFragmentManager().findFragmentById(R.id.map))).getMapAsync(this);
        return view;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        Bundle bundle = requireArguments();
        LatLng latLng = bundle.getParcelable("LatLng");
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
        switch (bundle.getInt("Screen_Type")) {
            case 1:
                map.setMyLocationEnabled(true);
                map.setOnMyLocationButtonClickListener(() -> {
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
                    return false;
                });
                googleMap.setOnMapLoadedCallback(this::loadMarkersToMap);
                break;
            case 2:
                AppCompatActivity activity = (AppCompatActivity) requireActivity();
                ShowByBottomSheetViewModel showByBottomSheetViewModel = ViewModelProviders.of(activity).get(ShowByBottomSheetViewModel.class);
                showByBottomSheetViewModel.getJobLocationQuery().observe(this, s -> changeCamera(GPSTracker.getLatLngFromAddress(activity.getApplication(), s)));
                googleMap.setOnMapClickListener(newLocation -> {
                    changeCamera(newLocation);
                    showByBottomSheetViewModel.setChosenLocation(GPSTracker.getAddressFromLatLng(activity, newLocation));
                });
                break;
        }
    }

    private void changeCamera(LatLng latLng) {
        if (latLng != null) {
            googleMap.clear();
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
            googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromAsset("location_marker.png")).position(latLng));
        }
    }

    private void loadMarkersToMap() {
        Activity activity = requireActivity();
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
package zfani.assaf.jobim.Fragments.FeedFragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

import java.util.HashMap;

import zfani.assaf.jobim.Models.ClusterJobs;
import zfani.assaf.jobim.Models.ClusterJobsRenderer;
import zfani.assaf.jobim.Models.Job;
import zfani.assaf.jobim.R;
import zfani.assaf.jobim.Utils.Adapter;
import zfani.assaf.jobim.Utils.FilteredAdapter;
import zfani.assaf.jobim.Utils.GPSTracker;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    static GoogleMap googleMap;

    public static MapFragment newInstance(LatLng latLng) {

        MapFragment mapFragment = new MapFragment();

        Bundle bundle = new Bundle();

        bundle.putParcelable("LatLng", latLng);

        mapFragment.setArguments(bundle);

        return mapFragment;
    }

    static void changeCamera(LatLng latLng) {

        googleMap.clear();

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));

        googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromAsset("location_marker.png")).position(latLng));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.map, container, false);

        ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(final GoogleMap map) {

        googleMap = map;

        final LatLng latLng = getArguments().getParcelable("LatLng");

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));

        final Activity activity = getActivity();

        String activityName = activity.getLocalClassName();

        if (activityName.equalsIgnoreCase("Activities.HomePage")) {

            map.setMyLocationEnabled(true);

            map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {

                @Override
                public boolean onMyLocationButtonClick() {

                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));

                    return false;
                }
            });

            googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {

                @Override
                public void onMapLoaded() {

                    loadMarkersToMap(activity);
                }
            });
        } else if (activityName.equalsIgnoreCase("Activities.ShowBy")) {

            final EditText editText = (EditText) activity.findViewById(R.id.editText2);

            googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                @Override
                public void onMapClick(LatLng latLng) {

                    editText.setText(GPSTracker.getAddressFromLatLng(activity, latLng, null));

                    changeCamera(latLng);
                }
            });

            editText.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                    activity.getIntent().putExtra("Address", s + "");

                    editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                            if (v.getText().length() != 0) {

                                try {

                                    activity.getIntent().putExtra("Address", v.getText() + "");

                                    MapFragment.changeCamera(GPSTracker.getLatLngFromAddress(activity, v.getText() + ""));

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            return false;
                        }
                    });
                }
            });
        }
    }

    private void loadMarkersToMap(Activity activity) {

        ClusterManager<ClusterJobs> clusterManager = new ClusterManager<>(activity, googleMap);

        HashMap<ClusterJobs, Job> hashMap = new HashMap<>();

        ClusterJobsRenderer renderer = new ClusterJobsRenderer(activity, googleMap, clusterManager, hashMap);

        clusterManager.setOnClusterItemClickListener(renderer);
        clusterManager.setOnClusterClickListener(renderer);

        for (Job job : FilteredAdapter.filteredList == null ? Adapter.jobsList : FilteredAdapter.filteredList)
            hashMap.put(new ClusterJobs(activity, job), job);

        clusterManager.addItems(hashMap.keySet());

        clusterManager.setRenderer(renderer);

        googleMap.setOnMarkerClickListener(clusterManager);
    }
}
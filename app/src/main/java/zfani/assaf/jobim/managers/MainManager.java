package zfani.assaf.jobim.managers;

import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import zfani.assaf.jobim.R;
import zfani.assaf.jobim.utils.GPSTracker;
import zfani.assaf.jobim.views.fragments.FeedFragments.MapFragment;
import zfani.assaf.jobim.views.fragments.FeedFragments.MainFeedFragment;

public class MainManager {

    private static final String MAP_FRAGMENT_TAG = "map_fragment_tag";
    private AppCompatActivity activity;
    private FragmentManager fragmentManager;

    public MainManager(AppCompatActivity activity) {
        this.activity = activity;
        fragmentManager = activity.getSupportFragmentManager();
    }

    public void pushMainFeedFragment() {
        fragmentManager.beginTransaction().replace(R.id.fragmentContainer, new MainFeedFragment()).commit();
    }

    public void pushOrRemoveMapFragment() {
        ImageView btnSwitchMode = activity.findViewById(R.id.btnSwitchMode);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentByTag(MAP_FRAGMENT_TAG);
        if (fragment == null) {
            btnSwitchMode.setImageResource(R.drawable.selector_btn_feed);
            fragmentTransaction.add(R.id.mapFragment, MapFragment.newInstance(1, GPSTracker.createLatLng(GPSTracker.location)), MAP_FRAGMENT_TAG).commit();
        } else {
            btnSwitchMode.setImageResource(R.drawable.selector_btn_map);
            fragmentTransaction.remove(fragment).commit();
        }
    }
}

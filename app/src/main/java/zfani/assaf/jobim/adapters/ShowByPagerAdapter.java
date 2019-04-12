package zfani.assaf.jobim.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import zfani.assaf.jobim.utils.GPSTracker;
import zfani.assaf.jobim.views.fragments.DetailsFragments.ListFragment;
import zfani.assaf.jobim.views.fragments.FeedFragments.MapFragment;
import zfani.assaf.jobim.views.fragments.NewJobFragments.JobTypeFragment;

public class ShowByPagerAdapter extends FragmentPagerAdapter {

    public ShowByPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 2:
                return JobTypeFragment.newInstance(true);
            case 1:
                return MapFragment.newInstance(GPSTracker.createLatLng(GPSTracker.location));
            default:
                return ListFragment.newInstance(true);
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}

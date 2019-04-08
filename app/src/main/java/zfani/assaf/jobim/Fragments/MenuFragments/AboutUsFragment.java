package zfani.assaf.jobim.Fragments.MenuFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import zfani.assaf.jobim.R;

public class AboutUsFragment extends Fragment {

    public static AboutUsFragment newInstance() {

        return new AboutUsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = new View(container.getContext());

        view.setBackgroundResource(R.drawable.about_us);

        return view;
    }
}

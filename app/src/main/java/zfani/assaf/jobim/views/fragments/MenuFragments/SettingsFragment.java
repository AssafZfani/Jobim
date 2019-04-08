package zfani.assaf.jobim.views.fragments.MenuFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import androidx.fragment.app.Fragment;
import zfani.assaf.jobim.Application;
import zfani.assaf.jobim.R;

public class SettingsFragment extends Fragment {

    public ToggleButton toggleButton;

    public static SettingsFragment newInstance() {

        return new SettingsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.settings, container, false);

        toggleButton = view.findViewById(R.id.toogleButton);

        toggleButton.setChecked(Application.sharedPreferences.getBoolean("EnableNotification", true));

        return view;
    }
}

package zfani.assaf.jobim.Fragments.MenuFragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import zfani.assaf.jobim.Activities.FillDetails;
import zfani.assaf.jobim.Application;
import zfani.assaf.jobim.Fragments.DetailsFragments.FullNameFragment;
import zfani.assaf.jobim.Models.RoundedImageView;
import zfani.assaf.jobim.R;

public class MyDetailsFragment extends Fragment {

    boolean needToFillDetails;
    private Activity activity;

    public static MyDetailsFragment newInstance() {

        return new MyDetailsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        activity = getActivity();

        return inflater.inflate(R.layout.my_details, container, false);
    }

    @Override
    public void onStart() {

        super.onStart();

        View view = getView();

        if (view != null) {

            ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);

            if (!Application.sharedPreferences.contains("FullName") && !needToFillDetails) {

                startActivity(new Intent(activity, FillDetails.class));

                needToFillDetails = true;

            } else {

                ((TextView) view.findViewById(R.id.fullName)).setText(Application.sharedPreferences.getString("FullName", ""));

                ((TextView) view.findViewById(R.id.city)).setText(Application.sharedPreferences.getString("City", ""));

                int birthYear = Application.sharedPreferences.getInt("BirthYear", 0);

                ((TextView) view.findViewById(R.id.birthYear)).setText(birthYear != 0 ? birthYear + "" : "");

                ((TextView) view.findViewById(R.id.email)).setText(Application.sharedPreferences.getString("Email", ""));

                activity.getIntent().putExtra("SmallRound", false);

                FullNameFragment.initSelfie((RoundedImageView) view.findViewById(R.id.selfie));
            }
        }
    }
}
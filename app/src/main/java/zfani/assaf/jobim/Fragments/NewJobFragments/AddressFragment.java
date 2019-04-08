package zfani.assaf.jobim.Fragments.NewJobFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import zfani.assaf.jobim.Activities.AddNewJob;
import zfani.assaf.jobim.Fragments.DetailsFragments.ListFragment;
import zfani.assaf.jobim.R;

public class AddressFragment extends Fragment {

    public static AddressFragment newInstance() {

        return new AddressFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.address_fragment, container, false);

        getChildFragmentManager().beginTransaction().add(R.id.listLayout, ListFragment.newInstance()).commit();

        return view;
    }

    public boolean isValidValue() {

        boolean result = AddNewJob.newJob.getAddress() != null;

        if (!result)
            Toast.makeText(getActivity(), "חובה לבחור מיקום", Toast.LENGTH_SHORT).show();

        return result;
    }
}
package zfani.assaf.jobim.views.fragments.NewJobFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import zfani.assaf.jobim.R;
import zfani.assaf.jobim.views.activities.AddNewJob;
import zfani.assaf.jobim.views.fragments.DetailsFragments.ListFragment;

public class AddressFragment extends Fragment {

    public static AddressFragment newInstance() {
        return new AddressFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.address_fragment, container, false);
        getChildFragmentManager().beginTransaction().add(R.id.listLayout, ListFragment.newInstance(false)).commit();
        return view;
    }

    public boolean isValidValue() {
        boolean result = AddNewJob.newJob.getAddress() != null;
        if (!result) {
            Toast.makeText(getActivity(), "חובה לבחור מיקום", Toast.LENGTH_SHORT).show();
        }
        return result;
    }
}
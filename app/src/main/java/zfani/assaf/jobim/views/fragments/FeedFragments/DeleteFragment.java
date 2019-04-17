package zfani.assaf.jobim.views.fragments.FeedFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import zfani.assaf.jobim.R;
import zfani.assaf.jobim.utils.AlertHelper;

public class DeleteFragment extends Fragment {

    public static DeleteFragment newInstance(int jobId) {
        DeleteFragment deleteFragment = new DeleteFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("JobId", jobId);
        deleteFragment.setArguments(bundle);
        return deleteFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delete_job, container, false);
        view.findViewById(R.id.btnDeleteJob).setOnClickListener(view1 -> {
            AlertHelper.displayDialog(getActivity(), R.layout.dialog_delete_job, requireArguments().getInt("JobId"));
        });
        return view;
    }
}

package zfani.assaf.jobim.views.fragments.NewJobFragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import zfani.assaf.jobim.R;
import zfani.assaf.jobim.views.activities.AddNewJob;
import zfani.assaf.jobim.views.activities.FilterQuestion;

public class PictureFragment extends Fragment {

    private Activity activity;
    private Button addFilterQuestion;

    public static PictureFragment newInstance() {

        return new PictureFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.picture_fragment, container, false);

        activity = getActivity();

        CheckBox checkBox = view.findViewById(R.id.checkBox);

        checkBox.setOnCheckedChangeListener((compoundButton, b) -> AddNewJob.newJob.setFitForTeens(b));

        addFilterQuestion = view.findViewById(R.id.addFilterQuestion);

        addFilterQuestion.setOnClickListener(view1 -> activity.startActivity(new Intent(activity, FilterQuestion.class)));

        return view;
    }

    @Override
    public void onResume() {

        super.onResume();

        String question = AddNewJob.newJob.getQuestion();

        boolean hasQuestionAdded = question != null;

        addFilterQuestion.setBackgroundResource(hasQuestionAdded ? R.drawable.button_empty : R.drawable.border);

        addFilterQuestion.setText(hasQuestionAdded ? question : "הוספת שאלת סינון למועמדים");

        addFilterQuestion.setTextColor(ContextCompat.getColor(activity, hasQuestionAdded ? R.color.orange : R.color.colorPrimaryDark));
    }
}

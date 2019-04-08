package zfani.assaf.jobim.Fragments.NewJobFragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import zfani.assaf.jobim.Activities.AddNewJob;
import zfani.assaf.jobim.Activities.FilterQuestion;
import zfani.assaf.jobim.R;

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

        CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                AddNewJob.newJob.setFitForTeens(b);
            }
        });

        addFilterQuestion = (Button) view.findViewById(R.id.addFilterQuestion);

        addFilterQuestion.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                activity.startActivity(new Intent(activity, FilterQuestion.class));
            }
        });

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

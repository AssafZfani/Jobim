package zfani.assaf.jobim.views.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import zfani.assaf.jobim.R;

public class FilterQuestion extends FragmentActivity {

    private EditText question;
    private RadioGroup radioGroup;
    private View showDeleteQuestionDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.filter_question);

        MainActivity.setupToolBar(this);

        question = findViewById(R.id.question);

        radioGroup = findViewById(R.id.radioGroup);

        showDeleteQuestionDialog = findViewById(R.id.showDeleteQuestionDialog);
    }

    @Override
    protected void onStart() {

        super.onStart();

        findViewById(R.id.backButton).setOnClickListener(view -> onBackPressed());

        findViewById(R.id.addButton).setOnClickListener(view -> {

            String questionText = question.getText().toString();

            if (questionText.isEmpty() ||
                    (radioGroup.getCheckedRadioButtonId() != R.id.yes && radioGroup.getCheckedRadioButtonId() != R.id.no))
                Toast.makeText(FilterQuestion.this, "בחרו את התשובה הרצויה", Toast.LENGTH_SHORT).show();
            else {

                AddNewJob.newJob.setQuestion(questionText);

                AddNewJob.newJob.setAnswer(radioGroup.getCheckedRadioButtonId() == R.id.yes);

                finish();
            }
        });

        String addedQuestion = AddNewJob.newJob.getQuestion();

        if (addedQuestion != null) {

            question.setText(addedQuestion);

            radioGroup.check(AddNewJob.newJob.getAnswer() ? R.id.yes : R.id.no);

            showDeleteQuestionDialog.setVisibility(View.VISIBLE);
        }

        showDeleteQuestionDialog.setOnClickListener(view -> MainActivity.displayDialog((FragmentActivity) view.getContext(), R.layout.delete_question_dialog, null));
    }
}

package zfani.assaf.jobim.Fragments.DetailsFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import zfani.assaf.jobim.Application;
import zfani.assaf.jobim.R;

import static android.util.Patterns.EMAIL_ADDRESS;

public class EmailFragment extends Fragment {

    EditText email;

    public static EmailFragment newInstance() {

        return new EmailFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.email_fragment, container, false);

        email = (EditText) view.findViewById(R.id.email);

        email.requestFocus();

        String emailText = Application.sharedPreferences.getString("Email", null);

        email.setText(emailText != null ? emailText : "");

        return view;
    }

    public boolean isValidValue() {

        String emailText = email.getText().toString();

        boolean result = !emailText.isEmpty() && EMAIL_ADDRESS.matcher(emailText).matches();

        if (result)
            Application.sharedPreferences.edit().putString("Email", emailText).apply();
        else
            Toast.makeText(getActivity(), "חובה למלא כתובת מייל תקינה", Toast.LENGTH_SHORT).show();

        return result;
    }
}

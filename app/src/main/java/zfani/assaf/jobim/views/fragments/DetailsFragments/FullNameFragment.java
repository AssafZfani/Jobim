package zfani.assaf.jobim.views.fragments.DetailsFragments;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import zfani.assaf.jobim.Application;
import zfani.assaf.jobim.R;
import zfani.assaf.jobim.models.RoundedImageView;
import zfani.assaf.jobim.views.activities.MainActivity;

public class FullNameFragment extends Fragment {

    FragmentActivity activity;
    EditText firstName, lastName;

    public static FullNameFragment newInstance() {

        return new FullNameFragment();
    }

    public static void initSelfie(RoundedImageView selfie) {

        String image = Application.sharedPreferences.getString("Image", null);

        if (image != null) {

            byte[] b = Base64.decode(image, Base64.DEFAULT);

            selfie.setImageBitmap(Application.sharedPreferences.getBoolean
                    ("FromCamera", true) ? BitmapFactory.decodeByteArray(b, 0, b.length) : BitmapFactory.decodeFile(image));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.full_name_fragment, container, false);

        activity = getActivity();

        activity.getIntent().putExtra("SmallRound", false);

        RoundedImageView selfie = view.findViewById(R.id.selfie);

        initSelfie(selfie);

        firstName = view.findViewById(R.id.firstName);

        lastName = view.findViewById(R.id.lastName);

        String fullName = Application.sharedPreferences.getString("FullName", null);

        if (fullName != null) {

            int lastSpace = fullName.lastIndexOf(" ");

            firstName.setText(fullName.substring(0, lastSpace));

            lastName.setText(fullName.substring(lastSpace + 1, fullName.length()));
        }

        view.findViewById(R.id.selfie).setOnClickListener(view1 -> MainActivity.displayDialog(activity, R.layout.image_dialog, null));

        return view;
    }

    public boolean isValidValue() {

        String firstNameText = firstName.getText().toString(), lastNameText = lastName.getText().toString();

        boolean result = !firstNameText.isEmpty() && !lastNameText.isEmpty();

        if (result)
            Application.sharedPreferences.edit().putString("FullName", firstNameText + " " + lastNameText).apply();
        else
            Toast.makeText(activity, "חובה למלא שם פרטי ושם משפחה", Toast.LENGTH_SHORT).show();

        return result;
    }
}
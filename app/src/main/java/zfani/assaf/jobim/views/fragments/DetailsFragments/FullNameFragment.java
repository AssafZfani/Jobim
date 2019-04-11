package zfani.assaf.jobim.views.fragments.DetailsFragments;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import zfani.assaf.jobim.App;
import zfani.assaf.jobim.R;
import zfani.assaf.jobim.models.RoundedImageView;
import zfani.assaf.jobim.views.activities.MainActivity;

public class FullNameFragment extends Fragment {

    private Activity activity;
    private EditText firstName;
    private EditText lastName;

    public static FullNameFragment newInstance() {
        return new FullNameFragment();
    }

    public static void initSelfie(RoundedImageView selfie) {
        String image = App.sharedPreferences.getString("Image", null);
        if (image != null) {
            byte[] b = Base64.decode(image, Base64.DEFAULT);
            selfie.setImageBitmap(App.sharedPreferences.getBoolean
                    ("FromCamera", true) ? BitmapFactory.decodeByteArray(b, 0, b.length) : BitmapFactory.decodeFile(image));
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.full_name_fragment, container, false);
        activity = getActivity();
        activity.getIntent().putExtra("SmallRound", false);
        RoundedImageView selfie = view.findViewById(R.id.selfie);
        initSelfie(selfie);
        firstName = view.findViewById(R.id.firstName);
        lastName = view.findViewById(R.id.lastName);
        String fullName = App.sharedPreferences.getString("FullName", null);
        if (fullName != null) {
            int lastSpace = fullName.lastIndexOf(" ");
            firstName.setText(fullName.substring(0, lastSpace));
            lastName.setText(fullName.substring(lastSpace + 1));
        }
        view.findViewById(R.id.selfie).setOnClickListener(view1 -> MainActivity.displayDialog(activity, R.layout.image_dialog, null));
        return view;
    }

    public boolean isValidValue() {
        String firstNameText = firstName.getText().toString(), lastNameText = lastName.getText().toString();
        boolean result = !firstNameText.isEmpty() && !lastNameText.isEmpty();
        if (result) {
            App.sharedPreferences.edit().putString("FullName", firstNameText + " " + lastNameText).apply();
        } else {
            Toast.makeText(activity, "חובה למלא שם פרטי ושם משפחה", Toast.LENGTH_SHORT).show();
        }
        return result;
    }
}
package zfani.assaf.jobim.views.fragments.DetailsFragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import zfani.assaf.jobim.Application;
import zfani.assaf.jobim.R;
import zfani.assaf.jobim.utils.GPSTracker;

public class CityFragment extends Fragment {

    AutoCompleteTextView city;

    public static CityFragment newInstance() {

        return new CityFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.city_fragment, container, false);

        city = (AutoCompleteTextView) view.findViewById(R.id.city);

        String address = Application.sharedPreferences.getString("City", null);

        if (address != null)
            city.setText(address);
        else {

            address = GPSTracker.getAddressFromLatLng(getActivity(), null, GPSTracker.location);

            if (address != null)
                city.setText(address.substring(address.lastIndexOf(", ") + 2, address.length()));
        }

        final String cityText = address;

        city.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String text = editable.toString();

                if (!text.isEmpty() && !text.equalsIgnoreCase(cityText) && text.length() >= 2)
                    ListFragment.initData(getActivity(), null, text);
            }
        });

        view.findViewById(R.id.resetButton).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                city.setText("");
            }
        });

        return view;
    }

    public boolean isValidValue() {

        String cityText = city.getText().toString();

        boolean result = !cityText.isEmpty();

        if (result)
            Application.sharedPreferences.edit().putString("City", cityText).apply();
        else
            Toast.makeText(getActivity(), "חובה לבחור עיר מגורים מהרשימה", Toast.LENGTH_SHORT).show();

        return result;
    }
}

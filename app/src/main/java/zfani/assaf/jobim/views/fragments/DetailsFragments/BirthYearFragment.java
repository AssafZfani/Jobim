package zfani.assaf.jobim.views.fragments.DetailsFragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import zfani.assaf.jobim.Application;
import zfani.assaf.jobim.R;

public class BirthYearFragment extends Fragment {

    NumberPicker numberPicker;

    public static BirthYearFragment newInstance() {

        return new BirthYearFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.birth_year_fragment, container, false);

        numberPicker = ((NumberPicker) view.findViewById(R.id.years));

        numberPicker.setMinValue(1900);

        numberPicker.setMaxValue(2002);

        int birthYear = Application.sharedPreferences.getInt("BirthYear", 0);

        numberPicker.setValue(birthYear != 0 ? birthYear : 1990);

        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        return view;
    }

    public boolean isValidValue() {

        Application.sharedPreferences.edit().putInt("BirthYear", numberPicker.getValue()).apply();

        return true;
    }
}
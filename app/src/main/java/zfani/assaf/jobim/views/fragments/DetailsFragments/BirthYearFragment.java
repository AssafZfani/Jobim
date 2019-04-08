package zfani.assaf.jobim.views.fragments.DetailsFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import zfani.assaf.jobim.App;
import zfani.assaf.jobim.R;

public class BirthYearFragment extends Fragment {

    NumberPicker numberPicker;

    public static BirthYearFragment newInstance() {

        return new BirthYearFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.birth_year_fragment, container, false);

        numberPicker = view.findViewById(R.id.years);

        numberPicker.setMinValue(1900);

        numberPicker.setMaxValue(2002);

        int birthYear = App.sharedPreferences.getInt("BirthYear", 0);

        numberPicker.setValue(birthYear != 0 ? birthYear : 1990);

        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        return view;
    }

    public boolean isValidValue() {

        App.sharedPreferences.edit().putInt("BirthYear", numberPicker.getValue()).apply();

        return true;
    }
}
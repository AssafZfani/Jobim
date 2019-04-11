package zfani.assaf.jobim.views.bottomsheets;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zfani.assaf.jobim.R;
import zfani.assaf.jobim.adapters.ShowByPagerAdapter;

public class ShowByBottomSheet extends BottomSheetDialogFragment {

    @BindView(R.id.etJobFirm)
    EditText etFirm;
    @BindView(R.id.etJobLocation)
    EditText etLocation;
    @BindView(R.id.etJobType)
    EditText etJob;
    @BindView(R.id.rgFragmentsBar)
    RadioGroup rgFragmentsBar;
    @BindView(R.id.vpContainer)
    ViewPager vpContainer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_show_by, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            View bottomSheet = requireDialog().findViewById(com.google.android.material.R.id.design_bottom_sheet);
            BottomSheetBehavior.from(bottomSheet).setPeekHeight(view.getHeight() -
                    Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).getHeight());
        });
    }

    private void initView() {
        EditText[] editTexts = new EditText[3];
        editTexts[0] = etFirm;
        editTexts[1] = etLocation;
        editTexts[2] = etJob;
        for (EditText editText : editTexts) {
            editText.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    switch (editText.getId()) {
                        case R.id.etJobType:
                            //setContentJobTypes(activity, radioGroup, s);
                            break;
                        case R.id.etJobLocation:
                            /*activity.getIntent().putExtra("Address", s + "");
                            editText.setOnEditorActionListener((v, actionId, event) -> {
                                if (v.getText().length() != 0) {
                                    activity.getIntent().putExtra("Address", v.getText() + "");
                                    MapFragment.changeCamera(GPSTracker.getLatLngFromAddress(activity.getApplication(), v.getText() + ""));
                                }
                                return false;
                            });*/
                            break;
                        case R.id.etJobFirm:
                            /*ArrayList<String> filteredData = new ArrayList<>();
                            for (String value : data) {
                                if (value.contains(s)) {
                                    filteredData.add(value);
                                }
                            }
                            setContent(activity, listView, filteredData);*/
                            break;
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }
        rgFragmentsBar.setOnCheckedChangeListener((group, checkedId) -> {
            int item;
            switch (checkedId) {
                case R.id.button1:
                    item = 2;
                    break;
                case R.id.button2:
                    item = 1;
                    break;
                default:
                    item = 0;
                    break;
            }
            for (int i = 0; i < editTexts.length; i++) {
                if (i != item) {
                    editTexts[i].setVisibility(View.INVISIBLE);
                }
            }
            editTexts[item].setVisibility(View.VISIBLE);
            editTexts[item].requestFocus();
            vpContainer.setCurrentItem(item);
        });
        vpContainer.setAdapter(new ShowByPagerAdapter(getChildFragmentManager()));
        vpContainer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int id;
                switch (position) {
                    case 2:
                        id = R.id.button1;
                        break;
                    case 1:
                        id = R.id.button2;
                        break;
                    default:
                        id = R.id.button3;
                        break;
                }
                rgFragmentsBar.check(id);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        vpContainer.setCurrentItem(2);
        rgFragmentsBar.check(R.id.button1);
    }

    @OnClick(R.id.tvAllow)
    void allow() {

    }

    @OnClick(R.id.tvCancel)
    void cancel() {
    }
}

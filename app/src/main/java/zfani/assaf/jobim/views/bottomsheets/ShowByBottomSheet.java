package zfani.assaf.jobim.views.bottomsheets;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zfani.assaf.jobim.R;
import zfani.assaf.jobim.adapters.ShowByPagerAdapter;

public class ShowByBottomSheet extends LinearLayout {

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
    private AppCompatActivity activity;

    public ShowByBottomSheet(Context context) {
        super(context);
        inflate(context, R.layout.bottom_sheet_show_by, this);
        this.activity = (AppCompatActivity) context;
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        EditText[] editTexts = new EditText[3];
        editTexts[0] = etFirm;
        editTexts[1] = etLocation;
        editTexts[2] = etJob;
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
        rgFragmentsBar.check(R.id.button1);
        vpContainer.setAdapter(new ShowByPagerAdapter(activity.getSupportFragmentManager()));
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
    }

    @OnClick(R.id.tvAllow)
    public void allow() {

    }

    @OnClick(R.id.tvCancel)
    public void cancel() {
    }
}

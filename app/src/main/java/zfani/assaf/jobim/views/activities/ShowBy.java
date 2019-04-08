package zfani.assaf.jobim.views.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import zfani.assaf.jobim.R;
import zfani.assaf.jobim.utils.GPSTracker;
import zfani.assaf.jobim.views.fragments.DetailsFragments.ListFragment;
import zfani.assaf.jobim.views.fragments.FeedFragments.MapFragment;
import zfani.assaf.jobim.views.fragments.NewJobFragments.JobTypeFragment;

public class ShowBy extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.show_by);

        MainActivity.setupToolBar(this);

        final EditText[] editTexts = new EditText[3];

        editTexts[0] = findViewById(R.id.editText1);
        editTexts[1] = findViewById(R.id.editText2);
        editTexts[2] = findViewById(R.id.editText3);

        final ViewPager viewPager = findViewById(R.id.viewPager);

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {

                switch (position) {

                    case 2:
                        return JobTypeFragment.newInstance();

                    case 1:
                        return MapFragment.newInstance(GPSTracker.createLatLng(GPSTracker.location));

                    default:
                        return ListFragment.newInstance();
                }
            }

            @Override
            public int getCount() {

                return 3;
            }
        });

        final RadioGroup radioGroup = findViewById(R.id.fragmentsBar);

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {

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

            for (int i = 0; i < editTexts.length; i++)
                if (i != item)
                    editTexts[i].setVisibility(View.INVISIBLE);

            editTexts[item].setVisibility(View.VISIBLE);

            editTexts[item].requestFocus();

            viewPager.setCurrentItem(item);
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

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

                radioGroup.check(id);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        radioGroup.check(R.id.button1);

        viewPager.setCurrentItem(2);
    }

    public void allow(View v) {

        setResult(1, getIntent());

        finish();
    }

    public void cancel(View v) {

        finish();
    }
}
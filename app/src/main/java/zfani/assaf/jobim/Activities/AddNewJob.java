package zfani.assaf.jobim.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RadioGroup;

import zfani.assaf.jobim.Fragments.NewJobFragments.AddressFragment;
import zfani.assaf.jobim.Fragments.NewJobFragments.FirmFragment;
import zfani.assaf.jobim.Fragments.NewJobFragments.JobTitleFragment;
import zfani.assaf.jobim.Fragments.NewJobFragments.JobTypeFragment;
import zfani.assaf.jobim.Fragments.NewJobFragments.PictureFragment;
import zfani.assaf.jobim.Models.NewJob;
import zfani.assaf.jobim.R;

public class AddNewJob extends FragmentActivity {

    public static NewJob newJob;
    FirmFragment firmFragment;
    JobTypeFragment jobTypeFragment;
    JobTitleFragment jobTitleFragment;
    AddressFragment addressFragment;
    PictureFragment pictureFragment;
    RadioGroup fragmentsBar;
    ViewPager viewPager;
    private int[] drawableResources;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_new_job);

        HomePage.setupToolBar(this);

        newJob = new NewJob();

        findViewById(R.id.closeButton).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

        drawableResources = new int[5];

        drawableResources[4] = R.drawable.firm;
        drawableResources[3] = R.drawable.job_type2;
        drawableResources[2] = R.drawable.job_title2;
        drawableResources[1] = R.drawable.address2;
        drawableResources[0] = R.drawable.picture2;

        fragmentsBar = (RadioGroup) findViewById(R.id.fragmentsBar);

        fragmentsBar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

                if (checkedId != -1) {

                    int currentFragment = viewPager.getCurrentItem(), nextFragment;

                    switch (checkedId) {

                        case R.id.pictureButton:
                            nextFragment = 0;
                            break;
                        case R.id.addressButton:
                            nextFragment = 1;
                            break;
                        case R.id.jobTitleButton:
                            nextFragment = 2;
                            break;
                        case R.id.jobTypeButton:
                            nextFragment = 3;
                            break;
                        default:
                            nextFragment = 4;
                            break;
                    }

                    hideKeyboard();

                    moveToAnotherFragment(currentFragment, nextFragment, checkedId);
                }
            }
        });

        viewPager = (ViewPager) findViewById(R.id.viewPager);

        viewPager.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                findViewById(R.id.nextButton).setBackgroundResource(position == 0 ? R.drawable.save_icon : R.drawable.next_icon);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {

                switch (position) {

                    case 0:
                        return pictureFragment = PictureFragment.newInstance();
                    case 1:
                        return addressFragment = AddressFragment.newInstance();
                    case 2:
                        return jobTitleFragment = JobTitleFragment.newInstance();
                    case 3:
                        return jobTypeFragment = JobTypeFragment.newInstance();
                    default:
                        return firmFragment = FirmFragment.newInstance();
                }
            }

            @Override
            public int getCount() {

                return 5;
            }
        });

        viewPager.setCurrentItem(4);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == 1) {

            setResult(1);

            finish();
        }
    }

    @Override
    public void onBackPressed() {

        HomePage.displayDialog(this, R.layout.close_dialog, null);
    }

    private boolean canMovetoFragment(int fragmentNumber) {

        switch (fragmentNumber) {

            default:

                return firmFragment.isValidValue();

            case 3:

                return jobTypeFragment.isValidValue();

            case 2:

                return jobTitleFragment.isValidValue();

            case 1:

                return addressFragment.isValidValue();

            case 0:

                return true;
        }
    }

    private void hideKeyboard() {

        View view = getCurrentFocus();

        if (view != null)
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    void moveToAnotherFragment(int currentFragment, int nextFragment, int checkedId) {

        int button;

        switch (currentFragment) {

            case 4:
                button = R.id.firmButton;
                break;
            case 3:
                button = R.id.jobTypeButton;
                break;
            case 2:
                button = R.id.jobTitleButton;
                break;
            case 1:
                button = R.id.addressButton;
                break;
            default:
                button = R.id.pictureButton;
                break;
        }

        if (canMovetoFragment(currentFragment))

            if (nextFragment != -1) {

                viewPager.setCurrentItem(nextFragment);

                findViewById(button).setBackgroundResource(R.drawable.completed2);

                findViewById(checkedId).setBackgroundResource(drawableResources[nextFragment]);

            } else {

                if (newJob.getBusinessNumber() == 0)
                    moveToAnotherFragment(currentFragment, 3, R.id.jobTypeButton);
                else if (newJob.getTitle() == null)
                    moveToAnotherFragment(currentFragment, 2, R.id.jobTitleButton);
                else if (newJob.getAddress() == null)
                    moveToAnotherFragment(currentFragment, 1, R.id.addressButton);
                else {

                    findViewById(button).setBackgroundResource(R.drawable.completed2);

                    startActivityForResult(new Intent(AddNewJob.this, MakingContact.class), 1);
                }
            }
        else
            fragmentsBar.clearCheck();
    }

    public void next(View v) {

        hideKeyboard();

        int fragmentNumber = viewPager.getCurrentItem();

        int checkedId;

        switch (fragmentNumber - 1) {

            case 3:
                checkedId = R.id.jobTypeButton;
                break;
            case 2:
                checkedId = R.id.jobTitleButton;
                break;
            case 1:
                checkedId = R.id.addressButton;
                break;
            default:
                checkedId = R.id.pictureButton;
                break;
        }

        moveToAnotherFragment(fragmentNumber, fragmentNumber - 1, checkedId);
    }
}
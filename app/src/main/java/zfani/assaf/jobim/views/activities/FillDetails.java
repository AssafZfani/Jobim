package zfani.assaf.jobim.views.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RadioGroup;

import java.io.ByteArrayOutputStream;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import zfani.assaf.jobim.App;
import zfani.assaf.jobim.R;
import zfani.assaf.jobim.models.RoundedImageView;
import zfani.assaf.jobim.utils.AlertHelper;
import zfani.assaf.jobim.views.fragments.DetailsFragments.BirthYearFragment;
import zfani.assaf.jobim.views.fragments.DetailsFragments.CityFragment;
import zfani.assaf.jobim.views.fragments.DetailsFragments.EmailFragment;
import zfani.assaf.jobim.views.fragments.DetailsFragments.FullNameFragment;

public class FillDetails extends AppCompatActivity {

    private int[] drawableResources;
    private RadioGroup fragmentsBar;
    private ViewPager viewPager;
    private FullNameFragment fullNameFragment;
    private CityFragment cityFragment;
    private BirthYearFragment birthYearFragment;
    private EmailFragment emailFragment;

    static void saveImageFromCamera(Activity activity, Intent data) {
        RoundedImageView selfie = activity.findViewById(R.id.selfie);
        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        selfie.setImageBitmap(bitmap);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (bitmap != null) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        }
        String imageEncoded = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        App.sharedPreferences.edit().putBoolean("FromCamera", true).apply();
        App.sharedPreferences.edit().putString("Image", imageEncoded).apply();
    }

    static void saveImageFromGallery(Activity activity, Intent data) {
        RoundedImageView selfie = activity.findViewById(R.id.selfie);
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = activity.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            App.sharedPreferences.edit().putBoolean("FromCamera", false).apply();
            App.sharedPreferences.edit().putString("Image", cursor.getString(cursor.getColumnIndex(filePathColumn[0]))).apply();
            cursor.close();
        }
        selfie.setImageURI(selectedImage);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fill_details);
        MainActivity.setupToolBar(this);
        //findViewById(R.id.closeButton).setOnClickListener(view -> onBackPressed());
        drawableResources = new int[4];
        drawableResources[3] = R.drawable.full_name;
        drawableResources[2] = R.drawable.city2;
        drawableResources[1] = R.drawable.birth_year2;
        drawableResources[0] = R.drawable.email2;
        fragmentsBar = findViewById(R.id.rgFragmentsBar);
        fragmentsBar.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            if (checkedId != -1) {
                int currentFragment = viewPager.getCurrentItem(), nextFragment;
                switch (checkedId) {
                    case R.id.emailButton:
                        nextFragment = 0;
                        break;
                    case R.id.birthYearButton:
                        nextFragment = 1;
                        break;
                    case R.id.cityButton:
                        nextFragment = 2;
                        break;
                    default:
                        nextFragment = 3;
                        break;
                }
                hideKeyboard();
                moveToAnotherFragment(currentFragment, nextFragment, checkedId);
            }
        });
        viewPager = findViewById(R.id.viewPager);
        viewPager.setOnTouchListener((view, motionEvent) -> true);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //findViewById(R.id.nextButton).setBackgroundResource(position == 0 ? R.drawable.save_icon : R.drawable.next_icon);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            @NonNull
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return emailFragment = EmailFragment.newInstance();
                    case 1:
                        return birthYearFragment = BirthYearFragment.newInstance();
                    case 2:
                        return cityFragment = CityFragment.newInstance();
                    default:
                        return fullNameFragment = FullNameFragment.newInstance();
                }
            }

            @Override
            public int getCount() {
                return 4;
            }
        });
        viewPager.setCurrentItem(3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 3) {
                saveImageFromCamera(this, data);
            } else if (requestCode == 4) {
                saveImageFromGallery(this, data);
            }
        }
    }

    @Override
    public void onBackPressed() {
        AlertHelper.displayDialog(this, R.layout.dialog_close, null);
    }

    private boolean canMoveToFragment(int fragmentNumber) {
        switch (fragmentNumber) {
            default:
                return fullNameFragment.isValidValue();
            case 2:
                return cityFragment.isValidValue();
            case 1:
                return birthYearFragment.isValidValue();
            case 0:
                return emailFragment.isValidValue();
        }
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void moveToAnotherFragment(int currentFragment, int nextFragment, int checkedId) {
        int button;
        switch (currentFragment) {
            case 3:
                button = R.id.nameButton;
                break;
            case 2:
                button = R.id.cityButton;
                break;
            case 1:
                button = R.id.birthYearButton;
                break;
            default:
                button = R.id.emailButton;
                break;
        }
        if (canMoveToFragment(currentFragment))
            if (nextFragment != -1) {
                viewPager.setCurrentItem(nextFragment);
                findViewById(button).setBackgroundResource(R.drawable.completed1);
                findViewById(checkedId).setBackgroundResource(drawableResources[nextFragment]);
            } else {
                if (App.sharedPreferences.getString("City", null) == null) {
                    moveToAnotherFragment(currentFragment, 2, R.id.cityButton);
                } else if (App.sharedPreferences.getInt("BirthYear", 0) == 0) {
                    moveToAnotherFragment(currentFragment, 1, R.id.birthYearButton);
                } else {
                    finish();
                }
            }
        else {
            fragmentsBar.clearCheck();
        }
    }

    public void next(View v) {
        hideKeyboard();
        int fragmentNumber = viewPager.getCurrentItem();
        int checkedId;
        switch (fragmentNumber - 1) {
            case 2:
                checkedId = R.id.cityButton;
                break;
            case 1:
                checkedId = R.id.birthYearButton;
                break;
            default:
                checkedId = R.id.emailButton;
                break;
        }
        moveToAnotherFragment(fragmentNumber, fragmentNumber - 1, checkedId);
    }
}
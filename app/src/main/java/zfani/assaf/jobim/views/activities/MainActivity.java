package zfani.assaf.jobim.views.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zfani.assaf.jobim.App;
import zfani.assaf.jobim.R;
import zfani.assaf.jobim.managers.MainManager;
import zfani.assaf.jobim.utils.AlertHelper;
import zfani.assaf.jobim.utils.Constants;
import zfani.assaf.jobim.viewmodels.MainFeedViewModel;
import zfani.assaf.jobim.views.fragments.DetailsFragments.BirthYearFragment;
import zfani.assaf.jobim.views.fragments.DetailsFragments.CityFragment;
import zfani.assaf.jobim.views.fragments.DetailsFragments.EmailFragment;
import zfani.assaf.jobim.views.fragments.DetailsFragments.FullNameFragment;
import zfani.assaf.jobim.views.fragments.MenuFragments.AboutUsFragment;
import zfani.assaf.jobim.views.fragments.MenuFragments.MyDetailsFragment;
import zfani.assaf.jobim.views.fragments.MenuFragments.MyJobsFragment;
import zfani.assaf.jobim.views.fragments.MenuFragments.NotificationsFragment;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolBar;
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.drawerMenu)
    View drawerMenu;
    private MainManager mainManager;
    private MainFeedViewModel mainFeedViewModel;
    private Fragment fragmentToReplace;

    static void setupToolBar(@NonNull AppCompatActivity activity) {
        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        TextView title = toolbar.findViewById(R.id.title);
        View addButton, backButton, closeButton, mapButton, menuButton, nextButton, postButton, saveButton, settingsButton;
        /*addButton = view_action_bar.findViewById(R.id.addButton);
        backButton = view_action_bar.findViewById(R.id.backButton);
        closeButton = view_action_bar.findViewById(R.id.closeButton);*/
        mapButton = toolbar.findViewById(R.id.btnSwitchMode);
        menuButton = toolbar.findViewById(R.id.btnMenu);
        /*nextButton = view_action_bar.findViewById(R.id.nextButton);
        postButton = view_action_bar.findViewById(R.id.postButton);
        saveButton = view_action_bar.findViewById(R.id.saveButton);
        settingsButton = view_action_bar.findViewById(R.id.settingsButton);*/
        /*addButton.setVisibility(View.GONE);
        backButton.setVisibility(View.GONE);
        closeButton.setVisibility(View.GONE);
        nextButton.setVisibility(View.GONE);
        postButton.setVisibility(View.GONE);
        saveButton.setVisibility(View.GONE);
        settingsButton.setVisibility(View.GONE);*/
        int orange = ContextCompat.getColor(activity, R.color.orange);
        String className = activity.getLocalClassName(), text = "";
        switch (className) {
            case "views.activities.AddNewJob": {
                text = "פרסם ג'וב חדש";
            }
            case "views.activities.FillDetails": {
                /*closeButton.setVisibility(View.VISIBLE);
                nextButton.setVisibility(View.VISIBLE);*/
                break;
            }
            case "views.activities.FilterQuestion": {
                text = "שאלת סינון";
                /*addButton.setVisibility(View.VISIBLE);
                backButton.setVisibility(View.VISIBLE);*/
                break;
            }
            case "views.activities.JobsEmployer": {
                menuButton.setEnabled(false);
                menuButton.setBackgroundColor(orange);
                mapButton.setEnabled(false);
                mapButton.setBackgroundColor(orange);
                text = "ג'ובים נוספים ממעסיק זה";
                break;
            }
            case "views.activities.MakingContact": {
                text = "אמצעי יצירת קשר";
                /*postButton.setVisibility(View.VISIBLE);
                backButton.setVisibility(View.VISIBLE);*/
                break;
            }
            default: {
                text = activity.getIntent().getStringExtra("Fragment");
                if (text == null) {
                    text = "JOBIM";
                    mapButton.setEnabled(true);
                    mapButton.setBackground(null);
                } else {
                    mapButton.setEnabled(false);
                    mapButton.setBackgroundColor(orange);
                    switch (text) {
                        case "הפרטים שלי":
                            //settingsButton.setVisibility(View.VISIBLE);
                            break;
                        case "הגדרות":
                        case "עריכת שם ותמונה":
                        case "עריכת עיר מגורים":
                        case "עריכת שנת לידה":
                        case "עריכת כתובת מייל":
                            /*backButton.setVisibility(View.VISIBLE);
                            saveButton.setVisibility(View.VISIBLE);*/
                            break;
                    }
                }
                break;
            }
        }
        title.setText(text);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolBar);
        mainManager = new MainManager(this);
        mainFeedViewModel = ViewModelProviders.of(this).get(MainFeedViewModel.class);
        mainManager.pushMainFeedFragment();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == 1) {
                    //allJobsFragment.filterList(findViewById(R.id.fragmentContainer), data);
                    hideMap();
                }
                break;
            case 2:
                if (resultCode == 1) {
                    handleMenuButtons(findViewById(R.id.myJobs));
                }
                break;
            case 3:
                if (resultCode == RESULT_OK) {
                    FillDetails.saveImageFromCamera(this, data);
                }
                break;
            case 4:
                if (resultCode == RESULT_OK) {
                    FillDetails.saveImageFromGallery(this, data);
                }
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(drawerMenu)) {
            drawerLayout.closeDrawers();
        } else {
            String fragmentName = getIntent().getStringExtra("Fragment");
            if (fragmentName != null) {
                switch (fragmentName) {
                    case "הגדרות":
                    case "עריכת שם ותמונה":
                    case "עריכת עיר מגורים":
                    case "עריכת שנת לידה":
                    case "עריכת כתובת מייל":
                        handleMenuButtons(findViewById(R.id.myDetails));
                        break;
                    default:
                        clean(findViewById(R.id.cleanButton));
                        break;
                }
            } else {
                AlertHelper.displayDialog(this, R.layout.dialog_exit, null);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.KEY_REQUEST_LOCATION_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mainFeedViewModel.setShouldCheckPermission(false);
                }
                break;
        }
    }

    @OnClick(R.id.btnMenu)
    public void menu() {
        String fullName = App.sharedPreferences.getString("FullName", null);
        getIntent().putExtra("SmallRound", true);
        FullNameFragment.initSelfie(findViewById(R.id.menuSelfie));
        ((TextView) drawerMenu.findViewById(R.id.fullName)).setText(fullName != null ? fullName : "היי אורח!");
        drawerLayout.openDrawer(drawerMenu);
    }

    @OnClick(R.id.btnSwitchMode)
    public void switchMode() {
        mainManager.pushOrRemoveMapFragment();
    }

    public void clean(View v) {
        drawerLayout.closeDrawers();
        hideMap();
        //allJobsFragment.clean();
        //fragmentManager.beginTransaction().replace(R.id.fragmentContainer, allJobsFragment).commit();
        getIntent().removeExtra("Fragment");
        MainActivity.setupToolBar(MainActivity.this);
    }

    private void hideMap() {
        /*if (mapFragment != null && mapFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().remove(mapFragment).commitAllowingStateLoss();
            findViewById(R.id.btnSwitchMode).setBackground(background);
        }*/
    }

    private void handleMenuButtons(View v) {
        drawerLayout.closeDrawers();
        String fragmentName = "";
        switch (v.getId()) {
            case R.id.aboutUs:
                fragmentName = "קצת עלינו";
                fragmentToReplace = AboutUsFragment.newInstance();
                break;
            case R.id.addNewJob:
                fragmentToReplace = null;
                startActivityForResult(new Intent(MainActivity.this, AddNewJob.class), 2);
                break;
            case R.id.birthYear:
                fragmentName = "עריכת שנת לידה";
                fragmentToReplace = BirthYearFragment.newInstance();
                break;
            case R.id.city:
                fragmentName = "עריכת עיר מגורים";
                fragmentToReplace = CityFragment.newInstance();
                break;
            case R.id.email:
                fragmentName = "עריכת כתובת מייל";
                fragmentToReplace = EmailFragment.newInstance();
                break;
            case R.id.fullName:
                fragmentName = "עריכת שם ותמונה";
                fragmentToReplace = FullNameFragment.newInstance();
                break;
            case R.id.myDetails:
            case R.id.myDetailsLayout:
                fragmentName = "הפרטים שלי";
                fragmentToReplace = MyDetailsFragment.newInstance();
                break;
            case R.id.myJobs:
                fragmentName = "הג'ובים שלי";
                fragmentToReplace = MyJobsFragment.newInstance();
                break;
            case R.id.notifications:
                fragmentName = "התראות";
                fragmentToReplace = NotificationsFragment.newInstance();
                break;
            /*case R.id.settingsButton:
                fragmentName = "הגדרות";
                fragmentToReplace = SettingsFragment.newInstance();
                break;*/
        }
        if (fragmentToReplace != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragmentToReplace, fragmentName).commitAllowingStateLoss();
            getIntent().putExtra("Fragment", fragmentName);
            MainActivity.setupToolBar(MainActivity.this);
        }
    }

    public void handleMyDetailsButtons(View v) {
        handleMenuButtons(App.sharedPreferences.contains("FullName") ? v : findViewById(R.id.myDetails));
    }

    public void website(View v) {
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://www.drushim.co.il/?utm_source=jobim&utm_medium=app&utm_campaign=gowebsite")));
    }

    /*public void back(View v) {
        onBackPressed();
    }

    public void save(View v) {
        boolean result = false;
        switch (getIntent().getStringExtra("Fragment")) {
            case "הגדרות":
                App.sharedPreferences.edit().putBoolean("EnableNotification", ((SettingsFragment) fragmentToReplace).toggleButton.isChecked()).apply();
                result = true;
                break;
            case "עריכת שם ותמונה":
                FullNameFragment fullNameFragment = (FullNameFragment) fragmentToReplace;
                result = fullNameFragment.isValidValue();
                break;
            case "עריכת עיר מגורים":
                CityFragment cityFragment = (CityFragment) fragmentToReplace;
                result = cityFragment.isValidValue();
                break;
            case "עריכת שנת לידה":
                BirthYearFragment birthYearFragment = (BirthYearFragment) fragmentToReplace;
                result = birthYearFragment.isValidValue();
                break;
            case "עריכת כתובת מייל":
                EmailFragment emailFragment = (EmailFragment) fragmentToReplace;
                result = emailFragment.isValidValue();
                break;
        }
        if (result) {
            onBackPressed();
        }
    }*/
}
package zfani.assaf.jobim.views.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import zfani.assaf.jobim.App;
import zfani.assaf.jobim.R;
import zfani.assaf.jobim.models.Job;
import zfani.assaf.jobim.utils.Adapter;
import zfani.assaf.jobim.utils.FilteredAdapter;
import zfani.assaf.jobim.utils.GPSTracker;
import zfani.assaf.jobim.views.fragments.DetailsFragments.BirthYearFragment;
import zfani.assaf.jobim.views.fragments.DetailsFragments.CityFragment;
import zfani.assaf.jobim.views.fragments.DetailsFragments.EmailFragment;
import zfani.assaf.jobim.views.fragments.DetailsFragments.FullNameFragment;
import zfani.assaf.jobim.views.fragments.FeedFragments.ContactFragment;
import zfani.assaf.jobim.views.fragments.FeedFragments.MapFragment;
import zfani.assaf.jobim.views.fragments.MenuFragments.AboutUsFragment;
import zfani.assaf.jobim.views.fragments.MenuFragments.AllJobsFragment;
import zfani.assaf.jobim.views.fragments.MenuFragments.MyDetailsFragment;
import zfani.assaf.jobim.views.fragments.MenuFragments.MyJobsFragment;
import zfani.assaf.jobim.views.fragments.MenuFragments.NotificationsFragment;
import zfani.assaf.jobim.views.fragments.MenuFragments.SettingsFragment;

public class MainActivity extends FragmentActivity {

    public AllJobsFragment allJobsFragment;
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.drawerMenu)
    View drawerMenu;
    private FragmentManager fragmentManager;
    private Drawable background;
    private MapFragment mapFragment;
    private Fragment fragmentToReplace;

    static void setupToolBar(final FragmentActivity activity) {
        Toolbar toolbar = activity.findViewById(R.id.toolBar);
        TextView title = toolbar.findViewById(R.id.title);
        View addButton, backButton, closeButton, mapButton, menuButton, nextButton, postButton, saveButton, settingsButton;
        addButton = toolbar.findViewById(R.id.addButton);
        backButton = toolbar.findViewById(R.id.backButton);
        closeButton = toolbar.findViewById(R.id.closeButton);
        mapButton = toolbar.findViewById(R.id.mapButton);
        menuButton = toolbar.findViewById(R.id.menuButton);
        nextButton = toolbar.findViewById(R.id.nextButton);
        postButton = toolbar.findViewById(R.id.postButton);
        saveButton = toolbar.findViewById(R.id.saveButton);
        settingsButton = toolbar.findViewById(R.id.settingsButton);
        addButton.setVisibility(View.GONE);
        backButton.setVisibility(View.GONE);
        closeButton.setVisibility(View.GONE);
        nextButton.setVisibility(View.GONE);
        postButton.setVisibility(View.GONE);
        saveButton.setVisibility(View.GONE);
        settingsButton.setVisibility(View.GONE);
        int orange = ContextCompat.getColor(activity, R.color.orange);
        String className = activity.getLocalClassName(), text = "";
        switch (className) {
            case "Activities.AddNewJob": {
                text = "פרסם ג'וב חדש";
            }
            case "Activities.FillDetails": {
                closeButton.setVisibility(View.VISIBLE);
                nextButton.setVisibility(View.VISIBLE);
                break;
            }
            case "Activities.FilterQuestion": {
                text = "שאלת סינון";
                addButton.setVisibility(View.VISIBLE);
                backButton.setVisibility(View.VISIBLE);
                break;
            }
            case "Activities.JobsEmployer": {
                menuButton.setEnabled(false);
                menuButton.setBackgroundColor(orange);
                mapButton.setEnabled(false);
                mapButton.setBackgroundColor(orange);
                text = "ג'ובים נוספים ממעסיק זה";
                break;
            }
            case "Activities.MakingContact": {
                text = "אמצעי יצירת קשר";
                postButton.setVisibility(View.VISIBLE);
                backButton.setVisibility(View.VISIBLE);
                break;
            }
            case "Activities.ShowBy": {
                toolbar.setBackground(activity.getDrawable(R.drawable.action_bar_dark));
                menuButton.setEnabled(false);
                mapButton.setEnabled(false);
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
                            settingsButton.setVisibility(View.VISIBLE);
                            break;
                        case "הגדרות":
                        case "עריכת שם ותמונה":
                        case "עריכת עיר מגורים":
                        case "עריכת שנת לידה":
                        case "עריכת כתובת מייל":
                            backButton.setVisibility(View.VISIBLE);
                            saveButton.setVisibility(View.VISIBLE);
                            break;
                    }
                }
                break;
            }
        }
        title.setText(text);
    }

    public static void displayDialog(final FragmentActivity activity, final int layout, final String jobId) {
        Dialog dialog = new Dialog(activity) {

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(layout);
                View cancel = findViewById(R.id.cancel);
                if (cancel != null) {
                    cancel.setOnClickListener(v -> {
                        dismiss();
                        if (layout == R.layout.fill_details_dialog) {
                            activity.startActivity(new Intent(activity, FillDetails.class));
                        } else if (layout == R.layout.post_job_dialog) {
                            activity.finish();
                        }
                    });
                }
                switch (layout) {
                    case R.layout.add_new_job_dialog:
                        findViewById(R.id.call).setOnClickListener(view -> {
                            Intent callIntent = new Intent(Intent.ACTION_DIAL);
                            callIntent.setData(Uri.parse("tel:0509907979"));
                            activity.startActivity(callIntent);
                        });
                        findViewById(R.id.sendEmail).setOnClickListener(view -> {
                            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:assafzfani@gmail.com"));
                            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "בנוגע ליצירת ג'וב");
                            activity.startActivity(emailIntent);
                        });
                        break;
                    case R.layout.close_dialog:
                        findViewById(R.id.exit).setOnClickListener(view -> {
                            dismiss();
                            App.sharedPreferences.edit().remove("FromCamera").remove("Image").remove("FullName").remove("City").remove("BirthYear").remove("Email").apply();
                            activity.finish();
                        });
                        break;
                    case R.layout.delete_job_dialog:
                        findViewById(R.id.deleteFromFeed).setOnClickListener(v -> {
                            if (FilteredAdapter.filteredList != null) {
                                int indexToRemove = FilteredAdapter.filteredList.indexOf(Job.findJobById(jobId));
                                if (activity.getLocalClassName().equalsIgnoreCase("Activities.MainActivity")) {
                                    MainActivity mainActivity = (MainActivity) activity;
                                    if (mainActivity.allJobsFragment.filteredAdapter != null)
                                        mainActivity.allJobsFragment.filteredAdapter.remove(indexToRemove);
                                } else
                                    FilteredAdapter.filteredList.remove(indexToRemove);
                            }
                            new Handler().postDelayed(() -> Adapter.query.getRef().child(jobId).removeValue(), 750);
                            if (activity.getLocalClassName().equalsIgnoreCase("Activities.JobInfo"))
                                activity.finish();
                            else
                                ((ViewPager) activity.findViewById(activity.getIntent().getIntExtra("ViewPager", 0))).setCurrentItem(1);
                            dismiss();
                            Toast.makeText(activity, "מעכשיו הג'וב לא יופיע יותר בפיד", Toast.LENGTH_SHORT).show();
                        });
                        break;
                    case R.layout.delete_question_dialog:
                        findViewById(R.id.deleteQuestion).setOnClickListener(view -> {
                            AddNewJob.newJob.setAnswer(false);
                            AddNewJob.newJob.setQuestion(null);
                            dismiss();
                            activity.finish();
                        });
                        break;
                    case R.layout.exit_dialog:
                        findViewById(R.id.exit).setOnClickListener(v -> {
                            dismiss();
                            activity.finish();
                        });
                        break;
                    case R.layout.image_dialog:
                        findViewById(R.id.camera).setOnClickListener(view -> {
                            dismiss();
                            activity.startActivityForResult(new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE), 3);
                        });
                        findViewById(R.id.gallery).setOnClickListener(view -> {
                            dismiss();
                            activity.startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 4);
                        });
                        break;
                    case R.layout.sending_mail_dialog:
                        findViewById(R.id.send).setOnClickListener(view -> {
                            dismiss();
                            ContactFragment.contact(activity, jobId, R.id.sendEmail);
                        });
                        break;
                    case R.layout.share_dialog:
                        ((TextView) findViewById(R.id.dialogText)).setText("שלחנו ל" + activity.getIntent().getStringExtra("ContactName") + " המלצה על המשרה");
                        break;
                }
            }
        };
        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        new GPSTracker(this);
        setupToolBar(this);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.mainFragment, allJobsFragment = (AllJobsFragment) AllJobsFragment.newInstance()).commit();
        background = findViewById(R.id.mapButton).getBackground();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == 1) {
                    allJobsFragment.filterList(findViewById(R.id.mainFragment), data);
                    hideMap();
                }
                break;
            case 2:
                if (resultCode == 1)
                    handleMenuButtons(findViewById(R.id.myJobs));
                break;
            case 3:
                if (resultCode == RESULT_OK)
                    FillDetails.saveImageFromCamera(this, data);
                break;
            case 4:
                if (resultCode == RESULT_OK)
                    FillDetails.saveImageFromGallery(this, data);
                break;
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
                        handleMenuButtons(findViewById(R.id.myDeatils));
                        break;
                    default:
                        clean(findViewById(R.id.cleanButton));
                        break;
                }
            } else {
                displayDialog(this, R.layout.exit_dialog, null);
            }
        }
    }

    public void menu(View v) {
        String fullName = App.sharedPreferences.getString("FullName", null);
        getIntent().putExtra("SmallRound", true);
        FullNameFragment.initSelfie(findViewById(R.id.menuSelfie));
        ((TextView) drawerMenu.findViewById(R.id.fullName)).setText(fullName != null ? fullName : "היי אורח!");
        drawerLayout.openDrawer(drawerMenu);
    }

    public void map(View v) {
        if (GPSTracker.location != null) {
            v.setBackground(v.getBackground() == background ? getDrawable(R.drawable.squares) : background);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            int commit = v.getBackground() == background ?
                    fragmentTransaction.remove(mapFragment).commit() : fragmentTransaction.add(R.id.mapFragment, mapFragment =
                    MapFragment.newInstance(GPSTracker.createLatLng(GPSTracker.location))).commit();
        }
    }

    public void clean(View v) {
        drawerLayout.closeDrawers();
        hideMap();
        allJobsFragment.clean();
        fragmentManager.beginTransaction().replace(R.id.mainFragment, allJobsFragment).commit();
        getIntent().removeExtra("Fragment");
        MainActivity.setupToolBar(MainActivity.this);
    }

    private void hideMap() {
        if (mapFragment != null && mapFragment.isAdded()) {
            fragmentManager.beginTransaction().remove(mapFragment).commitAllowingStateLoss();
            findViewById(R.id.mapButton).setBackground(background);
        }
    }

    public void showBy(View v) {
        if (GPSTracker.location != null) {
            startActivityForResult(new Intent(MainActivity.this, ShowBy.class), 1);
        }
    }

    public void handleMenuButtons(View v) {
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
            case R.id.myDeatils:
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
            case R.id.settingsButton:
                fragmentName = "הגדרות";
                fragmentToReplace = SettingsFragment.newInstance();
                break;
        }
        if (fragmentToReplace != null) {
            fragmentManager.beginTransaction().replace(R.id.mainFragment, fragmentToReplace, fragmentName).commitAllowingStateLoss();
            getIntent().putExtra("Fragment", fragmentName);
            MainActivity.setupToolBar(MainActivity.this);
        }
    }

    public void handleMyDetailsButtons(View v) {
        handleMenuButtons(App.sharedPreferences.contains("FullName") ? v : findViewById(R.id.myDeatils));
    }

    public void website(View v) {
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://www.drushim.co.il/?utm_source=jobim&utm_medium=app&utm_campaign=gowebsite")));
    }

    public void back(View v) {
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
    }
}
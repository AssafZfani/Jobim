package zfani.assaf.jobim.views.fragments.DetailsFragments;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import zfani.assaf.jobim.R;
import zfani.assaf.jobim.utils.GPSTracker;
import zfani.assaf.jobim.views.activities.AddNewJob;

public class ListFragment extends Fragment {

    static String currentAddress;
    ArrayList<String> data;

    public static ListFragment newInstance() {

        return new ListFragment();
    }

    static void setContent(final Activity activity, ListView listView, final ArrayList<String> data) {

        final RadioGroup radioGroup = new RadioGroup(activity);

        listView.setAdapter(new ArrayAdapter<String>(activity, R.layout.radio_button, data) {

            @NonNull

            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {

                final boolean isShowByActivity = activity.getLocalClassName().equalsIgnoreCase("Activities.ShowBy");

                final RadioButton radioButton = (RadioButton) (convertView == null ?
                        LayoutInflater.from(activity).inflate(R.layout.radio_button, parent, false) : convertView);

                radioGroup.addView(radioButton);

                radioButton.setId(View.generateViewId());

                radioButton.setHint(data.get(position));

                radioButton.setOnClickListener(view -> {

                    if (isShowByActivity)
                        activity.getIntent().putExtra("Firm", radioButton.getHint());
                    else {

                        AddNewJob.newJob.setAddress(radioButton.getHint().toString());

                        AddNewJob.newJob.setDistance(activity);

                        activity.findViewById(R.id.pictureButton).performClick();
                    }
                });

                if (isShowByActivity) {

                    String firm = activity.getIntent().getStringExtra("Firm");

                    radioButton.setChecked(firm != null && firm.equalsIgnoreCase(radioButton.getHint().toString()));

                } else if (data.size() == 1 && data.get(0).equalsIgnoreCase(currentAddress)) {

                    radioButton.setChecked(true);

                    AddNewJob.newJob.setAddress(currentAddress);

                    AddNewJob.newJob.setDistance(activity);
                }

                Drawable drawable = activity.getDrawable(R.drawable.radiobutton);

                if (drawable != null)
                    drawable.setBounds(0, 0, 75, 75);

                radioButton.setCompoundDrawablesRelative(drawable, null, null, null);

                return radioButton;
            }
        });
    }

    static void initData(final Activity activity, final ListView listView, String text) {

        final boolean isAddNewJobActivity = activity.getLocalClassName().equalsIgnoreCase("Activities.AddNewJob");

        new AsyncTask<String, Void, String>() {

            @Override
            protected String doInBackground(String... params) {

                HttpURLConnection connection = null;

                try {

                    URL url = new URL(params[0]);

                    connection = (HttpURLConnection) url.openConnection();

                    connection.connect();

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    StringBuilder stringBuilder = new StringBuilder();

                    String line;

                    while ((line = bufferedReader.readLine()) != null)
                        stringBuilder.append(line);

                    bufferedReader.close();

                    return stringBuilder.toString();

                } catch (Exception e) {

                    return e.getMessage();

                } finally {

                    if (connection != null)
                        connection.disconnect();
                }
            }

            @Override
            protected void onPostExecute(String json) {

                JSONObject obj = null;

                try {

                    obj = new JSONObject(json);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (obj != null) {

                    try {

                        ArrayList<String> data = new ArrayList<>();

                        JSONArray jsonArray = obj.getJSONArray("predictions");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i).getJSONObject("structured_formatting");

                            String value = jsonObject.getString("main_text");

                            String country = "";

                            if (jsonObject.has("secondary_text"))
                                country = jsonObject.getString("secondary_text");

                            if ((country.isEmpty() || country.contains("ישראל")) && !data.contains(value))
                                data.add(isAddNewJobActivity ? value + ", " + country.substring(0, country.indexOf(",")) : value);
                        }

                        if (isAddNewJobActivity) {

                            if (data.isEmpty())
                                activity.findViewById(R.id.addressNotFound).setVisibility(View.VISIBLE);

                            ListFragment.setContent(activity, listView, data);
                        } else
                            ((AutoCompleteTextView) activity.findViewById(R.id.city)).setAdapter
                                    (new ArrayAdapter<>(activity, R.layout.city_layout, data));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        }.execute("https://maps.googleapis.com/maps/api/place/autocomplete/json?input=" + text + "&types=" +
                (isAddNewJobActivity ? "address" : "(cities)") + "&language=he_IL&key=" +
                activity.getResources().getString(R.string.google_places_key));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final Activity activity = getActivity();

        final ListView listView = new ListView(activity);

        listView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        if (activity.getLocalClassName().equalsIgnoreCase("Activities.ShowBy")) {

            data = new ArrayList<>(Arrays.asList(activity.getResources().getStringArray(R.array.firms)));

            setContent(activity, listView, data);

            ((EditText) activity.findViewById(R.id.editText1)).addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    ArrayList<String> filteredData = new ArrayList<>();

                    for (String value : data)
                        if (value.contains(s))
                            filteredData.add(value);

                    setContent(activity, listView, filteredData);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            TextView footer = new TextView(activity);

            footer.setPadding(0, 50, 0, 50);

            footer.setGravity(Gravity.CENTER);

            footer.setText("חפש חברות נוספות על ידי הקלדת השם שלהן בתיבת החיפוש");

            footer.setTextColor(Color.BLACK);

            footer.setTypeface(Typeface.MONOSPACE);

            listView.addFooterView(footer);

        } else {

            data = new ArrayList<>();

            data.add(currentAddress = GPSTracker.getAddressFromLatLng(activity, null, GPSTracker.location));

            setContent(activity, listView, data);

            ((EditText) activity.findViewById(R.id.searchAddress)).addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    boolean isEmptyString = charSequence.toString().isEmpty();

                    activity.findViewById(R.id.addressNotFound).setVisibility
                            (isEmptyString ? View.VISIBLE : View.GONE);

                    if (!isEmptyString)
                        AddNewJob.newJob.setAddress(null);

                    initData(activity, listView, charSequence.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }

        return listView;
    }
}

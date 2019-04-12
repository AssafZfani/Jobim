package zfani.assaf.jobim.views.fragments.DetailsFragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import zfani.assaf.jobim.R;
import zfani.assaf.jobim.adapters.ListAdapter;
import zfani.assaf.jobim.utils.GPSTracker;
import zfani.assaf.jobim.viewmodels.ShowByBottomSheetViewModel;
import zfani.assaf.jobim.views.activities.AddNewJob;

public class ListFragment extends Fragment {

    @BindView(R.id.rvList)
    RecyclerView rvList;
    @BindView(R.id.tvMoreFirms)
    TextView tvMoreFirms;
    private ListAdapter listAdapter;

    public static ListFragment newInstance(boolean isComeFromShowBy) {
        ListFragment listFragment = new ListFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isComeFromShowBy", isComeFromShowBy);
        listFragment.setArguments(bundle);
        return listFragment;
    }

    static void initData(boolean isComeFromShowBy, Activity activity, ListView listView, String text) {
        boolean isAddNewJobActivity = activity.getLocalClassName().equalsIgnoreCase("views.activities.AddNewJob");
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
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                } catch (Exception e) {
                    return e.getMessage();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
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
                            if (jsonObject.has("secondary_text")) {
                                country = jsonObject.getString("secondary_text");
                            }
                            if ((country.isEmpty() || country.contains("ישראל")) && !data.contains(value)) {
                                data.add(isAddNewJobActivity ? value + ", " + country.substring(0, country.indexOf(",")) : value);
                            }
                        }
                        if (isAddNewJobActivity) {
                            if (data.isEmpty()) {
                                activity.findViewById(R.id.addressNotFound).setVisibility(View.VISIBLE);
                            }
                            //ListFragment.setContent(isComeFromShowBy, activity, listView, data);
                        } else
                            ((AutoCompleteTextView) activity.findViewById(R.id.city)).setAdapter(new ArrayAdapter<>(activity, R.layout.city_layout, data));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.execute("https://maps.googleapis.com/maps/api/place/autocomplete/json?input=" + text + "&types=" + (isAddNewJobActivity ? "address" : "(cities)") + "&language=he_IL&key=" + activity.getResources().getString(R.string.google_places_key));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);
        boolean isComeFromShowBy = requireArguments().getBoolean("isComeFromShowBy");
        tvMoreFirms.setVisibility(isComeFromShowBy ? View.VISIBLE : View.GONE);
        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        ArrayList<String> items = new ArrayList<>();
        if (isComeFromShowBy) {
            items.addAll(Arrays.asList(activity.getResources().getStringArray(R.array.firms)));
            ViewModelProviders.of(activity).get(ShowByBottomSheetViewModel.class).getJobFirmQuery().observe(this, s -> listAdapter.getFilter().filter(s));
        } else {
            items.add(GPSTracker.getAddressFromLatLng(activity, null, GPSTracker.location));
            ((EditText) activity.findViewById(R.id.searchAddress)).addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    boolean isEmptyString = charSequence.toString().isEmpty();
                    activity.findViewById(R.id.addressNotFound).setVisibility(isEmptyString ? View.VISIBLE : View.GONE);
                    if (!isEmptyString) {
                        AddNewJob.newJob.setAddress(null);
                    }
                    //initData(isComeFromShowBy, activity, listView, charSequence.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }
        rvList.setAdapter(listAdapter = new ListAdapter(items, isComeFromShowBy));
        rvList.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        return view;
    }
}

package zfani.assaf.jobim.Fragments.MenuFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import zfani.assaf.jobim.Application;
import zfani.assaf.jobim.R;

public class NotificationsFragment extends Fragment {

    public static NotificationsFragment newInstance() {

        return new NotificationsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.notifications, container, false);

        final int notificationsCount = Application.sharedPreferences.getInt("NotificationsCount", 0);

        if (notificationsCount != 0) {

            view.findViewById(R.id.message).setVisibility(View.INVISIBLE);

            ListView notifications = (ListView) view.findViewById(R.id.notifications);

            final Context context = container.getContext();

            notifications.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1) {

                @NonNull

                @Override
                public View getView(int position, View convertView, @NonNull ViewGroup parent) {

                    if (convertView == null)
                        convertView = View.inflate(context, R.layout.notification_item, null);

                    TextView notification = (TextView) convertView.findViewById(R.id.notification);

                    TextView date = (TextView) convertView.findViewById(R.id.date);

                    notification.setText(Application.sharedPreferences.getString("notification" + (notificationsCount - position), ""));

                    Long dateOrTime = Application.sharedPreferences.getLong("time" + (notificationsCount - position), 0);

                    date.setText(DateFormat.format(DateUtils.isToday(dateOrTime) ? "HH:mm" : "dd.MM.yy", dateOrTime));

                    return convertView;
                }

                @Override
                public int getCount() {
                    return notificationsCount;
                }
            });
        }

        return view;
    }
}

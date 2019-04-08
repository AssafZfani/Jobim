package zfani.assaf.jobim.models;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import androidx.viewpager.widget.PagerAdapter;
import zfani.assaf.jobim.R;
import zfani.assaf.jobim.utils.Adapter;
import zfani.assaf.jobim.views.fragments.FeedFragments.JobFragment;

public class ClusterJobsRenderer extends DefaultClusterRenderer<ClusterJobs>
        implements ClusterManager.OnClusterClickListener<ClusterJobs>, ClusterManager.OnClusterItemClickListener<ClusterJobs> {

    private Activity activity;
    private HashMap<ClusterJobs, Job> hashMap;
    private Adapter.ViewHolder viewHolder;
    private TextView txt;

    public ClusterJobsRenderer(Activity activity, GoogleMap map, ClusterManager<ClusterJobs> clusterManager, HashMap<ClusterJobs, Job> hashMap) {

        super(activity, map, clusterManager);

        this.activity = activity;
        this.hashMap = hashMap;
    }

    @Override
    protected void onBeforeClusterRendered(Cluster<ClusterJobs> cluster, MarkerOptions markerOptions) {

        super.onBeforeClusterRendered(cluster, markerOptions);

        markerOptions.icon(BitmapDescriptorFactory.fromAsset("full_marker.png"));
    }

    @Override
    protected void onBeforeClusterItemRendered(ClusterJobs item, MarkerOptions markerOptions) {

        super.onBeforeClusterItemRendered(item, markerOptions);

        Drawable drawable = null;

        try {

            drawable = Drawable.createFromStream(activity.getAssets().open("1_" + item.job.getBusinessNumber() + ".png"), "");

        } catch (IOException e) {
            e.printStackTrace();
        }

        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{activity.getDrawable(R.drawable.marker), drawable});

        Canvas canvas = new Canvas();

        Bitmap bitmap = Bitmap.createBitmap(100, 123, Bitmap.Config.ARGB_8888);

        canvas.setBitmap(bitmap);

        layerDrawable.setBounds(0, 0, 100, 123);

        layerDrawable.draw(canvas);

        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
    }

    @Override
    public boolean onClusterItemClick(final ClusterJobs clusterJobs) {

        return createClusterDialog(new Cluster<ClusterJobs>() {

            @Override
            public LatLng getPosition() {

                return clusterJobs.getPosition();
            }

            @Override
            public Collection<ClusterJobs> getItems() {

                ArrayList<ClusterJobs> array = new ArrayList<>(1);

                array.add(clusterJobs);

                return array;
            }

            @Override
            public int getSize() {
                return 1;
            }
        });
    }

    @Override
    public boolean onClusterClick(Cluster<ClusterJobs> cluster) {

        return createClusterDialog(cluster);
    }

    private boolean createClusterDialog(final Cluster cluster) {

        viewHolder = new Adapter.ViewHolder(View.inflate(activity, R.layout.layouts_container, null));

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 550);

        layoutParams.weight = 8;

        viewHolder.mainView.setLayoutParams(layoutParams);

        viewHolder.viewPager.setAdapter(new PagerAdapter() {

            @Override
            public int getCount() {
                return cluster.getSize();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {

                final Job job = hashMap.get(cluster.getItems().toArray()[position]);

                View view = View.inflate(activity, R.layout.cluster_layout, null);

                JobFragment.fillJobDetails(view, job);

                container.addView(view);

                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });

        viewHolder.viewPager.setCurrentItem(cluster.getSize() - 1);

        final LinearLayout container = new LinearLayout(activity), mainView = new LinearLayout(activity);

        mainView.setOrientation(LinearLayout.VERTICAL);

        Button btn1 = createButton(false), btn2 = createButton(true);

        container.addView(btn1);

        container.addView(viewHolder.mainView);

        container.addView(btn2);

        container.setPadding(0, 0, 0, 50);

        mainView.addView(container);

        txt = new TextView(activity);

        txt.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        txt.setTextSize(20);

        int count = viewHolder.viewPager.getAdapter().getCount();

        txt.setText("ג'וב " + count + "/" + (count - viewHolder.viewPager.getCurrentItem()));

        mainView.addView(txt);

        Dialog dialog = new Dialog(activity);

        dialog.setContentView(mainView);

        Window window = dialog.getWindow();

        if (window != null)
            window.setBackgroundDrawable(null);

        dialog.show();

        return true;
    }

    private Button createButton(final boolean next) {

        Button btn = new Button(activity, null, android.R.style.Widget_DeviceDefault_Button_Borderless);

        btn.setText(next ? ">" : "<");

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 150);

        layoutParams.gravity = Gravity.CENTER;

        layoutParams.weight = 1;

        btn.setLayoutParams(layoutParams);

        btn.setTextSize(50);

        btn.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        btn.setOnClickListener(view -> {

            if (next && viewHolder.viewPager.getCurrentItem() - 1 >= 0)
                viewHolder.viewPager.setCurrentItem(viewHolder.viewPager.getCurrentItem() - 1);

            if (!next && viewHolder.viewPager.getCurrentItem() + 1 <= viewHolder.viewPager.getAdapter().getCount())
                viewHolder.viewPager.setCurrentItem(viewHolder.viewPager.getCurrentItem() + 1);

            int count = viewHolder.viewPager.getAdapter().getCount();

            txt.setText("ג'וב " + count + "/" + (count - viewHolder.viewPager.getCurrentItem()));
        });

        return btn;
    }
}

package zfani.assaf.jobim.custom_views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import zfani.assaf.jobim.R;

class CutLayout extends LinearLayout {

    private Activity activity;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path path = new Path();

    public CutLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.activity = (Activity) context;
    }

    @Override
    public void dispatchDraw(Canvas canvas) {
        if (!activity.getLocalClassName().equalsIgnoreCase("views.activities.JobInfoActivity") && getId() != R.id.drawerMenu) {
            super.dispatchDraw(canvas);
        } else {
            int saveCount = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
            super.dispatchDraw(canvas);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            if (getId() == R.id.jobLayout) {
                path.moveTo(0, getHeight() - 75);
                path.lineTo(getWidth(), getHeight());
                path.lineTo(0, getHeight());
            } else if (getId() == R.id.clMapFragment) {
                path.moveTo(0, 0);
                path.lineTo(0, 75);
                path.lineTo(getWidth(), 0);
            } else if (getId() == R.id.drawerMenu) {
                path.moveTo(0, getHeight());
                path.lineTo(75, getHeight());
                path.lineTo(150, 0);
                path.lineTo(0, 0);
            }
            canvas.drawPath(path, paint);
            canvas.restoreToCount(saveCount);
            paint.setXfermode(null);
        }
    }
}
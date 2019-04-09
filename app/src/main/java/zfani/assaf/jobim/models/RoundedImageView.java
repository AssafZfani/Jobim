package zfani.assaf.jobim.models;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

public class RoundedImageView extends AppCompatImageView {

    private final Activity activity;

    public RoundedImageView(Context context, AttributeSet attrs) {

        super(context, attrs);

        this.activity = (Activity) context;
    }

    private static Bitmap getCroppedBitmap(Bitmap bmp, int radius) {

        Bitmap bitmap;

        if (bmp.getWidth() != radius || bmp.getHeight() != radius) {

            float smallest = Math.min(bmp.getWidth(), bmp.getHeight());

            float factor = smallest / radius;

            bitmap = Bitmap.createScaledBitmap(bmp, (int) (bmp.getWidth() / factor), (int) (bmp.getHeight() / factor), false);

        } else
            bitmap = bmp;

        Bitmap output = Bitmap.createBitmap(radius, radius, Config.ARGB_8888);

        Canvas canvas = new Canvas(output);

        final String color = "#BAB399";

        final Paint paint = new Paint();

        final Rect rect = new Rect(0, 0, radius, radius);

        paint.setAntiAlias(true);

        paint.setFilterBitmap(true);

        paint.setDither(true);

        canvas.drawARGB(0, 0, 0, 0);

        paint.setColor(Color.parseColor(color));

        canvas.drawCircle(radius / 2 + 0.7f, radius / 2 + 0.7f, radius / 2 + 0.1f, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Drawable drawable = getDrawable();

        if (drawable == null)
            return;

        Bitmap b = ((BitmapDrawable) drawable).getBitmap();

        Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);

        int radius = activity.getIntent().getBooleanExtra("SmallRound", true) ? 125 : 300;

        Bitmap roundBitmap = getCroppedBitmap(bitmap, radius);

        canvas.drawBitmap(roundBitmap, 0, 0, null);
    }
}
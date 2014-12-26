package uk.dan_gilbert.paytouch.ui.transformation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.squareup.picasso.Transformation;

import uk.dan_gilbert.paytouch.R;

/**
 * Created by dangilbert on 26/12/14.
 */
public class CircleTransform implements Transformation {

    private Context context;

    public CircleTransform(Context context) {
        this.context = context;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        int size = Math.min(source.getWidth(), source.getHeight());

        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;
        float strokeWidth = context.getResources().getDimensionPixelSize(R.dimen.outline_width);

        Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
        if (squaredBitmap != source) {
            source.recycle();
        }

        Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        BitmapShader shader = new BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setAntiAlias(true);

        Paint circlePaint = new Paint();
        circlePaint.setColor(context.getResources().getColor(R.color.black));
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setStrokeWidth(strokeWidth);

        RectF rect = new RectF(strokeWidth / 2, strokeWidth / 2, squaredBitmap.getWidth() - strokeWidth/2, squaredBitmap.getHeight() - strokeWidth / 2);

        float r = size/2f;
        canvas.drawCircle(r, r, r, paint);
        canvas.drawArc(rect, 0, 360, false, circlePaint);

        squaredBitmap.recycle();
        return bitmap;
    }

    @Override
    public String key() {
        return "circle";
    }
}
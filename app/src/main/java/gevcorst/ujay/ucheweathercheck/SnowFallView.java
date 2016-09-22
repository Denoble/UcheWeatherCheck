package gevcorst.ujay.ucheweathercheck;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Ujay on 1/11/2015.
 */
public class SnowFallView extends View {
    private int snow_flake_count = 10;
    private final List<Drawable> drawables = new ArrayList<Drawable>();
    private int[][] coords;
    private final Drawable snow_flake;

    public SnowFallView(Context context) {
        super(context);
        setFocusable(true);
        setFocusableInTouchMode(true);
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#F5FEFF"));
        Bitmap bg = Bitmap.createBitmap(480, 800, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bg);

        canvas.drawCircle(2, 2, 5, paint);

        //snow_flake = context.getResources().getDrawable(R.drawable.snow_flake);
        snow_flake= (Drawable)new BitmapDrawable(getResources(),bg);
        snow_flake.setBounds(0, 0, snow_flake.getIntrinsicWidth(), snow_flake
                .getIntrinsicHeight());
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldw, int oldh) {
        super.onSizeChanged(width, height, oldw, oldh);
        Random random = new Random();
        Interpolator interpolator = new LinearInterpolator();

        snow_flake_count = Math.max(width, height) / 20;
        coords = new int[snow_flake_count][];
        drawables.clear();
        for (int i = 0; i < snow_flake_count; i++) {
            Animation animation = new TranslateAnimation(0, height / 10
                    - random.nextInt(height / 5), 0, height + 30);
            animation.setDuration(10 * height + random.nextInt(3 * height));
            animation.setRepeatCount(-1);
            animation.initialize(10, 10, 10, 10);
            animation.setInterpolator(interpolator);

            coords[i] = new int[] { random.nextInt(width - 30), -30 };

            drawables.add(new AnimateDrawable(snow_flake, animation));
            animation.setStartOffset(random.nextInt(20 * height));
            animation.startNow();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < snow_flake_count; i++) {
            Drawable drawable = drawables.get(i);
            canvas.save();
            canvas.translate(coords[i][0], coords[i][1]);
            drawable.draw(canvas);
            canvas.restore();
        }
        invalidate();
    }

}
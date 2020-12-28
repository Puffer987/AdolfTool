package cn.adolf.adolf.pathAnim;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * @program: Adolf
 * @description:
 * @author: yjq
 * @create: 2020-12-24 15:10
 **/

public class MyPathView extends View {
    private Paint paint;
    private Path path, dstPath;
    private PathMeasure pathMeasure;
    private ValueAnimator valueAnimator;
    private float curValue;
    private boolean isNext = false;
    private float length;
    private float animatorValue;

    public MyPathView(Context context) {
        super(context);
        init();
    }

    public MyPathView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyPathView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        dstPath.reset();
        dstPath.lineTo(0, 0);
        float stop = length * animatorValue;
        float start = (float) (stop - ((0.5 - Math.abs(animatorValue - 0.5)) * length));
        pathMeasure.getSegment(start, stop, dstPath, true);
        canvas.drawPath(dstPath, paint);
    }


    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);

        dstPath = new Path();
        path = new Path();
        path.addCircle(500, 500, 200, Path.Direction.CW);

        pathMeasure = new PathMeasure(path, false);
        length = pathMeasure.getLength();

        final ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                animatorValue = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.setDuration(1300);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.start();
    }
}
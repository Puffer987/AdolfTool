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

public class AiliPayView extends View {
    private Paint paint;
    private Path path, desPath;
    private PathMeasure pathMeasure;
    private ValueAnimator valueAnimator;
    private float curValue;
    private boolean isNext = false;

    public AiliPayView(Context context) {
        super(context);
        init();
    }

    public AiliPayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AiliPayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(20);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        path = new Path();
        desPath = new Path();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        path.addCircle(w / 2, h / 2, w / 2 - 4, Path.Direction.CW);
        path.moveTo(w / 4, h / 2);
        path.lineTo(w / 2, h / 4 * 3);
        path.lineTo(w / 4 * 3, h / 4);

        pathMeasure = new PathMeasure(path, false);
        valueAnimator = ValueAnimator.ofFloat(0, 2);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                curValue = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.setDuration(3000);
        valueAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (curValue < 1) {
            float stop = pathMeasure.getLength() * curValue;
            pathMeasure.getSegment(0, stop, desPath, true);
        } else {
            if (!isNext) {
                isNext = true;
                //这里是临界状态，getlength是圆的总长
                pathMeasure.getSegment(0, pathMeasure.getLength(), desPath, true);
                //路径轮廓切换
                pathMeasure.nextContour();
            } else {
                //getlength是新轮廓的总长，还有true的设置，因为折线路径在圆里面，所以把绘制起点更换，否则，折线的起点和圆又会连起来
                float stop = pathMeasure.getLength() * (curValue - 1);
                pathMeasure.getSegment(0, stop, desPath, true);
            }
        }
        canvas.drawPath(desPath, paint);
    }
}
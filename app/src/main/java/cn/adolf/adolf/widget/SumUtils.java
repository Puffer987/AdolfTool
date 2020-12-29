package cn.adolf.adolf.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import java.util.Calendar;
import java.util.Date;

/**
 * @program: LoveWidget
 * @description:
 * @author: Adolf
 * @create: 2020-10-30 14:44
 **/
public class SumUtils {

    public static String getSumDays() {
        Calendar nowCal = Calendar.getInstance();
        Date nowDate = nowCal.getTime();

        Calendar fallingCal = Calendar.getInstance();
        fallingCal.set(Calendar.YEAR, 2017);
        fallingCal.set(Calendar.MONTH, 3);
        fallingCal.set(Calendar.DATE, 22);
        fallingCal.set(Calendar.HOUR_OF_DAY, 22);
        fallingCal.set(Calendar.MINUTE, 30);
        fallingCal.set(Calendar.SECOND, 0);
        Date fallingDate = fallingCal.getTime();

        long sumTimeMillis = nowDate.getTime() - fallingDate.getTime();
        return (int) (sumTimeMillis / 86400000) + "";
    }

    public static String getSumDays(long timestamp) {
        Calendar nowCal = Calendar.getInstance();
        Date nowDate = nowCal.getTime();

        long sumTimeMillis = nowDate.getTime() - timestamp;
        return (int) (sumTimeMillis / 86400000) + "";
    }

    public static String getSumDays(int year, int month, int date, int hour, int min, int second) {
        Calendar nowCal = Calendar.getInstance();
        Date nowDate = nowCal.getTime();

        Calendar fallingCal = Calendar.getInstance();
        fallingCal.set(Calendar.YEAR, year);
        fallingCal.set(Calendar.MONTH, month - 1);
        fallingCal.set(Calendar.DATE, date);
        fallingCal.set(Calendar.HOUR_OF_DAY, hour);
        fallingCal.set(Calendar.MINUTE, min);
        fallingCal.set(Calendar.SECOND, second);
        Date fallingDate = fallingCal.getTime();

        long sumTimeMillis = nowDate.getTime() - fallingDate.getTime();
        return (int) (sumTimeMillis / 86400000) + "";
    }

    public static Bitmap buildBitmapWithShadow(Context context, String str, int fontSize) {
        Paint paint = new Paint();
        Typeface fontStyle = Typeface.createFromAsset(context.getAssets(), "fonts/branch_zystoo_Regular_2.otf");
        paint.setAntiAlias(true);
        paint.setSubpixelText(true);
        paint.setTypeface(fontStyle);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.GREEN);
        paint.setFakeBoldText(true);
        paint.setTextSize(fontSize);

        Bitmap bitmap = Bitmap.createBitmap((int) paint.measureText(str) + 1, (int) (paint.descent() - paint.ascent()) + 1, Bitmap.Config.ARGB_8888);
        Canvas myCanvas = new Canvas(bitmap);

        // 计算Baseline绘制的起点X轴坐标 ，计算方式：画布宽度的一半 - 文字宽度的一半
        int baseX = (int) (myCanvas.getWidth() / 2 - paint.measureText(str) / 2);
        // 计算Baseline绘制的Y坐标 ，计算方式：画布高度的一半 - 文字总高度的一半
        int baseY = (int) ((myCanvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));

        paint.setColor(Color.parseColor("#C0C0C0"));
        myCanvas.drawText(str, baseX + 1, baseY + 1, paint);

        paint.setColor(Color.WHITE);
        myCanvas.drawText(str, baseX, baseY, paint);

        return bitmap;
    }
}

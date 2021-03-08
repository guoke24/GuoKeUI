package com.guohao.guokeui.hencoder.hencoderpracticedraw2.sample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import androidx.annotation.Nullable;
import com.guohao.guokeui.R;
import android.util.AttributeSet;
import android.view.View;



public class Sample14MaskFilterView extends View {
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Bitmap bitmap;
    MaskFilter maskFilter1 = new BlurMaskFilter(50, BlurMaskFilter.Blur.NORMAL);
    MaskFilter maskFilter2 = new BlurMaskFilter(50, BlurMaskFilter.Blur.INNER);
    MaskFilter maskFilter3 = new BlurMaskFilter(50, BlurMaskFilter.Blur.OUTER);
    MaskFilter maskFilter4 = new BlurMaskFilter(50, BlurMaskFilter.Blur.SOLID);

    public Sample14MaskFilterView(Context context) {
        super(context);
    }

    public Sample14MaskFilterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Sample14MaskFilterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        // 不生效，但预览界面生效的，可以去那看看效果
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.what_the_fuck);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setMaskFilter(maskFilter1);
        canvas.drawBitmap(bitmap, 100, 50, paint);

        paint.setMaskFilter(maskFilter2);
        canvas.drawBitmap(bitmap, bitmap.getWidth() + 200, 50, paint);

        paint.setMaskFilter(maskFilter3);
        canvas.drawBitmap(bitmap, 100, bitmap.getHeight() + 100, paint);

        paint.setMaskFilter(maskFilter4);
        canvas.drawBitmap(bitmap, bitmap.getWidth() + 200, bitmap.getHeight() + 100, paint);
    }
}

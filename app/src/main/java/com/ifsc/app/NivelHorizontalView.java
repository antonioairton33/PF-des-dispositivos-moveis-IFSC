
package com.ifsc.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class NivelHorizontalView extends View {

    private Paint pincel;

    public NivelHorizontalView(Context context, AttributeSet attrs) {
        super(context, attrs);

        pincel = new Paint();
        pincel.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float centroX = getWidth() / 2f;
        float centroY = getHeight() / 2f;

        // coopo verdinho
        pincel.setColor(Color.rgb(190, 210, 100));
        pincel.setStyle(Paint.Style.FILL);

        canvas.drawRoundRect(
                new RectF(10, centroY - 60, getWidth() - 10, centroY + 60),
                5, 5, pincel
        );

        // contorno
        pincel.setColor(Color.rgb(65, 75, 65));
        pincel.setStyle(Paint.Style.STROKE);
        pincel.setStrokeWidth(6);

        canvas.drawRoundRect(
                new RectF(5, centroY - 60, getWidth() - 5, centroY + 60),
                5, 5, pincel
        );

        canvas.drawLine(centroX - 40 , centroY - 50,
                centroX - 40, centroY + 50, pincel);

        canvas.drawLine(centroX + 40, centroY - 50,
                centroX + 40, centroY + 50, pincel);

        // bolinha do centro
        pincel.setStyle(Paint.Style.FILL);
        pincel.setColor(Color.rgb(255, 255, 255));

        canvas.drawCircle(centroX, centroY, 20, pincel);
    }
}

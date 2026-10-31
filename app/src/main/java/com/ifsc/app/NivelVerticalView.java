
package com.ifsc.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class NivelVerticalView extends View {

    private Paint pincel;

    public NivelVerticalView(Context context, AttributeSet attrs) {
        super(context, attrs);

        pincel = new Paint();
        pincel.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float centroX = getWidth() / 2f;
        float centroY = getHeight() / 2f;

        pincel.setColor(Color.rgb(190, 210, 100));
        pincel.setStyle(Paint.Style.FILL);

        canvas.drawRoundRect(
                new RectF(centroX - 60, 5, centroX + 60, getHeight() - 5),
                5, 5, pincel
        );

        pincel.setColor(Color.rgb(65, 75, 65));
        pincel.setStyle(Paint.Style.STROKE);
        pincel.setStrokeWidth(6);

        canvas.drawRoundRect(
                new RectF(centroX - 60, 5, centroX + 60, getHeight() - 5),
                5, 5, pincel
        );

        canvas.drawLine(centroX - 50, centroY - 40,
                centroX + 50, centroY - 40, pincel);

        canvas.drawLine(centroX - 50, centroY + 40,
                centroX + 50, centroY + 40, pincel);

        pincel.setStyle(Paint.Style.FILL);
        pincel.setColor(Color.rgb(255, 255, 255));

        canvas.drawCircle(centroX, centroY, 20, pincel);
    }
}

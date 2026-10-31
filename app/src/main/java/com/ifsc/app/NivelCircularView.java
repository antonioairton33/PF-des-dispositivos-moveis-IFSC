package com.ifsc.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class NivelCircularView extends View {

    private Paint pincel;

    public NivelCircularView(Context context, AttributeSet attrs) {
        super(context, attrs);

        pincel = new Paint();
        pincel.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float centroX = getWidth() / 2f;
        float centroY = getHeight() / 2f;

        float raio = Math.min(getWidth(), getHeight()) * 0.42f;

        pincel.setColor(Color.rgb(65, 85, 45));
        pincel.setStyle(Paint.Style.FILL);

        canvas.drawCircle(centroX, centroY, raio, pincel);

        pincel.setColor(Color.rgb(155, 190, 65));

        canvas.drawCircle(centroX, centroY, raio * 0.82f, pincel);

        pincel.setColor(Color.DKGRAY);
        pincel.setStyle(Paint.Style.STROKE);
        pincel.setStrokeWidth(5);

        canvas.drawLine(centroX - raio, centroY,
                centroX + raio, centroY, pincel);

        canvas.drawLine(centroX, centroY - raio,
                centroX, centroY + raio, pincel);

        pincel.setStyle(Paint.Style.FILL);
        pincel.setColor(Color.WHITE);

        canvas.drawCircle(centroX, centroY, 20, pincel);
    }
}


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
    private float inclinacao;

    private boolean nivelado;

    public void setNivelado(boolean valor) {
        nivelado = valor;
    }

    public NivelHorizontalView(Context context, AttributeSet attrs) {
        super(context, attrs);

        pincel = new Paint();
        pincel.setAntiAlias(true);
    }

    public void setInclinacao(float valor) {
        inclinacao = valor;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float centroX = getWidth() / 2f;
        float centroY = getHeight() / 2f;

        float deslocamento = inclinacao * (getWidth() / 2f - 30);

        pincel.setColor(Color.rgb(190, 210, 100));
        pincel.setStyle(Paint.Style.FILL);

        canvas.drawRoundRect(
                new RectF(10, centroY - 30, getWidth() - 10, centroY + 30),
                5, 5, pincel
        );

        pincel.setColor(Color.rgb(65, 75, 65));
        pincel.setStyle(Paint.Style.STROKE);
        pincel.setStrokeWidth(6);

        canvas.drawRoundRect(
                new RectF(10, centroY - 30, getWidth() - 10, centroY + 30),
                5, 5, pincel
        );

        canvas.drawLine(centroX - 40, centroY - 25,
                centroX - 40, centroY + 25, pincel);

        canvas.drawLine(centroX + 40, centroY - 25,
                centroX + 40, centroY + 25, pincel);

        pincel.setStyle(Paint.Style.FILL);
        pincel.setColor(nivelado ? Color.rgb(0, 200, 80) : Color.WHITE);

        canvas.drawCircle(centroX + deslocamento, centroY, 20, pincel);
    }
}

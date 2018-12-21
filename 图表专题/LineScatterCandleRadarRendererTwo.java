package com.github.mikephil.charting.renderer.myview;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.interfaces.datasets.ILineScatterCandleRadarDataSet;
import com.github.mikephil.charting.renderer.BarLineScatterCandleBubbleRenderer;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * Created by Philipp Jahoda on 11/07/15.
 */
public abstract class LineScatterCandleRadarRendererTwo extends BarLineScatterCandleBubbleRenderer {

    /**
     * path that is used for drawing highlight-lines (drawLines(...) cannot be used because of dashes)
     */
    private Path mHighlightLinePath = new Path();

    public LineScatterCandleRadarRendererTwo(ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(animator, viewPortHandler);
    }

    /**
     * Draws vertical & horizontal highlight-lines if enabled.
     *
     * @param c
     * @param x     x-position of the highlight line intersection
     * @param y     y-position of the highlight line intersection
     * @param set   the currently drawn dataset
     * @param r     圆环半径
     * @param width 圆环的环宽度
     */
    protected void drawHighlightLines(Canvas c, float x, float y, ILineScatterCandleRadarDataSet set, float r, float width) {

        // set color and stroke-width
        mHighlightPaint.setColor(set.getHighLightColor());
        mHighlightPaint.setStrokeWidth(set.getHighlightLineWidth());

        // draw highlighted lines (if enabled)
        mHighlightPaint.setPathEffect(set.getDashPathEffectHighlight());

        // draw vertical highlight lines
        if (set.isVerticalHighlightIndicatorEnabled()) {

            // create vertical path
            mHighlightLinePath.reset();
            //画虚线
            PathEffect pathEffect = new DashPathEffect(new float[]{10, 10, 10, 10}, 3);
            mHighlightPaint.setPathEffect(pathEffect);

            mHighlightLinePath.moveTo(x, y);
            mHighlightLinePath.lineTo(x, mViewPortHandler.contentBottom());

            c.drawPath(mHighlightLinePath, mHighlightPaint);

            //画圆环---特定需求
            //大圆
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.rgb(255, 119, 10));
            paint.setStrokeWidth(1);
            c.drawCircle(x, y, r, paint);

            //小圆
            Paint paint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint1.setAntiAlias(true);
            paint1.setStyle(Paint.Style.FILL);
            paint1.setColor(Color.rgb(243, 243, 243));
            paint1.setStrokeWidth(1);
            c.drawCircle(x, y, (r - width), paint1);
        }

    }
}

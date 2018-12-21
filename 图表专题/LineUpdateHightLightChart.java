
package com.github.mikephil.charting.charts;

import android.content.Context;
import android.util.AttributeSet;

import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.renderer.myview.LineChartUpdateHightLightRenderer;

/**
 * Chart that draws lines, surfaces, circles, ...
 *
 * @author Philipp Jahoda
 */
public class LineUpdateHightLightChart extends BarLineChartBase<LineData> implements LineDataProvider {

    public LineUpdateHightLightChart(Context context) {
        super(context);
    }

    public LineUpdateHightLightChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LineUpdateHightLightChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        super.init();

        mRenderer = new LineChartUpdateHightLightRenderer(this, mAnimator, mViewPortHandler);
    }

    /**
     * hightlight上边的圆环
     *
     * @param r     大圆半径
     * @param width 圆环宽度
     */
    public void setCircleWidth(float r, float width) {
        ((LineChartUpdateHightLightRenderer) mRenderer).setCircleWidth(r, width);
    }


    @Override
    public LineData getLineData() {
        return mData;
    }

    @Override
    protected void onDetachedFromWindow() {
        // releases the bitmap in the renderer to avoid oom error
        if (mRenderer != null && mRenderer instanceof LineChartUpdateHightLightRenderer) {
            ((LineChartUpdateHightLightRenderer) mRenderer).releaseBitmap();
        }
        super.onDetachedFromWindow();
    }
}

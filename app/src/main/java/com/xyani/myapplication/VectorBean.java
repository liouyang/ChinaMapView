package com.xyani.myapplication;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Region;

/**
 * User: 1241734684@qq.com
 * Description:
 * Date:2023-07-07 16
 * Time:55
 */

class VectorBean {
    private Path path;
    private String name;
    private String fillColor;
    private String strokeColor;
    private String strokeWidth;

    private PointF point;



    public VectorBean(Path path, String name, String fillColor, String strokeColor, String strokeWidth, PointF point) {
        this.path = path;
        this.name = name;
        this.fillColor = fillColor;
        this.strokeColor = strokeColor;
        this.strokeWidth = strokeWidth;
        this.point = point;
    }



    /**
     * 绘制省份
     *
     * @param paint
     * @param canvas
     * @param isSelected 是否被选中
     */
    public void draw(Paint paint, Canvas canvas, boolean isSelected) {
        if (isSelected) {
            //绘制点击后的背景
            paint.setStrokeWidth(Float.valueOf(strokeWidth));
            paint.setColor(Color.parseColor(strokeColor));
            paint.setStyle(Paint.Style.FILL);
            //添加阴影
            paint.setShadowLayer(8, 0, 0, Color.BLACK);
            canvas.drawPath(path, paint);
            //绘制背景
            paint.clearShadowLayer();
            paint.setStrokeWidth(Float.valueOf(strokeWidth));
            paint.setColor(Color.parseColor(fillColor));
            paint.setStyle(Paint.Style.FILL);
            canvas.drawPath(path, paint);

        } else {
            //绘制背景
            paint.setStrokeWidth(Float.valueOf(strokeWidth));
            paint.clearShadowLayer();
            paint.setColor(Color.parseColor(strokeColor));
            paint.setStyle(Paint.Style.FILL);
            canvas.drawPath(path, paint);
            //绘制边界线
            paint.setStrokeWidth(Float.valueOf(Float.valueOf(strokeWidth)));
            paint.setColor(Color.parseColor(fillColor));
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawPath(path, paint);

        }

    }

    //触摸点是否在这个省的path内
    public boolean isTouch(int x, int y) {
        //构造一个矩形对象
        RectF rectF = new RectF();
        //返回路径控制点的计算边界保存到rectF
        path.computeBounds(rectF, true);
        //构造一个区域对象
        Region region = new Region();
        //给区赋值
        region.setPath(path, new Region((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom));
        return region.contains(x, y);
    }

    public void drawText(Paint mPaint, Canvas canvas) {
        mPaint.clearShadowLayer();
        mPaint.setTextSize(20f);
        mPaint.setColor(Color.BLACK);
        RectF rectF = new RectF();
        //返回路径控制点的计算边界保存到rectF
        path.computeBounds(rectF, true);
        //计算baseline
//        Paint.FontMetrics fontMetrics=mPaint.getFontMetrics();
//        float distance=(fontMetrics.bottom - fontMetrics.top)/2 - fontMetrics.bottom;
//        float baseline=rectF.centerY()+distance;

        canvas.drawText(name,point.x-mPaint.measureText(name)/2,point.y,mPaint);



    }
}


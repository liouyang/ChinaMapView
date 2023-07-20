package com.xyani.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.OverScroller;

import androidx.annotation.NonNull;
import androidx.core.graphics.PathParser;
import androidx.core.view.GestureDetectorCompat;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * User: 1241734684@qq.com
 * Description:
 * Date:2023-07-07 11
 * Time:51
 */
public class ChinaMapView extends View {
    private Paint mPaint;
    private Context context;
    private ArrayList<VectorBean> itemList = new ArrayList<>();
    private VectorBean selectItem;

    private Matrix mMatrix;
    private Matrix invertMatrix;

    private GestureDetectorCompat gestureDetectorCompat;

    private RectF totalRectF = null;



    private static final float MAX_SCALE =20f;



    private boolean showDebugInfo = true;

    private float focusX, focusY;
    private ScaleGestureDetector mScaleGestureDetector;
    private float[] points;
    private float[] pointsFocusBefore;


    private long lastTouchTime;
    private VelocityTracker mVelocityTracker;

    public ChinaMapView(Context context) {
        this(context, null);

    }

    public ChinaMapView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChinaMapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
   private OverScroller scroller=null;
    private long lastScrollTime = 0;
    private boolean isDragging = false;
    private boolean isScaling = false;
    private void init(Context context) {
        //准备画笔
        scroller= new OverScroller(context);
        mMatrix=new Matrix();
        invertMatrix=new Matrix();

        focusX=0;
        focusY=0;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        this.context = context;
        thread.start();

        //手势处理类
        gestureDetectorCompat = new GestureDetectorCompat(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent event) {
//                scroller.forceFinished(true);

                return true;
            }




            @Override
            public boolean onSingleTapConfirmed(@NonNull MotionEvent event) {

                float x = event.getX();
                float y = event.getY();
                points = new float[]{x, y};
                invertMatrix.mapPoints(points);

                handlerTouch(points[0], points[1]);


                return super.onSingleTapConfirmed(event);
            }

            @Override
            public boolean onScroll(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {
//                if ((Math.abs(distanceX)>scaledTouchSlop)||(Math.abs(distanceY)>scaledTouchSlop)){
//
//                }
//                post(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        ((FrameLayout) getParent()).scrollBy((int) distanceX, (int) distanceY);
//                    }
//                });
//
//                long currentTime = System.currentTimeMillis();
//                if (currentTime - lastScrollTime > 40) {
//                    ((FrameLayout) getParent()).scrollBy((int) distanceX, (int) distanceY);
//                    lastScrollTime = currentTime;
//                }
//                ObjectAnimator.ofInt(((FrameLayout) getParent()), "scrollX", (int) distanceX).start();
//                ObjectAnimator.ofInt(((FrameLayout) getParent()), "scrollY", (int) distanceY).start();
//
//                float dx = event.getX() - startX;
//                float dy = event.getY() - startY;
                // 使用 Scroller 进行滑动
//                if (!scroller.isFinished()) {
//                    scroller.abortAnimation();
//                }
//                scroller.startScroll(getScrollX(), getScrollY(), (int) -distanceX, (int) -distanceY);
//                   postInvalidateOnAnimation();();
//                startX = event.getX();
//                startY = event.getY();
//                ((VectorParentView) getParent()).startScroll(
//                        (int) -distanceX, (int) -distanceY
//                );
//                scrollBy((int)distanceX,(int)distanceY);


//                if (userScale >= 1.0f) {
//                    offsetX += -distanceX / userScale;
//                    offsetY += -distanceY / userScale;

//                }
                if (!scroller.isFinished()) {
                    scroller.abortAnimation();
                }
                if (!isScaling) {
                    mMatrix.postTranslate(-distanceX, -distanceY);



                    float[] values = new float[9];
                    mMatrix.getValues(values);

                    RectF contentRect = getContentRect();
//

//                    Log.d("sssssstransX","============="+transX+"--------------==="+transY+"========");
                    checkBoundaries();


                       postInvalidateOnAnimation();
//                    }

                }
                return true;
            }

            @Override
            public boolean onFling(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
//                Log.d("onFling","==================onFling===="+velocityX+"=============="+velocityY);
//
//                if (userScale >=1.0f) {
//                    float minX =viewWidth- totalRectF.width()*userScale;
//                    float minY =viewHeight-totalRectF.height()*userScale;
//                    scroller.fling((int) (offsetX * userScale), (int) (offsetY * userScale),
//                            (int) velocityX, (int) velocityY,
//                            (int) (minX * userScale), MAX_SCROLL, (int) (minY * userScale), MAX_SCROLL);
//                       postInvalidateOnAnimation();();
//                }
//                if (!isScaling) {
//                    // 这里可以根据需要对惯性滑动速度进行调整
//                    velocityX *= FLING_DAMPING_FACTOR;
//                    velocityY *= FLING_DAMPING_FACTOR;
//                    mMatrix.postTranslate(velocityX, velocityY);
//                    checkBoundaries();
//                    postInvalidate();
//                }

                // 在onFling中实现惯性滑动效果
//                startInertiaAnimation(velocityX, velocityY);
//                if (!isScaling) {

//                    startInertiaAnimation(velocityX, velocityY);

//                }
                float[] values = new float[9];
                mMatrix.getValues(values);
                float transX = values[Matrix.MTRANS_X];
                float transY = values[Matrix.MTRANS_Y];

                RectF contentRect = getContentRect();
                // 计算视图缩放后的宽度和高度
                float contentWidth = contentRect.width() ;
                float contentHeight = contentRect.height() ;

                // 计算最大平移X值和最大平移Y值
                float maxTransX = (viewWidth-contentWidth) ;
                float maxTransY = (viewHeight-contentHeight) ;

                Log.d("onFlingTransX","============="+transX+"--------------==="+transY+"========");

                scroller.fling((int)transX,(int)transY,(int)velocityX,(int)velocityY,(int)maxTransX,0,(int)maxTransY,0);
                postInvalidateOnAnimation();
                return true;
            }
        });



        mScaleGestureDetector =new ScaleGestureDetector(context,new ScaleGestureDetector.SimpleOnScaleGestureListener(){
            float lastScaleFactor;
            boolean mapPoint = false;

            @Override
            public boolean onScaleBegin(@NonNull ScaleGestureDetector detector) {
//                userScale = Math.max(MIN_SCALE, Math.min(userScale, MAX_SCALE));
//                MIN_SCALE = Math.min(1.0f, Math.min(viewWidth / (float) totalRectF.width(), viewHeight / (float) totalRectF.height()));
                mapPoint=true;
                isScaling=true;
//                Log.d("isScaling","===onScaleBegin====");
                return super.onScaleBegin(detector);
            }

            @Override
            public boolean onScale(@NonNull ScaleGestureDetector detector) {


                float scaleFactor = detector.getScaleFactor();
//                Log.d("scaleFactor","===scaleFactor===="+scaleFactor);

                float[] points = new float[]{detector.getFocusX(), detector.getFocusY()};
                pointsFocusBefore = new float[]{detector.getFocusX(), detector.getFocusY()};
                if (mapPoint) {
                    mapPoint = false;
                    invertMatrix.mapPoints(points);
                    focusX = points[0];
                    focusY = points[1];
                }

                float scale = getScale();

                if (isScaling) {
                    scaleFactor = 1.0f;
                    isScaling = false;
                }

        //        在INIT_VALUE到MAX_SCALE范围内缩放(放大不超过MAX_SCALE，缩小不小于INIT_SCALE)
                if ((scale < MAX_SCALE && scaleFactor >= 1.0f) || (scale > initScale && scaleFactor <= 1.0f)) {
                    if (scale * scaleFactor > MAX_SCALE) {//放大超过MAX_SCALE的处理
                        scaleFactor = MAX_SCALE / scale;
                    } else if (scale * scaleFactor < initScale) {//缩小超过INIT_SCALE的处理
                        scaleFactor = initScale / scale;
                    }
                    mMatrix.postScale(scaleFactor, scaleFactor,focusX,focusY);
                    invalidate();
                }

                return true;
            }

            @Override
            public void onScaleEnd(@NonNull ScaleGestureDetector detector) {
                super.onScaleEnd(detector);
//                handleScaleBoundaries(detector);
            }
        });

    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(scroller.isFinished()){
            return;
        }
        if (scroller.computeScrollOffset()) {
            float currentX = scroller.getCurrX();
            float currentY = scroller.getCurrY();
//            Log.d("computeScrollOffset","=========="+currentX+"=========="+currentY);

            float[] values = new float[9];
            mMatrix.getValues(values);
            values[Matrix.MTRANS_X] = currentX;
            values[Matrix.MTRANS_Y] = currentY;
            mMatrix.setValues(values);
            checkBoundaries();
            postInvalidateOnAnimation();
        }
    }

    private boolean resetFactor=false;

    private float initScale = 1.0f;
    //  获取全局缩放比例(相对于未缩放时的缩放比例),和直接用getScaleX()有区别！
    private float getScale() {
        float[] values = new float[9];
        mMatrix.getValues(values);

        return values[Matrix.MSCALE_X];
    }

    private void checkBoundScale() {
        float[] values = new float[9];
        mMatrix.getValues(values);
        float scale = values[Matrix.MSCALE_X];

        // 定义允许的最小和最大缩放值
        float minScale = 1.0f; // 最小缩放值
        float maxScale = 2.0f; // 最大缩放值

        // 边界处理
        if (scale > maxScale) {
            scale = maxScale;
        } else if (scale < minScale) {
            scale = minScale;
        }
//         设置调整后的平移值
        values[Matrix.MSCALE_X] = scale;
        values[Matrix.MSCALE_Y] = scale;

        mMatrix.setValues(values);
    }


    // 在缩放操作结束后调用此方法检查并处理边界回弹
    private void handleScaleBoundaries(ScaleGestureDetector detector) {
        float[] values = new float[9];
        mMatrix.getValues(values);
        float scale = values[Matrix.MSCALE_X];

        // 定义允许的最小和最大缩放值
        float minScale = 1.0f; // 最小缩放值
        float maxScale = 3.0f; // 最大缩放值

        if (scale < minScale) {
            // 如果当前缩放小于最小缩放值，进行回弹动画，将其缩放回最小值
            ValueAnimator scaleAnimator = ValueAnimator.ofFloat(scale, minScale);
            scaleAnimator.setDuration(200);

            scaleAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    // 获取视图的中心坐标



                    float viewCenterX =viewHeight / 2f;
                    float viewCenterY = viewHeight  / 2f;

                    // 获取缩放后的矩阵中心坐标
                    float[] newValues = new float[9];
                    mMatrix.getValues(newValues);
                    float newTransX = newValues[Matrix.MTRANS_X];
                    float newTransY = newValues[Matrix.MTRANS_Y];

                    RectF contentRect = getContentRect();
//        Log.d("contentRect","============="+contentRect.width()+"--------------==="+contentRect.height()+"========"+scale);
                    // 计算视图缩放后的宽度和高度
                    float contentWidth = contentRect.width() ;
                    float contentHeight = contentRect.height() ;

                    float newCenterX = (contentWidth/ 2f) + newTransX;
                    float newCenterY = (contentHeight / 2f) + newTransY;

                    // 计算平移距离
                    float transX = viewCenterX - newCenterX;
                    float transY = viewCenterY - newCenterY;

                    // 平移到矩阵中心
                    mMatrix.postTranslate(transX, transY);
                    postInvalidateOnAnimation(); // 重绘视图
                }
            });
            scaleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float newScale = (float) animation.getAnimatedValue();
                    mMatrix.setScale(newScale, newScale,detector.getFocusX(),detector.getFocusY());
                    postInvalidateOnAnimation();// 重绘视图
                }


            });
            scaleAnimator.start();
        } else if (scale > maxScale) {
            // 如果当前缩放大于最大缩放值，进行回弹动画，将其缩放回最大值
            ValueAnimator scaleAnimator = ValueAnimator.ofFloat(scale, maxScale);
            scaleAnimator.setDuration(200);
            scaleAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);

                    float viewCenterX =viewHeight / 2f;
                    float viewCenterY = viewHeight  / 2f;

                    // 获取缩放后的矩阵中心坐标
                    float[] newValues = new float[9];
                    mMatrix.getValues(newValues);
                    float newScale = newValues[Matrix.MSCALE_X];
                    float newTransX = newValues[Matrix.MTRANS_X];
                    float newTransY = newValues[Matrix.MTRANS_Y];

                    RectF contentRect = getContentRect();
//        Log.d("contentRect","============="+contentRect.width()+"--------------==="+contentRect.height()+"========"+scale);
                    // 计算视图缩放后的宽度和高度
                    float contentWidth = contentRect.width() ;
                    float contentHeight = contentRect.height() ;

                    float newCenterX = (contentWidth/ 2f) + newTransX;
                    float newCenterY = (contentHeight / 2f) + newTransY;

                    // 计算平移距离
                    float transX = viewCenterX - newCenterX;
                    float transY = viewCenterY - newCenterY;

                    // 平移到矩阵中心
                    mMatrix.postTranslate(transX, transY);
                       postInvalidateOnAnimation();// 重绘视图
                }
            });
            scaleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float newScale = (float) animation.getAnimatedValue();
                    mMatrix.setScale(newScale, newScale,detector.getFocusX(),detector.getFocusY());
                       postInvalidateOnAnimation(); // 重绘视图
                }
            });
            scaleAnimator.start();
        }
    }

    private int viewWidth=0;
    private int viewHeight=0;
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d("onSizeChanged","======"+w+"===="+h+"=====");
        viewWidth=w;
        viewHeight=h;
    }

    private void checkBoundaries() {


//        RectF rect = getContentRect();
//
//
//
//        int width = viewWidth;
//        int height = viewHeight;
//        Log.d("checkBoundaries",rect.toString()+"======="+rect.width());
//        // 边界回弹的实现
//        float[] values = new float[9];
//        mMatrix.getValues(values);
//
//        float transX = values[Matrix.MTRANS_X];
//        float transY = values[Matrix.MTRANS_Y];
//        float scale = values[Matrix.MSCALE_X];
//
//        float contentWidth = totalRectF.width() * scale;
//        float contentHeight = totalRectF.height() * scale;
//        Log.d("transX","============="+transX+"--------------==="+transY);
//        float dx = 0.0f;
//        float dy = 0.0f;
//
//        // 限制左边界
//        if (transX > 0) {
//            dx = -transX;
//            Log.d("边界","========左边界====="+dx);
//        }
//        // 限制右边界
//        else if (transX + contentWidth < width) {
//            dx = width - contentWidth - transX;
//            Log.d("边界","========右边界====="+dx);
//        }
//
//        // 限制上边界
//        if (transY > 0) {
//            dy = -transY;
//        }
//        // 限制下边界
//        else if (transY + contentHeight < height) {
//            dy = height - contentHeight - transY;
//        }
//        Log.d("dxy","============="+dx+"--------------==="+dy);
        // 进行边界回弹
//        mMatrix.postTranslate(dx, dy);


        float[] values = new float[9];
        mMatrix.getValues(values);
        float scale = values[Matrix.MSCALE_X];
        float transX = values[Matrix.MTRANS_X];
        float transY = values[Matrix.MTRANS_Y];

        // 定义允许的最大和最小平移值
        float minTransX = 0.0f; // 最小平移X值
        float minTransY = 0.0f; // 最小平移Y值
        RectF contentRect = getContentRect();
//        Log.d("contentRect","============="+contentRect.width()+"--------------==="+contentRect.height()+"========"+scale);
        // 计算视图缩放后的宽度和高度
        float contentWidth = contentRect.width() ;
        float contentHeight = contentRect.height() ;

// 计算最大平移X值和最大平移Y值
        float maxTransX = (viewWidth-contentWidth) ;
        float maxTransY = (viewHeight-contentHeight) ;


        if (contentWidth<viewWidth){
            transX=minTransX;
        }else {
            // 边界处理
            if (transX > minTransX) {
                transX = minTransX;
            } else if (transX < maxTransX) {
                transX = maxTransX;
            }
        }

        if (contentHeight<viewHeight){
            transY=minTransY;
        }else {
            if (transY > minTransY) {
                transY = minTransY;
            } else if (transY < maxTransY) {
                transY = maxTransY;
            }
        }

//        Log.d("dxy","============="+transX+"--------------==="+transY+"========"+scale);
//         设置调整后的平移值
        values[Matrix.MTRANS_X] = transX;
        values[Matrix.MTRANS_Y] = transY;
        mMatrix.setValues(values);
//           postInvalidateOnAnimation();(); // 重绘视图
    }

    private RectF getContentRect() {
        RectF rectF = new RectF(0,0,totalRectF.width(),totalRectF.height());
        mMatrix.mapRect(rectF);
        return rectF;
    }


    private void showDebugInfo(Canvas canvas) {
        if (!showDebugInfo) {
            return;
        }
        if (points != null) {
            mPaint.setColor(Color.GREEN);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(points[0], points[1], 20, mPaint);
        }
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(focusX, focusY, 20, mPaint);


        if (pointsFocusBefore != null) {
            mPaint.setColor(Color.RED);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(pointsFocusBefore[0], pointsFocusBefore[1], 20, mPaint);
        }


    }
//


//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        // 获取画布原始大小
//        Log.d("onMeasure", "=========onMeasure========");
//        int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
//
//        int measuredHeight = MeasureSpec.getSize(heightMeasureSpec);
//
//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//
//
//        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//
//        // 计算缩放系数
//
//        if (totalRectF != null) {
//
//            float width = totalRectF.width();
//            float height = totalRectF.height();
//
//            float newWith = width;
//
//
////            switch (widthMode){
////
////                case MeasureSpec.EXACTLY:
////                    newWith = width;
////                    break;
////                default:
////                    newWith=defaultWidth;
////                    break;
////
////            }
//
////            scale = newWith / width;
//
//            float newHeight = height;
//
//
////            setMeasuredDimension((int) (newWith*userScale), (int) (newHeight*userScale));
//
//        }
//
//
//    }

    private void handlerTouch(float x, float y) {
        if (itemList != null) {
            for (VectorBean item : itemList) {
                if (item.isTouch((int) (x), (int) (y))) {
                    selectItem = item;
                    postInvalidate();
                    break;
                }
            }
        }
    }

    private float lastTouchX,lastTouchY=0f;


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        mScaleGestureDetector.onTouchEvent(event);
        gestureDetectorCompat.onTouchEvent(event);

//        // 添加 VelocityTracker
//        if (mVelocityTracker == null) {
//            mVelocityTracker = VelocityTracker.obtain();
//        }
//        mVelocityTracker.addMovement(event);
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//
//
//
//                lastTouchX = event.getX();
//                lastTouchY = event.getY();
//                Log.d("lastTouch","========"+lastTouchX+"========"+lastTouchX);
//                isDragging = false;
//                lastTouchTime = System.currentTimeMillis();
//                break;
//
//            case MotionEvent.ACTION_MOVE:
//                float dx = event.getX() - lastTouchX;
//                float dy = event.getY() - lastTouchY;
//                Log.d("isScaling","========"+isScaling+"========"+dy);
////                if (!isScaling && isDragging) {
////
////                    Log.d("distance","========"+dx+"========"+dy);
//////                    if (Math.abs(dx)>scaledTouchSlop||Math.abs(dy)>scaledTouchSlop){
////                        mMatrix.postTranslate(dx, dy);
////                        checkBoundaries();
////                           postInvalidateOnAnimation();();
//////                    }
////
////                }
//                lastTouchX = event.getX();
//                lastTouchY = event.getY();
//
//                // 更新滑动速度
//                long currentTime = System.currentTimeMillis();
//                long deltaTime = currentTime - lastTouchTime;
//                if (deltaTime > 0) {
////                    velocityX = dx / deltaTime;
////                    velocityY = dy / deltaTime;
//                    lastTouchTime = currentTime;
//                }
//
//                // 标记正在拖拽
//                isDragging = true;
//                break;
//
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_CANCEL:
//                isDragging = false;
//
//                if (!isScaling) {
//                    // 在此处实现飞速滑动判定和惯性滑动
//                    mVelocityTracker.computeCurrentVelocity(1000); // 计算当前的速度，单位为像素/秒
//                    float velocityX = mVelocityTracker.getXVelocity();
//                    float velocityY = mVelocityTracker.getYVelocity();
//                    if (isFlingFast(velocityX, velocityY)) {
//                        // 飞速滑动，启动惯性滑动
////                        startInertiaAnimation(velocityX,velocityY);
//                    }
//                }
//                // 释放 VelocityTracker
//                if (mVelocityTracker != null) {
//                    mVelocityTracker.recycle();
//                    mVelocityTracker = null;
//                }
//
//                isScaling = false;
//                break;
//        }
        return true;
    }

    private boolean isFlingFast(float velocityX, float velocityY) {
        final int fastThreshold = 3000; // 像素/秒
        return Math.abs(velocityX) > fastThreshold || Math.abs(velocityY) > fastThreshold;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//
        canvas.save();
        canvas.setMatrix(mMatrix);
        invertMatrix.reset();
        mMatrix.invert(invertMatrix);
        for (int i = 0; i < itemList.size(); i++) {
            VectorBean vectorBean = itemList.get(i);
            if (selectItem != vectorBean) {
                vectorBean.draw(mPaint, canvas, false);
            } else {
                selectItem.draw(mPaint, canvas, true);
            }
//            vectorBean.drawText(mPaint, canvas);
        }
        for (int i = 0; i < itemList.size(); i++) {
            VectorBean vectorBean = itemList.get(i);
            vectorBean.drawText(mPaint, canvas);
        }

//        showDebugInfo(canvas);
        canvas.restore();

    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                requestLayout();
                invalidate();
            }
        }
    };

    Thread thread = new Thread() {
        @Override
        public void run() {

            //dom解析xml
            InputStream inputStream = context.getResources().openRawResource(R.raw.chinahigh);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = null;
            try {
                builder = factory.newDocumentBuilder();
                Document document = builder.parse(inputStream);//解析输入流
                Element rootElement = document.getDocumentElement();
                NodeList items = rootElement.getElementsByTagName("path");
//                Log.d("MyMapView:", "集合大小=" + items.getLength());
                float left = -1, right=-1, top=-1, bottom = -1;
                for (int i = 0; i < items.getLength(); i++) {
                    int colorsIndex = i % 4;
                    Element element = (Element) items.item(i);
                    String pathData = element.getAttribute("android:pathData");

                    String name = element.getAttribute("android:name");

                    String fillColor = element.getAttribute("android:fillColor");
                    String strokeColor = element.getAttribute("android:strokeColor");
                    String strokeWidth = element.getAttribute("android:strokeWidth");
                    String center = element.getAttribute("android:center");
                    String[] split = center.split(",");
                    PointF point = new PointF();
                    point.x=Float.parseFloat(split[0]);
                    point.y=Float.parseFloat(split[1]);
                    @SuppressLint("RestrictedApi") Path path = PathParser.createPathFromPathData(pathData);
                    VectorBean provinceBean = new VectorBean(path,name,fillColor,strokeColor,strokeWidth,point);




                    RectF rect = new RectF();
                    path.computeBounds(rect, true);
                    left = left == -1 ? rect.left : Math.min(left, rect.left);
                    right = right == -1 ? rect.right : Math.max(right, rect.right);
                    top = top == -1 ? rect.top : Math.min(top, rect.top);
                    bottom = bottom == -1 ? rect.bottom : Math.max(bottom, rect.bottom);


                    itemList.add(provinceBean);
                }
                totalRectF = new RectF(left, top, right, bottom);
//                Log.d("totalRectF","==========="+totalRectF.width()+"------===="+totalRectF.height());
                handler.sendEmptyMessage(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


}


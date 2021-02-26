package com.gaocheng.baselibrary.widget.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.gaocheng.baselibrary.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 4/20 0020.
 */
public class SingleCakeView extends View {

    private Context ctx;
    private DecimalFormat format;
    private List<SingCakeBean> mList;
    private List<SingCakeBean> mCanvasList; //需要绘制的区域集合

    private Paint arcPaint;
    private Paint arcSecondPaint;
    private Paint linePaint;
    private Paint legendPaint;
    private TextPaint textPaint;
    private TextPaint legendTextPaint;


    private float centerX;
    private float centerY;
    private float legendX;
    private float legendY;
    private float radius;

    private float total;
    private float startAngle;
    private float textAngle;
    private float roundAngle = 360;
    private float legendHeight;

    private List<PointF[]> lineList; //一级 线的集合
    private List<PointF> textList; //1级文字 文字的区域集合


    public SingleCakeView(Context context) {
        this(context, null);
    }

    public SingleCakeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SingleCakeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.ctx = context;
        this.lineList = new ArrayList<>();
        this.textList = new ArrayList<>();
        this.mList = new ArrayList<>();
        this.mCanvasList = new ArrayList<>();
        this.format = new DecimalFormat("##0.00");

        this.arcPaint = new Paint();
        this.arcPaint.setAntiAlias(true);
        this.arcPaint.setDither(true);
        this.arcPaint.setStyle(Paint.Style.FILL);

        this.legendPaint = new Paint();
        this.legendPaint.setAntiAlias(true);
        this.legendPaint.setDither(true);
        this.legendPaint.setStyle(Paint.Style.FILL);

        this.arcSecondPaint = new Paint();
        this.arcSecondPaint.setAntiAlias(true);
        this.arcSecondPaint.setDither(true);
        this.arcSecondPaint.setStyle(Paint.Style.STROKE);

        this.linePaint = new Paint();
        this.linePaint.setAntiAlias(true);
        this.linePaint.setDither(true);
        this.linePaint.setStyle(Paint.Style.STROKE);
        this.linePaint.setStrokeWidth(dip2px(ctx, 1));
        this.linePaint.setColor(Color.parseColor("#FFFFFF"));

        this.textPaint = new TextPaint();
        this.textPaint.setAntiAlias(true);
        this.textPaint.setDither(true);
        this.textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint.setTextAlign(Paint.Align.CENTER);

        this.legendTextPaint = new TextPaint();
        this.legendTextPaint.setAntiAlias(true);
        this.legendTextPaint.setDither(true);
        this.legendTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        legendTextPaint.setTextAlign(Paint.Align.CENTER);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = w / 2; //  984/2
        centerY = h / 2; //  1625/2

        radius = Math.min(centerX, centerY) * 0.9f; //391.5
//        arcPaint.setStrokeWidth(radius / 3 * 2);   //setStrokeWidth = 261
        arcPaint.setStrokeWidth(1);

        //放在初始化方法里面无效 待考察
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(getResources().getDimension(R.dimen.chart_text_size));

        legendTextPaint.setColor(Color.BLACK);
        legendTextPaint.setTextSize(getResources().getDimension(R.dimen.chart_text_size));
        legendHeight = legendTextPaint.getTextSize();

        legendX = w - 150; //
        legendY = legendHeight; //
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        textList.clear();
        lineList.clear();
        mCanvasList.clear();

        float x = legendX;
        float y = legendY;
        for (int i = 0; i < mList.size(); i++) {
            SingCakeBean baseMessage = mList.get(i);
            legendPaint.setColor(baseMessage.color);
            canvas.drawCircle(x, y, dip2px(ctx, 3), legendPaint);
            canvas.drawText(baseMessage.content, x + 80, y + legendHeight / 2, legendTextPaint);
            y = y + legendHeight * 3 / 2;
        }
        if (total==0){
            return;
        }
        if (mList != null) {
            RectF mRectF = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);

            for (int i = 0; i < mList.size(); i++) {
                SingCakeBean baseMessage = mList.get(i);
                arcPaint.setColor(baseMessage.color);
                //偏移的角度
                float sweepAngle = baseMessage.percent / total * roundAngle;
                if (sweepAngle <= 0) {
                    continue;
                }
                canvas.drawArc(mRectF, startAngle, sweepAngle, true, arcPaint);
                lineList.add(getLinePointFs(startAngle));//获取直线 开始坐标 结束坐标
                textAngle = startAngle + sweepAngle / 2;

                PointF textPointF = getTextPointF(textAngle);
//                canvas.drawText(baseMessage.content, textPointF.x, textPointF.y, textPaint);
//                Paint.FontMetrics fm = textPaint.getFontMetrics();
//                canvas.drawText(format.format(baseMessage.percent * 100 / total) + "%", textPointF.x, textPointF.y + (fm.descent - fm.ascent), textPaint);

                textList.add(textPointF);   //获取需要绘制文本的区域
                mCanvasList.add(baseMessage); //获取绘制的数据
                startAngle += sweepAngle;
            }
        }


        // 绘制间隔空白线
        drawSpacingLine(canvas, lineList);
        // 绘制文字
        drawText(canvas);

    }


    private PointF getTextPointF(float angle) {
        float textPointX = (float) (centerX + radius / 2 * Math.cos(Math.toRadians(angle)));
        float textPointY = (float) (centerY + radius / 2 * Math.sin(Math.toRadians(angle)));
        return new PointF(textPointX, textPointY);
    }


    /**
     * 获取直线 开始坐标 结束坐标
     */
    private PointF[] getLinePointFs(float angle) {
        float stopX = (float) (centerX + (radius + arcPaint.getStrokeWidth()) * Math.cos(Math.toRadians(angle)));
        float stopY = (float) (centerY + (radius + arcPaint.getStrokeWidth()) * Math.sin(Math.toRadians(angle)));
        float startX = (float) (centerX + (radius - arcPaint.getStrokeWidth()) * Math.cos(Math.toRadians(angle)));
        float startY = (float) (centerY + (radius - arcPaint.getStrokeWidth()) * Math.sin(Math.toRadians(angle)));
        PointF startPoint = new PointF(centerX, centerY);
        PointF stopPoint = new PointF(stopX, stopY);
        return new PointF[]{startPoint, stopPoint};
    }


    /**
     * 画间隔线
     *
     * @param canvas
     */
    private void drawSpacingLine(Canvas canvas, List<PointF[]> pointFs) {
        for (PointF[] fp : pointFs) {
            canvas.drawLine(fp[0].x, fp[0].y, fp[1].x, fp[1].y, linePaint);
        }
    }


    private void drawText(Canvas canvas) {
        for (int i = 0; i < textList.size(); i++) {
            String text = mCanvasList.get(i).content ;
            canvas.drawText(text, textList.get(i).x, textList.get(i).y, textPaint);
            Paint.FontMetrics fm = textPaint.getFontMetrics();
            canvas.drawText(format.format(mCanvasList.get(i).percent * 100 / total) + "%", textList.get(i).x, textList.get(i).y + (fm.descent - fm.ascent), textPaint);
        }
    }


    public void setCakeData(List<SingCakeBean> mList) {
        total = 0;
        if (mList == null) {
            return;
        }
        for (int i = 0; i < mList.size(); i++) {
            total += mList.get(i).percent;
        }
        this.mList.clear();
        this.mList = mList;
        invalidate();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width;
        int height;
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.EXACTLY) {
            height = heightSpecSize;
            width = Math.min(heightSpecSize, Math.min(getScreenSize(ctx)[0], getScreenSize(ctx)[1]));
        } else if (widthSpecMode == MeasureSpec.EXACTLY && heightSpecMode == MeasureSpec.AT_MOST) {
            width = widthSpecSize;
            height = Math.min(widthSpecSize, Math.min(getScreenSize(ctx)[0], getScreenSize(ctx)[1]));
        } else if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            width = height = Math.min(getScreenSize(ctx)[0], getScreenSize(ctx)[1]);
        } else {
            width = widthSpecSize;
            height = heightSpecSize;
        }
        setMeasuredDimension(width, height);
    }

    public static int[] getScreenSize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(
                Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return new int[]{outMetrics.widthPixels, outMetrics.heightPixels};
    }

}

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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 4/20 0020.
 */
public class DoubleCakeView extends View {

    private Context ctx;
    private DecimalFormat format;
    private List<BaseMessage> mList;

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
    private float secondradius;
    private float total;
    private float startAngle;
    private float startSecondAngle;
    private float textAngle;
    private float roundAngle = 360;
    private float legendHeight;

    private List<PointF[]> lineList; //一级 线的集合
    private List<PointF> textList; //1级文字 文字的区域集合
    private List<PointF> secondTtextList; //1级文字 文字的区域集合
    private List<SecondMessage> secondTtext; //1级文字 文字的区域集合


    public DoubleCakeView(Context context) {
        this(context, null);
    }

    public DoubleCakeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DoubleCakeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.ctx = context;
        this.lineList = new ArrayList<>();
        this.textList = new ArrayList<>();
        this.secondTtextList = new ArrayList<>();
        this.secondTtext = new ArrayList<>();
        this.mList = new ArrayList<>();
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

        radius = Math.min(centerX, centerY) * 0.66f; //391.5
        secondradius = Math.min(centerX, centerY) - radius; //391.5
//        arcPaint.setStrokeWidth(radius / 3 * 2);   //setStrokeWidth = 261
        arcPaint.setStrokeWidth(1);
//        Log.e("Tag",radius+""+secondradius);
        arcSecondPaint.setStrokeWidth(secondradius);

        //放在初始化方法里面无效 待考察
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(radius / 7);

        legendTextPaint.setColor(Color.BLACK);
        legendTextPaint.setTextSize(radius / 7);
        legendHeight = legendTextPaint.getTextSize();

        legendX = w-150; //
        legendY = legendHeight; //
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        textList.clear();
        secondTtextList.clear();
        secondTtext.clear();
        lineList.clear();
//        lineList = new ArrayList<>();
//        textList = new ArrayList<>();

        if (mList != null) {
            RectF mRectF = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);

            for (int i = 0; i < mList.size(); i++) {
                BaseMessage baseMessage = mList.get(i);
                arcPaint.setColor(baseMessage.color);
                //偏移的角度
                float sweepAngle = baseMessage.percent / total * roundAngle;
                if (sweepAngle <= 0){
                    continue;
                }

                canvas.drawArc(mRectF, startAngle, sweepAngle, true, arcPaint);
                lineList.add(getLinePointFs(startAngle));//获取直线 开始坐标 结束坐标

                for (int j = 0;j<baseMessage.secondMessageList.size();j++){

                    RectF mRectF1 = new RectF(centerX - radius-secondradius/2,
                            centerY - radius-secondradius/2,
                            centerX + radius+secondradius/2,
                            centerY + radius+secondradius/2);
                    SecondMessage secondMessage = baseMessage.secondMessageList.get(j);
                    arcSecondPaint.setColor(secondMessage.color);
                    //画旁边的

                    float secondSweepAngle = secondMessage.percent/baseMessage.secondpercentTotal * sweepAngle;

                    if (secondSweepAngle<=0){
                        continue;
                    }
                    canvas.drawArc(mRectF1, startSecondAngle, secondSweepAngle, false, arcSecondPaint);
                    lineList.add(getSecondLinePointFs(startSecondAngle));


                    //添加 二级文字的位置
                    textAngle = startSecondAngle+secondSweepAngle/2;
//                    String text = secondMessage.content;
                    PointF textPointF = getSecondTextPointF(textAngle);
                    secondTtextList.add(textPointF);   //获取文本文本
                    secondTtext.add(secondMessage);   //获取文本文本

//                    canvas.drawText(text, textPointF.x, textPointF.y, textPaint);
//                    Paint.FontMetrics fm = textPaint.getFontMetrics();
//                    canvas.drawText(format.format(secondMessage.percent * 100 /total) + "%", textPointF.x, textPointF.y + (fm.descent - fm.ascent), textPaint);
                    startSecondAngle += secondSweepAngle;
                }

                textAngle = startAngle + sweepAngle / 2;
                textList.add(getTextPointF(textAngle));   //获取文本文本

                startAngle += sweepAngle;
                startSecondAngle = startAngle;
            }
        }
        float x = legendX;
        float y = legendY;
        for (int i = 0; i < mList.size() -1 ; i++) {
            BaseMessage baseMessage = mList.get(i);
            for (int j = 0;j<baseMessage.secondMessageList.size();j++){
                SecondMessage secondMessage = baseMessage.secondMessageList.get(j);
                legendPaint.setColor(secondMessage.color);
                canvas.drawCircle(x,y,dip2px(ctx, 3),legendPaint);
                canvas.drawText(secondMessage.content,x+80,y+legendHeight/2,legendTextPaint);
                y = y+legendHeight*3/2;
            }
        }

        // 绘制间隔空白线
        drawSpacingLine(canvas, lineList);
        // 绘制文字
        drawText(canvas);
        drawSeondText(canvas);

    }


    private PointF getTextPointF(float angle) {
        float textPointX = (float) (centerX + radius/2 * Math.cos(Math.toRadians(angle)));
        float textPointY = (float) (centerY + radius/2 * Math.sin(Math.toRadians(angle)));
        return new PointF(textPointX, textPointY);
    }

    /**
     * 获取文本文本
     *
     * @return
     */
    private PointF getSecondTextPointF(float angle) {

        float textPointX = (float) (centerX + (radius+secondradius/2) * Math.cos(Math.toRadians(angle)));
        float textPointY = (float) (centerY + (radius+secondradius/2) * Math.sin(Math.toRadians(angle)));
//        Log.e("getSecondTextPointF",(textPointX-centerX)+":"+(textPointY-centerY)+":"+angle);
        return new PointF(textPointX, textPointY);
    }
    /**
     * 获取直线 开始坐标 结束坐标
     */
    private PointF[] getLinePointFs(float angle) {
        float stopX = (float) (centerX + (radius + secondradius+ arcPaint.getStrokeWidth() ) * Math.cos(Math.toRadians(angle)));
        float stopY = (float) (centerY + (radius +secondradius+ arcPaint.getStrokeWidth() ) * Math.sin(Math.toRadians(angle)));
        float startX = (float) (centerX + (radius+ secondradius- arcPaint.getStrokeWidth() ) * Math.cos(Math.toRadians(angle)));
        float startY = (float) (centerY + (radius + secondradius- arcPaint.getStrokeWidth() ) * Math.sin(Math.toRadians(angle)));
        PointF startPoint = new PointF(centerX, centerY);
        PointF stopPoint = new PointF(stopX, stopY);
        return new PointF[]{startPoint, stopPoint};
    }

    private PointF[] getSecondLinePointFs(float angle) {
        float stopX = (float) (centerX + (radius + arcSecondPaint.getStrokeWidth() ) * Math.cos(Math.toRadians(angle)));
        float stopY = (float) (centerY + (radius + arcSecondPaint.getStrokeWidth() ) * Math.sin(Math.toRadians(angle)));
        float startX = (float) (centerX + (radius) * Math.cos(Math.toRadians(angle)));
        float startY = (float) (centerY + (radius) * Math.sin(Math.toRadians(angle)));
        PointF startPoint = new PointF(startX, startY);
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
            String text = mList.get(i).content;
            canvas.drawText(text, textList.get(i).x, textList.get(i).y, textPaint);
//            Paint.FontMetrics fm = textPaint.getFontMetrics();
//            canvas.drawText(format.format(mList.get(i).percent * 100 / total) + "%", textList.get(i).x, textList.get(i).y + (fm.descent - fm.ascent), textPaint);
        }
    }

    private void drawSeondText(Canvas canvas) {
        for (int i = 0; i < secondTtextList.size(); i++) {
            String text = secondTtext.get(i).content;
            canvas.drawText(text, secondTtextList.get(i).x, secondTtextList.get(i).y, textPaint);
            Paint.FontMetrics fm = textPaint.getFontMetrics();
            canvas.drawText(format.format(secondTtext.get(i).percent * 100 / total) + "%", secondTtextList.get(i).x, secondTtextList.get(i).y + (fm.descent - fm.ascent), textPaint);
        }
    }

    public void setCakeData(List<BaseMessage> mList) {
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

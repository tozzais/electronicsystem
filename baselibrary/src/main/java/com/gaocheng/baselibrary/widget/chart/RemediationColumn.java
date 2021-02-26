package com.gaocheng.baselibrary.widget.chart;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.gaocheng.baselibrary.R;

/**
 * Created by 32672 on 2019/1/21.
 */

public class RemediationColumn extends View {
    int width,height;

    Paint textPaint;
    Paint linePaint;
    private float textSize;
    private String tag = "";
    private int lenght;

    public RemediationColumn(Context context) {
        super(context);
        init();
    }



    public RemediationColumn(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initParams(context, attrs);
        init();
    }

    public RemediationColumn(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initParams(context, attrs);
        init();
    }

    private void initParams(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RemediationColumn);
        if (typedArray != null){

            tag = typedArray.getString(0);
//            Log.e("tag==",tag+"");
        }


    }

    public RemediationColumn(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        textPaint = new Paint();             // 创建画笔
        textPaint.setColor(Color.BLACK);           // 画笔颜色 - 黑色
        textPaint.setStyle(Paint.Style.FILL);    // 填充模式 - 描边
        textPaint.setStrokeWidth(2);
        textSize =  getResources().getDimension(R.dimen.remediation_column_text_size);
        textPaint.setTextSize(textSize);
        lenght = (int) textPaint.measureText(tag);
//        Log.e("lenght ",":lenght= :"+lenght); //144

        linePaint = new Paint();             // 创建画笔
        linePaint.setColor(Color.BLACK);           // 画笔颜色 - 黑色
        linePaint.setStyle(Paint.Style.STROKE);    // 填充模式 - 描边
        linePaint.setStrokeWidth(2);


    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Path path = new Path();
//        canvas.translate(10,10);

        path.moveTo(width/2 - lenght/2,textSize/2);
        path.lineTo(textSize/2,textSize/2);
        path.lineTo(textSize/2,height-textSize/2);
        path.lineTo(width-textSize/2,height-textSize/2);
        path.lineTo(width-textSize/2,textSize/2);
        path.lineTo(width/2+lenght/2,textSize/2);

//        Log.e("TAG",width+":::"+height); //525 210
//        Log.e("TAG",":textSize= :"+textSize); //36
        canvas.drawPath(path,linePaint);

        canvas.drawText(tag,width/2 - lenght/2,textSize,textPaint);

    }

    /**
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     * MeasureSpec.EXACTLY是精确尺寸 固定值 或者是 FILL_PARENT
     * MeasureSpec.AT_MOST是最大尺寸 WRAP_CONTENT
     * MeasureSpec.UNSPECIFIED是未指定尺寸，这种情况不多，一般都是父控件是AdapterView，通过measure方法传入的模式。
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


//        if (widthSpecMode == MeasureSpec.AT_MOST ) {
//            width = Math.min(heightSpecSize, Math.min(getScreenSize(ctx)[0], getScreenSize(ctx)[1]));
//        }  else {
//            width = widthSpecSize;
//        }
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(width, height);
    }




}

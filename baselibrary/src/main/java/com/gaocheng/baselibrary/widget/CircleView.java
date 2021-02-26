package com.gaocheng.baselibrary.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

import com.gaocheng.baselibrary.R;


public class CircleView extends View {

	/** 背景圆的画笔 */
	private Paint paint;
	/** 进度条圆的画笔 */
	private Paint paint1;
	/** 设置矩阵的坐标点 */
	private RectF rectF;

	/** 屏幕的高度 */
	private int width = 0;
	/** 园的半径 */
	private int circleRadius = 0;
	/** 园的y轴起始坐标 */
	private int circleStartY = 20;
	/** 园的y轴终点坐标 起始坐标加上园的半径*2 */
	private int circleEndy = 0;

	/** 初始进度 */
	private float currentPorcent = 0;
	/** 进度是多少 */
	private float maxPorcent = 0;
	
	/**满级回调*/
	public RestoreCirclr rc;
	/** 是否还原 */
	public boolean isRestore = false;
	
	/**我一般喜欢直接写3个构造方法，方便引用*/
	public CircleView(Context context) {
		super(context);
		init(context);
	}
	public CircleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}
	
	/**初始化画笔*/
	private void init(Context context){
		paint = new Paint();// 布局xml里面引用
		paint1 = new Paint();// 布局xml里面引用

		paint.setAntiAlias(true);// 设置抗锯齿
		paint.setColor(getResources().getColor(R.color.grayLine));
		paint.setStyle(Style.STROKE);// 设置圆心掏空
		paint.setStrokeWidth(dip2px(context, 10));
		// 设置画笔形状 圆形，需要先设置画笔样式 SYROKE 或者 FILL_AND_STROKE
		paint.setStrokeCap(Paint.Cap.ROUND);
		
		paint1.setAntiAlias(true);// 设置抗锯齿
		paint1.setColor(getResources().getColor(R.color.yellowText));
		paint1.setStyle(Style.STROKE);
		paint1.setStrokeWidth(dip2px(context, 10));
		paint1.setStrokeCap(Paint.Cap.ROUND);
		
		width = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
		circleRadius = width / 4;
		circleEndy = circleStartY + circleRadius * 2;
		
		rectF = new RectF(width / 2 - circleRadius, circleStartY, width / 2 + circleRadius, circleEndy);// 弧形
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// 第一个参数是圆的大小，根据矩阵来控制。第二个参数是在哪个点起始，已顺时针方向走，所以说90为正下方。0为最右边。第三个参数是圆的度数360为一圈
		canvas.drawArc(rectF, 90, 360, false, paint);
		//这里等级为4/1等级，所以是90
		canvas.drawArc(rectF, 90, currentPorcent, false, paint1);
		
		if (currentPorcent == 0) {
			handler.postDelayed(drawRunnable, 0);
		}
	}
	/**启动动画刷新界面*/
	public void invalidateView(){
		handler.postDelayed(drawRunnable, 0);
	}
	
	private Handler handler = new Handler();
	
	Runnable drawRunnable = new Runnable() {

		@Override
		public void run() {
			if (!isRestore) {//有经验时动画
				if (currentPorcent >= maxPorcent) {
					currentPorcent = maxPorcent;
					invalidate();
					//移除当前Runnable
					handler.removeCallbacks(drawRunnable);
				} else {
					currentPorcent += 5;//这里是动画速度，当前为5。可自己去调试经验值增长速度
					handler.postDelayed(drawRunnable, (long) (1300 / maxPorcent));
					invalidate();
				}
				if (currentPorcent == 360) {
					if (rc != null) {
						isRestore = rc.OnRestoreCirclr();
						handler.postDelayed(drawRunnable, 0);
					}
				}
			} else {//满级之后经验条动画返回0进度
				if (currentPorcent <= 0) {
					currentPorcent = 0;
					invalidate();

					handler.removeCallbacks(drawRunnable);
				} else {
					currentPorcent -= 3;//这里是动画速度，当前为3。可自己去调试经验值反0速度
					handler.postDelayed(drawRunnable, (long) (1300 / maxPorcent));
					invalidate();
				}
			}
		}
	};
	
	
	
	public boolean isRestore() {
		return isRestore;
	}
	public void setRestore(boolean isRestore) {
		this.isRestore = isRestore;
	}
	/** 设置等级进度，传入升级经验，以及当前经验  maxPorcent就是当前经验在升级经验占的百分比*/
	public void setCirclePlan(int max, int current) {
		maxPorcent = (int) (((float)360 / (float)max) * current);
	}
	
	/**传入dp，返回px*/
	public float dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (float) (dpValue * scale + 0.5f);
	}	

	/** 设置园线的颜色 */
	public void setCircleColor(int color) {
		paint.setColor(color);
	}

	/** 设置进度线的颜色 */
	public void setCirclePlanColor(int color) {
		paint1.setColor(color);
	}

	public void setRc(RestoreCirclr rc) {
		this.rc = rc;
	}
	
	public interface RestoreCirclr {
		public boolean OnRestoreCirclr();
	}

}

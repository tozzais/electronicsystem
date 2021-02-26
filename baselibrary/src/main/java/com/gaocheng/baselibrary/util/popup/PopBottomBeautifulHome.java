package com.gaocheng.baselibrary.util.popup;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.gaocheng.baselibrary.R;
import com.gaocheng.baselibrary.bean.net.RainInfromUpdataLoadData;
import com.ipd.east.jumpboxlibrary.kankan.wheel.widget.OnWheelChangedListener;
import com.ipd.east.jumpboxlibrary.kankan.wheel.widget.WheelView;
import com.ipd.east.jumpboxlibrary.kankan.wheel.widget.adapters.AbstractWheelTextAdapter;

import java.util.ArrayList;
import java.util.List;


public class PopBottomBeautifulHome implements OnWheelChangedListener {


	private static PopBottomBeautifulHome cityUtils;

	public static PopBottomBeautifulHome getInstance() {
		if (cityUtils == null) {
			synchronized (PopBottomBeautifulHome.class) {
				if (cityUtils == null) {
					cityUtils = new PopBottomBeautifulHome();
				}
			}
		}
		return cityUtils;
	}

	private WheelView mViewProvince;
	private TextView mBtnConfirm;
	private Context context;
	private CityWheelAdapter provinceAdapter;
	private Dialog cityDialog;




	public void showSelectDialog(final Context context , List<RainInfromUpdataLoadData.BeautifulHomeBean> mList, final onSelectCityFinishListener listener) {
		this.context = context;
		View view = View.inflate(context, R.layout.pop_bottom_one_wheel, null);
		mViewProvince =  view.findViewById(R.id.wv_country);


		mBtnConfirm = view.findViewById(R.id.tv_commit);
		TextView tv_cancel = view.findViewById(R.id.tv_cancel);
		mBtnConfirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				cityDialog.dismiss();
				if (listener != null) {
					listener.onFinish(mList.get(mViewProvince.getCurrentItem()));
				}
			}
		});
		tv_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				cityDialog.dismiss();
			}
		});

		setUpListener();
		setUpData(mList);

		cityDialog = new Dialog(context,R.style.transparentFrameWindowStyle);
		cityDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		cityDialog.setContentView(view);
		Window window = cityDialog.getWindow();
		window.setWindowAnimations(R.style.PopupAnimation);

		WindowManager.LayoutParams wl = window.getAttributes();
		wl.x = 0;
		wl.y = ((Activity) context).getWindowManager().getDefaultDisplay().getHeight();
		wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
		wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

		cityDialog.getWindow().setAttributes(wl);
//		cityDialog.onWindowAttributesChanged(wl);

		cityDialog.setCanceledOnTouchOutside(true);
		cityDialog.show();
	}

	private void setUpData(List<RainInfromUpdataLoadData.BeautifulHomeBean> mList) {
		List<String> list = new ArrayList<>();
		for (RainInfromUpdataLoadData.BeautifulHomeBean bean:mList){
			list.add(bean.Name);
		}
		provinceAdapter = new CityWheelAdapter(context, list);
		mViewProvince.setViewAdapter(provinceAdapter);
		mViewProvince.setVisibleItems(7);
	}

	private void setUpListener() {
		mViewProvince.addChangingListener(this);
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {

	}



	class CityWheelAdapter extends AbstractWheelTextAdapter {
		List<String> list;

		public CityWheelAdapter(Context context, List<String> list) {
			super(context);
			this.list = list;
		}

		@Override
		public int getItemsCount() {
			return list == null ? 0 : list.size();
		}


		@Override
		protected CharSequence getItemText(int index) {
			return list.get(index);
		}

	}

	public interface onSelectCityFinishListener {
		public void onFinish(RainInfromUpdataLoadData.BeautifulHomeBean s);
	}

}

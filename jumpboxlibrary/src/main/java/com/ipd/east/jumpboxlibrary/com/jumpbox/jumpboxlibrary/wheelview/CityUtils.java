package com.ipd.east.jumpboxlibrary.com.jumpbox.jumpboxlibrary.wheelview;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.ipd.east.jumpboxlibrary.R;
import com.ipd.east.jumpboxlibrary.kankan.wheel.widget.OnWheelChangedListener;
import com.ipd.east.jumpboxlibrary.kankan.wheel.widget.WheelView;
import com.ipd.east.jumpboxlibrary.kankan.wheel.widget.adapters.ArrayWheelAdapter;


public class CityUtils implements OnWheelChangedListener {
	private static CityUtils cityUtils;

	public static CityUtils getInstance() {
		if (cityUtils == null) {
			synchronized (CityUtils.class) {
				if (cityUtils == null) {
					cityUtils = new CityUtils();
				}
			}
		}

		return cityUtils;
	}

	private WheelView mViewProvince;
	private WheelView mViewCity;
	private WheelView mViewDistrict;
	private TextView mBtnConfirm;
	private Context context;

	public void showSelectDialog(final Context context) {
		this.context = context;
		View view = View.inflate(context, R.layout.city_choose_dialog, null);
		mViewProvince = (WheelView) view.findViewById(R.id.wv_country);
		mViewCity = (WheelView) view.findViewById(R.id.wv_city);
		mViewDistrict = (WheelView) view.findViewById(R.id.wv_ccity);
		mBtnConfirm = (TextView) view.findViewById(R.id.tv_commit);
		mBtnConfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(context, mCurrentProviceName + "," + mCurrentCityName + "," + mCurrentDistrictName + ","
						+ mCurrentZipCode, Toast.LENGTH_SHORT).show();
			}
		});
		setUpListener();
		setUpData();

		Dialog cityDialog = new Dialog(context);
		cityDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		cityDialog.setContentView(view);
		Window window = cityDialog.getWindow();
		// 设置显示动画
		window.setWindowAnimations(R.style.PopupAnimation);
		WindowManager.LayoutParams wl = window.getAttributes();
		wl.x = 0;
		wl.y = ((Activity) context).getWindowManager().getDefaultDisplay().getHeight();
		// 以下这两句是为了保证按钮可以水平满屏
		wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
		wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		// 设置显示位置
		cityDialog.onWindowAttributesChanged(wl);
		// 设置点击外围解散
		cityDialog.setCanceledOnTouchOutside(true);
		cityDialog.show();
	}

	private void setUpData() {
		initProvinceDatas(context);
		mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(context, mProvinceDatas));

		mViewProvince.setVisibleItems(7);
		mViewCity.setVisibleItems(7);
		mViewDistrict.setVisibleItems(7);
		updateCities();
		updateAreas();
	}

	private void setUpListener() {
		mViewProvince.addChangingListener(this);
		mViewCity.addChangingListener(this);
		mViewDistrict.addChangingListener(this);
		// mBtnConfirm.setOnClickListener(this);
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		if (wheel == mViewProvince) {
			updateCities();
		} else if (wheel == mViewCity) {
			updateAreas();
		} else if (wheel == mViewDistrict) {
			mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
			mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
		}
	}

	private void updateAreas() {
		int pCurrent = mViewCity.getCurrentItem();
		mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
		String[] areas = mDistrictDatasMap.get(mCurrentCityName);

		if (areas == null) {
			areas = new String[] { "" };
		}
		mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(context, areas));
		mViewDistrict.setCurrentItem(0);
	}

	private void updateCities() {
		int pCurrent = mViewProvince.getCurrentItem();
		mCurrentProviceName = mProvinceDatas[pCurrent];
		String[] cities = mCitisDatasMap.get(mCurrentProviceName);
		if (cities == null) {
			cities = new String[] { "" };
		}
		mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(context, cities));
		mViewCity.setCurrentItem(0);
		updateAreas();
	}

	protected String[] mProvinceDatas;
	protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
	protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

	protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();

	protected String mCurrentProviceName;
	protected String mCurrentCityName;
	protected String mCurrentDistrictName = "";
	protected String mCurrentZipCode = "";

	protected void initProvinceDatas(Context context) {
		List<ProvinceModel> provinceList = null;
		AssetManager asset = context.getAssets();
		try {
			InputStream input = asset.open("province_data.xml");
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser parser = spf.newSAXParser();
			XmlParserHandler handler = new XmlParserHandler();
			parser.parse(input, handler);
			input.close();
			provinceList = handler.getDataList();
			System.out.println(provinceList);
			if (provinceList != null && !provinceList.isEmpty()) {
				mCurrentProviceName = provinceList.get(0).getName();
				List<CityModel> cityList = provinceList.get(0).getCityList();
				if (cityList != null && !cityList.isEmpty()) {
					mCurrentCityName = cityList.get(0).getName();
					List<DistrictModel> districtList = cityList.get(0).getDistrictList();
					mCurrentDistrictName = districtList.get(0).getName();
					mCurrentZipCode = districtList.get(0).getZipcode();
				}
			}
			// */
			mProvinceDatas = new String[provinceList.size()];
			for (int i = 0; i < provinceList.size(); i++) {
				mProvinceDatas[i] = provinceList.get(i).getName();
				List<CityModel> cityList = provinceList.get(i).getCityList();
				String[] cityNames = new String[cityList.size()];
				for (int j = 0; j < cityList.size(); j++) {
					cityNames[j] = cityList.get(j).getName();
					List<DistrictModel> districtList = cityList.get(j).getDistrictList();
					String[] distrinctNameArray = new String[districtList.size()];
					DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
					for (int k = 0; k < districtList.size(); k++) {
						DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(),
								districtList.get(k).getZipcode());
						mZipcodeDatasMap.put(districtList.get(k).getName(), districtList.get(k).getZipcode());
						distrinctArray[k] = districtModel;
						distrinctNameArray[k] = districtModel.getName();
					}
					mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
				}
				mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {

		}
	}
}

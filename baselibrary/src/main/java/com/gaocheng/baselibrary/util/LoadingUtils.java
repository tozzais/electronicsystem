package com.gaocheng.baselibrary.util;


import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;

import com.gaocheng.baselibrary.R;


public class LoadingUtils {
	private static LoadingDialog dialog;

	public static void show(Context context,String dislogTip) {
		if (dialog != null && dialog.isShowing()) {
			try{
				dialog.dismiss();
			}catch (Exception e){
				e.printStackTrace();
			}finally {
				dialog = null;
			}
		}
		if (!TextUtils.isEmpty(dislogTip)){
			dialog = new LoadingDialog(context,
					R.style.ActionSheetDialogStyle, R.layout.view_tips_loading,dislogTip);
		}else {
			dialog = new LoadingDialog(context,
					R.style.ActionSheetDialogStyle, R.layout.view_tips_loading);
		}
		dialog.setCanceledOnTouchOutside(false);
		try {
			dialog.show();
		}catch (Exception e){
			e.printStackTrace();
		}

	}
	public static void show(Context context){
		show(context,"");
	}


	public static void dismiss() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
		dialog = null;
	}

	public static void setOnKeyListener(DialogInterface.OnKeyListener onKeyListener){
		if (dialog != null){
			dialog.setOnKeyListener(onKeyListener);
		}
	}
}

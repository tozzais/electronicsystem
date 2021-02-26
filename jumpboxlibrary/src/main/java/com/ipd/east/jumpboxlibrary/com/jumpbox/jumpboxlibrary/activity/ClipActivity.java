package com.ipd.east.jumpboxlibrary.com.jumpbox.jumpboxlibrary.activity;

import java.io.File;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.ipd.east.jumpboxlibrary.R;
import com.ipd.east.jumpboxlibrary.com.jumpbox.jumpboxlibrary.utils.MyBitmapUtils;

/**
 * 裁剪照片
 * 
 * 
 */
public class ClipActivity extends Activity implements OnClickListener {
	private ClipImageLayout mClipImageLayout;
	private ProgressDialog loadingDialog;
	String pathfile;
	private String path;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clipimage);
		mClipImageLayout = (ClipImageLayout) findViewById(R.id.id_clipImageLayout);
		// 这步必须要加
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		loadingDialog = new ProgressDialog(this);
		loadingDialog.setTitle("请稍后...");
		path = getIntent().getStringExtra("path");
		if (TextUtils.isEmpty(path) || !(new File(path).exists())) {
			Toast.makeText(this, "图片加载失败", Toast.LENGTH_SHORT).show();
			return;
		}
		Bitmap bitmap = MyBitmapUtils.convertToBitmap(path, 600, 600);
		if (bitmap == null) {
			Toast.makeText(this, "图片加载失败", Toast.LENGTH_SHORT).show();
			return;
		}
		mClipImageLayout.setBitmap(bitmap);

		TextView rl_botom_clip = (TextView) findViewById(R.id.rl_botom_clip);
		rl_botom_clip.setOnClickListener(this);
		TextView rl_cancel = (TextView) findViewById(R.id.rl_cancel);
		rl_cancel.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.rl_botom_clip) {
			loadingDialog.show();
			new Thread(new Runnable() {
				@Override
				public void run() {
					Bitmap bitmap = mClipImageLayout.clip();
					pathfile = Environment.getExternalStorageDirectory() + "/ClipHeadPhoto/cache/"
							+ System.currentTimeMillis() + ".png";
					MyBitmapUtils.savePhotoToSDCard(bitmap, path);
					loadingDialog.dismiss();
					Intent intent = new Intent();
					intent.putExtra("path", path);
					setResult(RESULT_OK, intent);
					// info.setMemberpic(path);
					finish();
				}
			}).start();
		} else if (v.getId() == R.id.rl_cancel) {
			finish();
		}
	}

}

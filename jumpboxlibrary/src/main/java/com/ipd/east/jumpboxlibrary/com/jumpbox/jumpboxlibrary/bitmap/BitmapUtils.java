package com.ipd.east.jumpboxlibrary.com.jumpbox.jumpboxlibrary.bitmap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.Display;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitmapUtils {

	private static BitmapUtils instance;
	private static String cacheDirPath = Environment
			.getExternalStorageDirectory() + "/east";
	private static File cacheDir = new File(cacheDirPath);

	private BitmapUtils() {
		if (!cacheDir.exists()) {
			cacheDir.mkdirs();
		}
	}

	public static BitmapUtils getInstance() {
		if (instance == null) {
			instance = new BitmapUtils();
		}
		return instance;
	}

	public Bitmap reSizeBitmap(Activity a, File file) {
		Display display = a.getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		int height = display.getHeight();

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(),
				options);
		options.inJustDecodeBounds = false;
		int bitmapHeight = options.outHeight;
		int bitmapWidth = options.outWidth;

		if (bitmapHeight > height || bitmapWidth > width) {
			int scaleX = bitmapWidth / width;
			int scaleY = bitmapHeight / height;
			if (scaleX > scaleY) {// 按照水平方向的比例缩放
				options.inSampleSize = scaleX;
			} else {// 按照竖直方向的比例缩放
				options.inSampleSize = scaleY;
			}
		} else {// 如果图片比手机屏幕小 不去缩放了.
			options.inSampleSize = 1;
		}
		return bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(),
				options);
	}

	private boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri
				.getAuthority());
	}

	private boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri
				.getAuthority());
	}

	private boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri
				.getAuthority());
	}

	private boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri
				.getAuthority());
	}

	@SuppressLint("NewApi")
	public String getPath(final Context context, final Uri uri) {

		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/"
							+ split[1];
				}

			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] { split[1] };

				return getDataColumn(context, contentUri, selection,
						selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {

			// Return the remote address
			if (isGooglePhotosUri(uri))
				return uri.getLastPathSegment();

			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	public Bitmap reSizeBitmap(Activity a, Uri uri) {
		Display display = a.getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		int height = display.getHeight();

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		String path = getPath(a, uri);
		Bitmap bitmap = BitmapFactory.decodeFile(path, options);

		options.inJustDecodeBounds = false;
		int bitmapHeight = options.outHeight;
		int bitmapWidth = options.outWidth;

		if (bitmapHeight > height || bitmapWidth > width) {
			int scaleX = bitmapWidth / width;
			int scaleY = bitmapHeight / height;
			if (scaleX > scaleY) {// 按照水平方向的比例缩放
				options.inSampleSize = scaleX;
			} else {// 按照竖直方向的比例缩放
				options.inSampleSize = scaleY;
			}
		} else {// 如果图片比手机屏幕小 不去缩放了.
			options.inSampleSize = 1;
		}
		return bitmap = BitmapFactory.decodeFile(path, options);
	}

	private String getDataColumn(Context context, Uri uri, String selection,
								 String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };

		try {
			cursor = context.getContentResolver().query(uri, projection,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/***
	 * 图片压缩
	 */
	public void CompactPic(final Activity activity, final String path,
						   final CallBack callback) {
		if (Environment.getExternalStorageState() == Environment.MEDIA_UNMOUNTED) {
			return;
		}

		new AsyncTask<String, Integer, String>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);

				if (callback != null) {
					callback.resultPath(result);
				}

			}

			@Override
			protected String doInBackground(String... params) {
				Bitmap bitmap = reSizeBitmap(activity, new File(path));
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				bitmap.compress(CompressFormat.PNG, 100, bos);
				int options = 100;
				int size = bos.toByteArray().length / 1024;
				while (size > 100 && options > 0) {
					bos.reset();
					if (size > 500) {
						options -= 40;
					} else if (size > 400) {
						options -= 20;
					} else {
						options -= 10;
					}
					bitmap.compress(CompressFormat.JPEG, options, bos);
					size = bos.toByteArray().length / 1024;
				}

				String filePath = cacheDirPath + "/"
						+ System.currentTimeMillis() + ".png";
				FileOutputStream fos = null;
				try {
					fos = new FileOutputStream(filePath);
					bos.writeTo(fos);
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				} finally {
					if (fos != null) {
						try {
							fos.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (bos != null) {
						try {
							bos.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				return filePath;
			}

		}.execute();

	}

	public interface CallBack {
		void resultPath(String path);
	}

	public void deleteFile(String path) {
		File file = new File(path);
		file.delete();
	}

	/**
	 * 转圆角bitmap
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap,float roundPx) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	/**
	 *
	 * @param path
	 * @param roundPx  圆角的角度
     * @return
     */
	public static Bitmap getRoundedCornerBitmap(String path,float roundPx){
		return getRoundedCornerBitmap(BitmapFactory.decodeFile(path),roundPx);
	}

	public int getExifOrientation(String filepath) {
		int degree = 0;
		ExifInterface exif = null;
		try {
			exif = new ExifInterface(filepath);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		if (exif != null) {
			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
			if (orientation != -1) {
				switch(orientation) {
					case ExifInterface.ORIENTATION_ROTATE_90:
						degree = 90;
						break;
					case ExifInterface.ORIENTATION_ROTATE_180:
						degree = 180;
						break;
					case ExifInterface.ORIENTATION_ROTATE_270:
						degree = 270;
						break;
				}
			}
		}
		return degree;
	}

	public Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
		if (degrees == 0 || null == bitmap) {
			return bitmap;
		}
		Matrix matrix = new Matrix();
		matrix.setRotate(degrees, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
		Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		if (null != bitmap) {
			bitmap.recycle();
		}
		return bmp;
	}

}

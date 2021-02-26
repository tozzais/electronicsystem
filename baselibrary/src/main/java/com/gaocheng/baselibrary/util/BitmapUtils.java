package com.gaocheng.baselibrary.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
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
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class BitmapUtils {

	private static BitmapUtils instance;
	private static String cacheDirPath = Environment
			.getExternalStorageDirectory() + "/rain/image";
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
	public void deleteCacheImage(){
        File file = new File(cacheDirPath);
        deleteAllFiles(file);

    }
	private void deleteAllFiles(File root) {
		File files[] = root.listFiles();
		if (files != null)
			for (File f : files) {
				if (f.isDirectory()) { // 判断是否为文件夹
					deleteAllFiles(f);
					try {
						f.delete();
					} catch (Exception e) {
					}
				} else {
					if (f.exists()) { // 判断是否存在
						deleteAllFiles(f);
						try {
							f.delete();
						} catch (Exception e) {
						}
					}
				}
			}

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
	public void CompactPic(final Activity activity, final String path,final int finalSize,
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
				Bitmap bitmap  = BitmapFactory.decodeFile(path);
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				bitmap.compress(CompressFormat.JPEG, 100, bos);
				int options = 100;
				int size = bos.toByteArray().length / 1024;
				while (size > finalSize && options > 0) {
					bos.reset();
					if (size > 500) {
						options -= 40;
					} else if (size > 400) {
						options -= 20;
					} else {
						options -= 10;
					}
//					Log.e("-options-","options = "+options);
					if (options<=0){
						bitmap.compress(CompressFormat.JPEG, 0, bos);
					}else{
						bitmap.compress(CompressFormat.JPEG, options, bos);
					}
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

	/***
	 * 图片压缩
	 */
	public void CompactPic(final Activity activity, final String path,
						   final CallBack callback) {
		CompactPic(activity,path,400,callback);

	}

	public interface CallBack {
		public void resultPath(String path);
	}

	public void deleteFile(String path) {
		File file = new File(path);
		file.delete();
	}

	/**
	 * 保存bitmap到本地
	 *
	 * @param context
	 * @param mBitmap
	 * @return
	 */
	public String saveBitmap(Context context, Bitmap mBitmap) {
		File filePic;
		try {
			String filePath = cacheDirPath + "/"
					+ System.currentTimeMillis() + ".png";
			filePic = new File(filePath);
			if (!filePic.exists()) {
				filePic.getParentFile().mkdirs();
				filePic.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(filePic);
			mBitmap.compress(CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		return filePic.getAbsolutePath();
	}

	public String savaPic(String netPath)  {
		byte[] btImg = getImageFromNetByUrl(netPath);
		if(null != btImg && btImg.length > 0){
			String fileName = cacheDirPath + "/"
					+ System.currentTimeMillis() + ".png";
			writeImageToDisk(btImg, fileName);
			return fileName;
		}

		return "";
	}

	/**
	 * 将图片写入到磁盘
	 * @param img 图片数据流
	 * @param fileName 文件保存时的名称
	 */
	public static void writeImageToDisk(byte[] img, String fileName){
		try {
			File file = new File(fileName);
			FileOutputStream fops = new FileOutputStream(file);
			fops.write(img);
			fops.flush();
			fops.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 根据地址获得数据的字节流
	 * @param strUrl 网络连接地址
	 * @return
	 */
	public static byte[] getImageFromNetByUrl(String strUrl){
		try {
			URL url = new URL(strUrl);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5 * 1000);
			InputStream inStream = conn.getInputStream();//通过输入流获取图片数据
			byte[] btImg = readInputStream(inStream);//得到图片的二进制数据
			return btImg;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 从输入流中获取数据
	 * @param inStream 输入流
	 * @return
	 * @throws Exception
	 */
	public static byte[] readInputStream(InputStream inStream) throws Exception{
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while( (len=inStream.read(buffer)) != -1 ){
			outStream.write(buffer, 0, len);
		}
		inStream.close();
		return outStream.toByteArray();
	}

    /**
     * 读取图片属性：旋转的角度
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public  int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转图片
     * @param angle
     * @param bitmap
     * @return Bitmap
     */
    public  Bitmap rotaingImageView(int angle , Bitmap bitmap) {
        //旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        System.out.println("angle2=" + angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;

    }

}

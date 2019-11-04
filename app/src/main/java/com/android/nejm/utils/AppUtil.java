package com.android.nejm.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.widget.TextView;
import android.widget.Toast;

import com.android.nejm.MyApplication;
import com.android.nejm.R;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

public class AppUtil {

	public static List<String[]> mProcessList = null;

	public static void installApk(Activity activity, File file) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		activity.startActivity(intent);
	}

	public static void uninstallApk(Activity activity, String packageName) {
		Intent intent = new Intent(Intent.ACTION_DELETE);
		Uri packageURI = Uri.parse("package:" + packageName);
		intent.setData(packageURI);
		activity.startActivity(intent);
	}

	public static boolean isServiceRunning(Context context, String className) {
		boolean isRunning = false;
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> servicesList = activityManager
				.getRunningServices(Integer.MAX_VALUE);
		Iterator<RunningServiceInfo> l = servicesList.iterator();
		while (l.hasNext()) {
			RunningServiceInfo si = (RunningServiceInfo) l.next();
			if (className.equals(si.service.getClassName())) {
				isRunning = true;
			}
		}
		return isRunning;
	}

	public static boolean stopRunningService(Context context, String className) {
		Intent intent_service = null;
		boolean ret = false;
		try {
			intent_service = new Intent(context, Class.forName(className));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (intent_service != null) {
			ret = context.stopService(intent_service);
		}
		return ret;
	}

	public static int getNumCores() {
		try {
			// Get directory containing CPU info
			File dir = new File("/sys/devices/system/cpu/");
			// Filter to only list the devices we care about
			File[] files = dir.listFiles(new FileFilter() {

				@Override
				public boolean accept(File pathname) {
					// Check if filename is "cpu", followed by a single digit
					// number
					if (Pattern.matches("cpu[0-9]", pathname.getName())) {
						return true;
					}
					return false;
				}

			});
			// Return the number of cores (virtual CPU devices)
			return files.length;
		} catch (Exception e) {
			e.printStackTrace();
			return 1;
		}
	}

	public static boolean isNetworkAvailable(Context context) {
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

	public static boolean isGpsEnabled(Context context) {
		LocationManager lm = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}

	public static boolean isMobile(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null
				&& activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
			return true;
		}
		return false;
	}

	public static boolean importDatabase(Context context, String dbName,
			int rawRes) {
		int buffer_size = 1024;
		InputStream is = null;
		FileOutputStream fos = null;
		boolean flag = false;

		try {
			String dbPath = "/data/data/" + context.getPackageName()
					+ "/databases/" + dbName;
			File dbfile = new File(dbPath);

			if (!dbfile.exists()) {

				if (!dbfile.getParentFile().exists()) {
					dbfile.getParentFile().mkdirs();
				}
				dbfile.createNewFile();
				is = context.getResources().openRawResource(rawRes);
				fos = new FileOutputStream(dbfile);
				byte[] buffer = new byte[buffer_size];
				int count = 0;
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}
				fos.flush();
			}
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (Exception e) {
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (Exception e) {
				}
			}
		}
		return flag;
	}

	public static DisplayMetrics getDisplayMetrics(Context context) {
		Resources mResources;
		if (context == null) {
			mResources = Resources.getSystem();

		} else {
			mResources = context.getResources();
		}
		// DisplayMetrics{density=1.5, width=480, height=854, scaledDensity=1.5,
		// xdpi=160.421, ydpi=159.497}
		// DisplayMetrics{density=2.0, width=720, height=1280,
		// scaledDensity=2.0, xdpi=160.42105, ydpi=160.15764}
		DisplayMetrics mDisplayMetrics = mResources.getDisplayMetrics();
		return mDisplayMetrics;
	}

	public static PackageInfo getPackageInfo(Context context) {
		PackageInfo info = null;
		try {
			String packageName = context.getPackageName();
			info = context.getPackageManager().getPackageInfo(packageName,
					PackageManager.GET_ACTIVITIES);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return info;
	}

	public static String getBaseSavePath(Context context) {
		String sdStatus = Environment.getExternalStorageState();
		if (sdStatus.equals(Environment.MEDIA_MOUNTED)) {
			return Environment.getExternalStorageDirectory().toString();
		} else {
			return context.getFilesDir().getAbsolutePath();
		}
	}


	public static void shareToCircel(final Context context,final String title, final String content,
									 final String url,PlatformActionListener listener) {
		Platform WeChatPlat = ShareSDK.getPlatform(WechatMoments.NAME);
		if(WeChatPlat==null||!WeChatPlat.isClientValid()){
			Toast.makeText(context, "你没有安装微信", Toast.LENGTH_SHORT).show();
			return;
		}
		Platform.ShareParams shareParams=new Platform.ShareParams();
		shareParams.setTitle(title);
		shareParams.setText(content);
		shareParams.setUrl(url);
		shareParams.setShareType(Platform.SHARE_WEBPAGE);
		Bitmap thumb = BitmapFactory.decodeResource(context.getResources(),
				R.mipmap.app_icon);
		shareParams.setImageData(thumb);

		WeChatPlat.setPlatformActionListener(listener);
		WeChatPlat.share(shareParams);

	}

	public static void shareToCircel(final Context context,final String title, final String content,
									 final String url, final String imgUrl, PlatformActionListener listener) {
		Platform WeChatPlat = ShareSDK.getPlatform(WechatMoments.NAME);
		if(WeChatPlat==null||!WeChatPlat.isClientValid()){
			Toast.makeText(context, "你没有安装微信", Toast.LENGTH_SHORT).show();
			return;
		}
		Platform.ShareParams shareParams=new Platform.ShareParams();
		shareParams.setTitle(title);
		shareParams.setText(content);
		shareParams.setUrl(url);
		shareParams.setShareType(Platform.SHARE_WEBPAGE);
		shareParams.setImageUrl(imgUrl);

		WeChatPlat.setPlatformActionListener(listener);
		WeChatPlat.share(shareParams);

	}

	public static void shareToFriend(final Context context,final String title, final String content,
									 final String url,PlatformActionListener listener) {

		Platform WeChatPlat = ShareSDK.getPlatform(Wechat.NAME);
		if(WeChatPlat==null||!WeChatPlat.isClientValid()){
			Toast.makeText(context, "你没有安装微信", Toast.LENGTH_SHORT).show();
			return;
		}
		Platform.ShareParams shareParams=new Platform.ShareParams();

		shareParams.setTitle(title);
		shareParams.setText(content);
		shareParams.setUrl(url);
		shareParams.setShareType(Platform.SHARE_WEBPAGE);
		Bitmap thumb = BitmapFactory.decodeResource(context.getResources(),
				R.mipmap.app_icon);
		shareParams.setImageData(thumb);
		WeChatPlat.setPlatformActionListener(listener);
		WeChatPlat.share(shareParams);



	}

	public static void shareToFriend(final Context context,final String title, final String content,
									 final String url, final String imgUrl, PlatformActionListener listener) {

		Platform WeChatPlat = ShareSDK.getPlatform(Wechat.NAME);
		if(WeChatPlat==null||!WeChatPlat.isClientValid()){
			Toast.makeText(context, "你没有安装微信", Toast.LENGTH_SHORT).show();
			return;
		}
		Platform.ShareParams shareParams=new Platform.ShareParams();

		shareParams.setTitle(title);
		shareParams.setText(content);
		shareParams.setUrl(url);
		shareParams.setShareType(Platform.SHARE_WEBPAGE);
		shareParams.setImageUrl(imgUrl);
		WeChatPlat.setPlatformActionListener(listener);
		WeChatPlat.share(shareParams);



	}

	public static void shareToSinaWeibo(final Context context,final String title, final String content,
									 final String url,PlatformActionListener listener) {

		Platform WeChatPlat = ShareSDK.getPlatform(SinaWeibo.NAME);
		if(WeChatPlat==null||!WeChatPlat.isClientValid()){
			Toast.makeText(context, "你没有安装新浪微博", Toast.LENGTH_SHORT).show();
			return;
		}
		Platform.ShareParams shareParams=new Platform.ShareParams();

		shareParams.setTitle(title);
		shareParams.setText(content);
		shareParams.setUrl(url);
		shareParams.setShareType(Platform.SHARE_WEBPAGE);
		Bitmap thumb = BitmapFactory.decodeResource(context.getResources(),
				R.mipmap.app_icon);
		shareParams.setImageData(thumb);
		WeChatPlat.setPlatformActionListener(listener);
		WeChatPlat.share(shareParams);



	}
	public static void shareToSinaWeibo(final Context context,final String title, final String content,
									 final String url, final String imgUrl, PlatformActionListener listener) {

		Platform WeChatPlat = ShareSDK.getPlatform(SinaWeibo.NAME);
		if(WeChatPlat==null||!WeChatPlat.isClientValid()){
			Toast.makeText(context, "你没有安装新浪微博", Toast.LENGTH_SHORT).show();
			return;
		}
		Platform.ShareParams shareParams=new Platform.ShareParams();

		shareParams.setTitle(title);
		shareParams.setText(content);
		shareParams.setUrl(url);
		shareParams.setShareType(Platform.SHARE_WEBPAGE);
		shareParams.setImageUrl(imgUrl);
		WeChatPlat.setPlatformActionListener(listener);
		WeChatPlat.share(shareParams);



	}


	/**
	 * 是否运行在前台
	 * @return
	 */
	public static boolean isAppOnForeground() {
		// Returns a list of application processes that are running on the
		// device

		ActivityManager activityManager = (ActivityManager) MyApplication.getApplication().getSystemService(Context.ACTIVITY_SERVICE);
		String packageName =  MyApplication.getApplication().getPackageName();

		List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		if (appProcesses == null)
			return false;

		for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
			// The name of the process that this object is associated with.
			if (appProcess.processName.equals(packageName)
					&& appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}
		return false;
	}

	public static String getTextContent(TextView textView) {
		return textView.getText().toString().trim();
	}
}

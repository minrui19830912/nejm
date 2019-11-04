package com.android.nejm.widgets;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.nejm.R;
import com.facebook.drawee.view.SimpleDraweeView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImageIndicatorView extends RelativeLayout {

	private ViewPager viewPager;

	private LinearLayout indicateLayout;

	// private Button leftButton;

	// private Button rightButton;

	private List<View> viewList = new ArrayList<View>();

	private Handler refreshHandler;

	private OnItemChangeListener onItemChangeListener;

	private OnItemClickListener onItemClickListener;

	private int totelCount = 0;

	private int currentIndex = 0;

	public static final int INDICATE_ARROW_ROUND_STYLE = 0;

	public static final int INDICATE_USERGUIDE_STYLE = 1;

	private int indicatorStyle = INDICATE_ARROW_ROUND_STYLE;

	private long refreshTime = 0l;
	private MyPagerAdapter mMyPagerAdapter;

	private boolean broadcastEnable = false;

	private static final long DEFAULT_STARTMILS = 2 * 1000;

	private static final long DEFAULT_INTEVALMILS = 3 * 1000;

	private long startMils = DEFAULT_STARTMILS;

	private long intevalMils = DEFAULT_INTEVALMILS;

	private final static int RIGHT = 0;

	private final static int LEFT = 1;

	private int direction = RIGHT;

	private static final int DEFAULT_TIMES = -1;

	private int broadcastTimes = DEFAULT_TIMES;

	private int timesCount = 0;

	// private int mBannerHeight = 0;

	// private int mBannerWidth = 0;

	private boolean mCanLoop = false;
	private boolean mShowIndicator = true;

	private int mIndicatorIconFocusDrawable = R.mipmap.image_indicator_focus;
	private int mIndicatorIconNorDrawable = R.mipmap.image_indicator;

	public interface OnBannerItemClickListener {
		public void onBannerItemClick(int position);
	}

	private OnBannerItemClickListener mOnBannerItemClickListener;

	public interface OnItemChangeListener {
		void onPosition(int position, int totalCount);
	}

	public interface OnItemClickListener {
		void OnItemClick(View view, int position);
	}

	public ImageIndicatorView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.init(context);
	}

	public ImageIndicatorView(Context context) {
		super(context);
		this.init(context);
	}

	private void init(Context context) {
		LayoutInflater.from(context).inflate(R.layout.image_indicator_layout,
				this);
		// mBannerHeight = getResources().getDrawable(R.drawable.banner03)
		// .getIntrinsicHeight();
		// mBannerWidth = getResources().getDisplayMetrics().widthPixels;
		// mBannerWidth = getResources().getDisplayMetrics().widthPixels;
		// getResources().getDrawable(R.drawable.banner01)
		// .getIntrinsicWidth();
		this.viewPager = (ViewPager) findViewById(R.id.view_pager);
		this.indicateLayout = (LinearLayout) findViewById(R.id.indicater_layout);

		this.viewPager.setOnPageChangeListener(new PageChangeListener());

		this.refreshHandler = new ScrollIndicateHandler(ImageIndicatorView.this);
		mMyPagerAdapter = new MyPagerAdapter();
		viewPager.setAdapter(mMyPagerAdapter);

	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub

		return super.onInterceptTouchEvent(ev);
	}

	public ViewPager getViewPager() {
		return viewPager;
	}

	public int getCurrentIndex() {
		return this.currentIndex;
	}

	public int getTotalCount() {
		return this.totelCount;
	}

	public long getRefreshTime() {
		return this.refreshTime;
	}

	public void addViewItem(View view) {
		final int position = viewList.size();
		view.setOnClickListener(new ItemClickListener(position));
		this.viewList.add(view);
	}

	private class ItemClickListener implements OnClickListener {
		private int position = 0;

		public ItemClickListener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View view) {
			if (onItemClickListener != null) {
				removeMsg();
				onItemClickListener.OnItemClick(view, position);
			}
		}
	}

	public void setupLayoutByDrawable(final Integer resArray[]) {
		if (resArray == null)
			throw new NullPointerException();

		this.setupLayoutByDrawable(Arrays.asList(resArray));
	}

	public void setupLayoutByDrawable(final List<Integer> resList) {
		if (resList == null)
			throw new NullPointerException();

		final int len = resList.size();
		if (len > 0) {
			for (int index = 0; index < len; index++) {
				final View pageItem = new ImageView(getContext());
				pageItem.setBackgroundResource(resList.get(index));
				addViewItem(pageItem);
			}
		}
	}

	public void setupLayoutByImageUrl(final List<String> resList, int defaultId) {
		if (resList == null)
			throw new NullPointerException();
		if (viewList != null && viewList.size() > 0) {
			viewList.clear();

		}

		final int len = resList.size();
		if (len > 0) {
			for (int index = 0; index < len; index++) {
				// final NetworkImageView pageItem = new NetworkImageView(
				// getContext());
				final SimpleDraweeView pageItem = new SimpleDraweeView(
						getContext());
				LayoutParams params = new LayoutParams(
						LayoutParams.MATCH_PARENT,
						LayoutParams.MATCH_PARENT);

				pageItem.setScaleType(ScaleType.CENTER_CROP);
				pageItem.setLayoutParams(params);
				// String imgUrl = resList.get(index) != null ?
				// RequestUrl.BASEURL
				// + resList.get(index) : null;
				String imgUrl = resList.get(index);
				pageItem.setImageURI(imgUrl);
				pageItem.setBackgroundColor(0);
				// pageItem.setImageURI(Uri.parse(resList.get(index)));
				addViewItem(pageItem);
			}
		}
		mMyPagerAdapter.notifyDataSetChanged();

	}
	public void setupLayoutByImageUrl(final List<String> resList, int defaultId, int width, int height, ScaleType scaleType) {
		if (resList == null)
			throw new NullPointerException();
		if (viewList != null && viewList.size() > 0) {
			viewList.clear();

		}

		final int len = resList.size();
		if (len > 0) {
			for (int index = 0; index < len; index++) {
				// final NetworkImageView pageItem = new NetworkImageView(
				// getContext());
				final SimpleDraweeView pageItem = new SimpleDraweeView(
						getContext());
				LayoutParams params = new LayoutParams(
						width,
						height);

				pageItem.setScaleType(scaleType);
				pageItem.setLayoutParams(params);
				// String imgUrl = resList.get(index) != null ?
				// RequestUrl.BASEURL
				// + resList.get(index) : null;
				String imgUrl = resList.get(index);
				pageItem.setImageURI(imgUrl);
				// pageItem.setImageURI(Uri.parse(resList.get(index)));
				addViewItem(pageItem);
			}
		}
		mMyPagerAdapter.notifyDataSetChanged();

	}
//	public void setupLayoutByImageUrlEx(final List<String> resList,
//										int defaultId) {
//		if (resList == null)
//			throw new NullPointerException();
//		if (viewList != null && viewList.size() > 0) {
//			viewList.clear();
//
//		}
//
//		final int len = resList.size();
//		if (len > 0) {
//			for (int index = 0; index < len; index++) {
//				// final NetworkImageView pageItem = new NetworkImageView(
//				// getContext());
//				final GestureImageView pageItem = new GestureImageView(
//						getContext());
//				LayoutParams params = new LayoutParams(
//						LayoutParams.MATCH_PARENT,
//						LayoutParams.WRAP_CONTENT);
//				pageItem.setScaleType(ScaleType.CENTER_INSIDE);
//				pageItem.setLayoutParams(params);
//				String imgUrl = resList.get(index);
//				VolleyHelper.loadImageByNetworkImageView(imgUrl, pageItem,
//						defaultId);
//				addViewItem(pageItem);
//			}
//		}
//
//		mMyPagerAdapter.notifyDataSetChanged();
//	}

//	public void setupLayoutByImagePath(final List<String> resList,
//										int defaultId) {
//		if (resList == null)
//			throw new NullPointerException();
//		if (viewList != null && viewList.size() > 0) {
//			viewList.clear();
//
//		}
//
//		final int len = resList.size();
//		if (len > 0) {
//			for (int index = 0; index < len; index++) {
//				// final NetworkImageView pageItem = new NetworkImageView(
//				// getContext());
//				final LocalGestureImageView pageItem = new LocalGestureImageView(
//						getContext());
//				LayoutParams params = new LayoutParams(
//						LayoutParams.MATCH_PARENT,
//						LayoutParams.WRAP_CONTENT);
//				pageItem.setScaleType(ScaleType.CENTER_INSIDE);
//				pageItem.setLayoutParams(params);
//				String imgUrl = resList.get(index);
//				pageItem.setImageURI(Uri.parse(imgUrl));
//				addViewItem(pageItem);
//			}
//		}
//
//		mMyPagerAdapter.notifyDataSetChanged();
//	}

	public void setCurrentItem(int index) {
		this.currentIndex = index;
	}

	public void setIndicateStyle(int style) {
		this.indicatorStyle = style;
	}

	public void setOnItemChangeListener(
			OnItemChangeListener onItemChangeListener) {
		if (onItemChangeListener == null) {
			throw new NullPointerException();
		}
		this.onItemChangeListener = onItemChangeListener;
	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

	public void setIndicatorVisible(boolean isShown) {
		mShowIndicator = isShown;
	}

	public void show() {
		this.totelCount = viewList.size();
		// final LayoutParams params = (LayoutParams) indicateLayout
		// .getLayoutParams();
		// if (INDICATE_USERGUIDE_STYLE == this.indicatorStyle) {// 操作指引
		// params.bottomMargin = 45;
		// }
		// this.indicateLayout.setLayoutParams(params);

		// 初始化指示器
		indicateLayout.removeAllViews();
		if (this.totelCount > 1 && mShowIndicator) {
			for (int index = 0; index < this.totelCount; index++) {
				final View indicater = new ImageView(getContext());
				this.indicateLayout.addView(indicater, index);
			}
		}

		this.refreshHandler.sendEmptyMessage(currentIndex);
		// 为ViewPager配置数据

		this.viewPager.setCurrentItem(currentIndex, false);
	}
	public void show(int show_width,int show_height ) {
		this.totelCount = viewList.size();
		// final LayoutParams params = (LayoutParams) indicateLayout
		// .getLayoutParams();
		// if (INDICATE_USERGUIDE_STYLE == this.indicatorStyle) {// 操作指引
		// params.bottomMargin = 45;
		// }
		// this.indicateLayout.setLayoutParams(params);

		// 初始化指示器
		indicateLayout.removeAllViews();
		if (this.totelCount > 1 && mShowIndicator) {
			for (int index = 0; index < this.totelCount; index++) {
				final View indicater = new ImageView(getContext());

				this.indicateLayout.addView(indicater, index);
				ViewGroup.LayoutParams parm = indicater.getLayoutParams();
				parm.width=show_width;
				parm.height =show_height;
				indicater.setLayoutParams(parm);
			}
		}

		this.refreshHandler.sendEmptyMessage(currentIndex);
		// 为ViewPager配置数据

		this.viewPager.setCurrentItem(currentIndex, false);
	}
	/**
	 * 页面变更监听
	 */
	private class PageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int index) {
			currentIndex = index;
			refreshHandler.sendEmptyMessage(index);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}
	}

	protected void refreshIndicateView() {
		this.refreshTime = System.currentTimeMillis();

		if (totelCount > 1 && mShowIndicator) {
			for (int index = 0; index < totelCount; index++) {
				final ImageView imageView = (ImageView) this.indicateLayout
						.getChildAt(index);
				if (this.currentIndex == index) {

					imageView
							.setBackgroundResource(mIndicatorIconFocusDrawable);
				} else {
					imageView.setBackgroundResource(mIndicatorIconNorDrawable);
				}
				LinearLayout.LayoutParams parms = (LinearLayout.LayoutParams) imageView
						.getLayoutParams();
				parms.leftMargin = (int) (10 * getResources()
						.getDisplayMetrics().density);
				imageView.setLayoutParams(parms);
			}
		}

		if (this.onItemChangeListener != null) {
			try {
				this.onItemChangeListener.onPosition(this.currentIndex,
						this.totelCount);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void setIndicatorIconDrawable(int focusDrawable, int norDrawable) {
		mIndicatorIconFocusDrawable = focusDrawable;
		mIndicatorIconNorDrawable = norDrawable;
	}

	private static class ScrollIndicateHandler extends Handler {
		private final WeakReference<ImageIndicatorView> scrollIndicateViewRef;

		public ScrollIndicateHandler(ImageIndicatorView scrollIndicateView) {
			this.scrollIndicateViewRef = new WeakReference<ImageIndicatorView>(
					scrollIndicateView);

		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Log.e("minrui","handleMessage");
			ImageIndicatorView scrollIndicateView = scrollIndicateViewRef.get();
			if (scrollIndicateView != null) {
				scrollIndicateView.refreshIndicateView();
			}
		}
	}

	private class MyPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return viewList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getItemPosition(Object object) {
			// return super.getItemPosition(object);
			// return POSITION_NONE;
			if (viewList.indexOf(object) < 0) {
				return POSITION_NONE;
			}
			return super.getItemPosition(object);
		}

		@Override
		public void destroyItem(View container, int position, Object object) {

			((ViewPager) container).removeView((View) object);

		}

		@Override
		public Object instantiateItem(View container, int position) {
			((ViewPager) container).addView(viewList.get(position));
			return viewList.get(position);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {

		}

		@Override
		public void finishUpdate(View arg0) {

		}
	}

	public void setBroadcastTimeIntevel(long startMils, long intevelMils) {
		this.startMils = startMils;
		this.intevalMils = intevelMils;
	}

	public void setBroadcastEnable(boolean flag) {
		this.broadcastEnable = flag;
	}

	public void setBroadCastTimes(int times) {
		this.broadcastTimes = times;
	}

	public void loop() {
		mCanLoop = true;
		if (broadcastEnable) {
			broadcastHandler.sendEmptyMessageDelayed(0, this.startMils);
		}
	}

	public void setIndicateLayoutParams(LayoutParams params) {
		if (indicateLayout != null) {
			indicateLayout.setLayoutParams(params);
		}
	}

	public LayoutParams getIndicateLayoutParams() {
		return indicateLayout == null ? null
				: (LayoutParams) indicateLayout
						.getLayoutParams();
	}

	private Handler broadcastHandler = new Handler() {
		public void handleMessage(Message msg) {
			int what = msg.what;
			if (what == 0) {

				if (broadcastEnable) {
					if (System.currentTimeMillis() - getRefreshTime() < 2 * 1000) {

						broadcastHandler
								.sendEmptyMessageDelayed(
										0,
										2 * 1000 - (System.currentTimeMillis() - getRefreshTime()));
						return;
					}
					// if ((broadcastTimes != DEFAULT_TIMES)
					// && (timesCount > broadcastTimes)) {// 循环次数用完
					//
					// return;
					// }

					if (direction == RIGHT) {// roll right
						if (getCurrentIndex() < getTotalCount()) {
							if (getCurrentIndex() == getTotalCount() - 1) {
								timesCount++;
								direction = LEFT;
							} else {

								getViewPager().setCurrentItem(
										getCurrentIndex() + 1, true);
							}
						}
					} else {// roll left
						if (getCurrentIndex() >= 0) {
							if (getCurrentIndex() == 0) {
								direction = RIGHT;
							} else {

								getViewPager().setCurrentItem(
										getCurrentIndex() - 1, true);
							}
						}
					}

					broadcastHandler.sendEmptyMessageDelayed(0, intevalMils);
				}
			}
		}
	};

	public void tapItem(MotionEvent e) {
		if (mOnBannerItemClickListener != null) {
			mOnBannerItemClickListener.onBannerItemClick(currentIndex);
		}
	}

	// public boolean isFling(MotionEvent ev) {
	//
	// return viewPager.dispatchTouchEvent(ev);
	// }

	public void fling(boolean left) {
		if (left) {
			if (currentIndex > 0) {
				currentIndex--;
				viewPager.setCurrentItem(currentIndex);
			}
		} else {
			currentIndex++;
			viewPager.setCurrentItem(currentIndex);
		}

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			broadcastEnable = false;
			break;
		case MotionEvent.ACTION_UP:
			broadcastEnable = true;
			if (mCanLoop) {
				loop();
			}
			break;
		}

		return super.dispatchTouchEvent(ev);
	}

	// private static class BroadcastHandler extends Handler {
	//
	// private final WeakReference<AutoPlayManager> autoBrocastManagerRef;
	//
	// public BroadcastHandler(AutoPlayManager autoBrocastManager) {
	// this.autoBrocastManagerRef = new WeakReference<AutoPlayManager>(
	// autoBrocastManager);
	// }
	//
	// @Override
	// public void handleMessage(Message msg) {
	// super.handleMessage(msg);
	// AutoPlayManager autoBrocastManager = autoBrocastManagerRef.get();
	//
	// if (autoBrocastManager != null) {
	// autoBrocastManager.handleMessage(msg);
	// }
	// }
	// }

	private void removeMsg(){
		refreshHandler.removeCallbacksAndMessages(null);
		broadcastHandler.removeCallbacksAndMessages(null);
		loop();
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		refreshHandler.removeCallbacksAndMessages(null);
		broadcastHandler.removeCallbacksAndMessages(null);
	}
}

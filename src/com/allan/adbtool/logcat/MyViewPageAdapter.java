package com.allan.adbtool.logcat;

import java.util.List;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;
public class MyViewPageAdapter extends PagerAdapter {
	private List<RelativeLayout> mListViews;

	public MyViewPageAdapter(List<RelativeLayout> listViews) {
		this.mListViews = listViews;
	}

	@Override
	public int getCount() {
		return mListViews.size();
	}

	/**
	 * 从指定的position创建page
	 * 
	 * @param container
	 *            ViewPager容器
	 * @param position
	 *            The page position to be instantiated.
	 * @return 返回指定position的page，这里不需要是一个view，也可以是其他的视图容器.
	 */
	@Override
	public Object instantiateItem(View collection, int position) {

		((ViewPager) collection).addView(mListViews.get(position), 0);

		return mListViews.get(position);
	}

	@Override
	public void destroyItem(View collection, int position, Object view) {
		((ViewPager) collection).removeView(mListViews.get(position));
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == (object);
	}

	@Override
	public void finishUpdate(View arg0) {
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

}
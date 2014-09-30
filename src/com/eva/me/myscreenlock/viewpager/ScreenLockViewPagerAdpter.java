package com.eva.me.myscreenlock.viewpager;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageView;

public class ScreenLockViewPagerAdpter extends PagerAdapter {
	private Context mContext = null;
	private int size = 0;
	private Bitmap[] bmps = null;  
    private Drawable[] drawables = new Drawable[size];  
    private List<ImageView> ivList;
    private LayoutParams params = new Gallery.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
	
	public ScreenLockViewPagerAdpter(Context context) {
		this.mContext = context;
	}
	
	public ScreenLockViewPagerAdpter(Context context, Drawable[] drawables, int size ) {
		this.mContext = context;
		this.size = size;
		this.drawables = drawables;
		ivList = new ArrayList<ImageView>();
		for (int i = 0; i < drawables.length; i++) {
			ImageView iv = new ImageView(context);
			iv.setLayoutParams(params);
			iv.setImageDrawable(drawables[i]);
			ivList.add(iv);
		}
	}
	
	public ScreenLockViewPagerAdpter(Context context, Bitmap[] bmps, int size) {
		this.mContext = context;
		this.size = size;
		this.bmps = bmps;
		ivList = new ArrayList<ImageView>();
		for (int i = 0; i < bmps.length; i++) {
			ImageView iv = new ImageView(context);
			iv.setLayoutParams(params);
			iv.setImageBitmap(bmps[i]);
			ivList.add(iv);
		}
	}
	
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
	
	@Override
	public int getCount() {
		return this.size;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position,
			Object object) {
		container.removeView(ivList.get(position));
		
//		super.destroyItem(container, position, object);
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		ImageView iv = ivList.get(position);  
		container.addView(iv);
        return iv; 
//		container.addView(viewList.get(position));
//		return viewList.get(position);
	}
	
	@Override
	public int getItemPosition(Object object) {
		return super.getItemPosition(object);
	}
}

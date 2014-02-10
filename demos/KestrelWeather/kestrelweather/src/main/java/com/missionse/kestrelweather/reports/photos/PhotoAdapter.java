package com.missionse.kestrelweather.reports.photos;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.missionse.kestrelweather.R;

public class PhotoAdapter extends BaseAdapter {
	private Context mContext;

	private Integer[] mImageIds = {
			R.drawable.ic_launcher,
			R.drawable.camera
	};

	public PhotoAdapter(final Context context) {
		mContext = context;
	}
	@Override
	public int getCount() {
		return mImageIds.length;
	}

	@Override
	public Object getItem(int position) {
		return mImageIds[position];
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		ImageView imageView;
		if (view == null) {
			imageView = new ImageView(mContext);
			imageView.setLayoutParams(new GridView.LayoutParams(
					(int) mContext.getResources().getDimension(R.dimen.photo_width),
					(int) mContext.getResources().getDimension(R.dimen.photo_height)));
			imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
		} else {
			imageView = (ImageView) view;
		}

		imageView.setImageResource(mImageIds[position]);
		return imageView;
	}
}

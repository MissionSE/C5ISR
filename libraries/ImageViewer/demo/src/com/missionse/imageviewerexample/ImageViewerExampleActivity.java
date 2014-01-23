package com.missionse.imageviewerexample;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.Menu;

import com.missionse.imageviewer.ImageFragmentFactory;
import com.missionse.imageviewer.ImagePagerAdapter;

/**
 * Provides an activity that is an example for how to use the image viewer library.
 */
public class ImageViewerExampleActivity extends Activity {

	private Fragment mImageFragment;
	private ImagePagerAdapter mImagePager;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_viewer_example);

		mImagePager = new ImagePagerAdapter(getFragmentManager());
		mImagePager.addImage(R.drawable.mselogo);
		mImagePager.addImage(R.drawable.wmd);

		mImageFragment = ImageFragmentFactory.createImageFragment(mImagePager);
		getFragmentManager().beginTransaction().replace(R.id.content_frame, mImageFragment).commit();
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		getMenuInflater().inflate(R.menu.image_viewer_example, menu);
		return true;
	}
}

package com.missionse.imageviewerexample;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.Menu;

import com.missionse.imageviewer.ImageFragmentFactory;
import com.missionse.imageviewer.ImagePagerAdapter;

public class ImageViewerExampleActivity extends Activity {

	private Fragment imageFragment;
	private ImagePagerAdapter imagePager;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_viewer_example);

		imagePager = new ImagePagerAdapter(getFragmentManager());
		imagePager.addImage(R.drawable.mselogo);
		imagePager.addImage(R.drawable.wmd);

		imageFragment = ImageFragmentFactory.createImageFragment(imagePager);
		getFragmentManager().beginTransaction().replace(R.id.content_frame, imageFragment).commit();
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.image_viewer_example, menu);
		return true;
	}
}

package com.missionse.basefragmentexample;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.missionse.basefragmentexample.imageviewer.ImageFragment;
import com.missionse.basefragmentexample.modelviewer.ModelViewerFragment;
import com.missionse.basefragmentexample.modelviewer.ObjectLoadedListener;
import com.missionse.basefragmentexample.videoviewer.VideoFragment;

public class MainActivity extends Activity implements ObjectLoadedListener {

	private ImageFragment imageFragment;
	private VideoFragment videoFragment;
	private ModelViewerFragment modelFragment;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		imageFragment = new ImageFragment();
		videoFragment = new VideoFragment();
		modelFragment = new ModelViewerFragment();
		modelFragment.registerObjectLoadedListener(this);

		showImage();
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_image:
				showImage();
				return true;
			case R.id.action_video:
				showVideo();
				return true;
			case R.id.action_model:
				showModel();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	public void showImage() {
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.content, imageFragment).commit();
	}

	public void showVideo() {
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.content, videoFragment).commit();
	}

	public void showModel() {
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.content, modelFragment).commit();
	}

	@Override
	public void onObjectLoaded() {
		modelFragment.getAnimator().scaleTo(4.0f, 500);
		modelFragment.getAnimator().rotateTo(-45f, 45f, 0f, 500);
	}
}

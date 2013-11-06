package com.missionse.videoviewerexample;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.missionse.videoviewer.VideoFragment;
import com.missionse.videoviewer.VideoFragmentFactory;

public class VideoViewerExampleActivity extends Activity {

	private VideoFragment videoFragment;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_viewer_example);

		videoFragment = VideoFragmentFactory.createVideoFragment(R.raw.security_video);
		getFragmentManager().beginTransaction().replace(R.id.content_frame, videoFragment).commit();
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.video_viewer_example, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		if (videoFragment != null) {
			switch (item.getItemId()) {
				case R.id.action_play:
					videoFragment.start();
					return true;
				case R.id.action_stop:
					videoFragment.stop();
					return true;
				case R.id.action_pause:
					videoFragment.pause();
					return true;
				case R.id.action_resume:
					videoFragment.resume();
					return true;
			}
		}

		return super.onOptionsItemSelected(item);
	}
}

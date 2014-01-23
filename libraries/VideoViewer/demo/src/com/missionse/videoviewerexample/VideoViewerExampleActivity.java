package com.missionse.videoviewerexample;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.missionse.videoviewer.VideoFragment;
import com.missionse.videoviewer.VideoFragmentFactory;

/**
 * Provides an activity that is an example for how to use the video viewer library.
 */
public class VideoViewerExampleActivity extends Activity {
	private VideoFragment mVideoFragment;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_viewer_example);

		mVideoFragment = VideoFragmentFactory.createVideoFragment(R.raw.security_video);
		getFragmentManager().beginTransaction().replace(R.id.content_frame, mVideoFragment).commit();
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		getMenuInflater().inflate(R.menu.video_viewer_example, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		if (mVideoFragment != null) {
			switch (item.getItemId()) {
				case R.id.action_play:
					mVideoFragment.start();
					return true;
				case R.id.action_stop:
					mVideoFragment.stop();
					return true;
				case R.id.action_pause:
					mVideoFragment.pause();
					return true;
				case R.id.action_resume:
					mVideoFragment.resume();
					return true;
				default:
					break;
			}
		}

		return super.onOptionsItemSelected(item);
	}
}

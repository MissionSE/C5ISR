package com.missionse.imageviewerexample;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.ClipData;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.missionse.imageviewer.ImageFragmentFactory;
import com.missionse.imageviewer.UriImagePagerAdapter;

/**
 * Provides an activity that is an example for how to use the image viewer library.
 */
public class ImageViewerExampleActivity extends Activity {
	public static final int ADD_IMAGE_REQUEST = 10;

	UriImagePagerAdapter mAdapter;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_viewer_example);

		mAdapter = new UriImagePagerAdapter(getFragmentManager());

		Fragment imageFragment = ImageFragmentFactory.createImageFragment(mAdapter);
		getFragmentManager().beginTransaction().replace(R.id.content_frame, imageFragment).commit();
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		getMenuInflater().inflate(R.menu.image_viewer_example, menu);
		return true;
	}

	@Override
	@TargetApi(Build.VERSION_CODES.KITKAT)
	public boolean onOptionsItemSelected(final MenuItem item) {
		if (item.getItemId() == R.id.action_add) {
			Intent intent;
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
				intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
				intent.addCategory(Intent.CATEGORY_OPENABLE);
			} else {
				intent = new Intent(Intent.ACTION_GET_CONTENT);
			}

			intent.setType("image/*");
			intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
			startActivityForResult(intent, ADD_IMAGE_REQUEST);

			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onActivityResult(final int requestCode, final int resultCode, final Intent resultData) {
		super.onActivityResult(requestCode, resultCode, resultData);

		if (requestCode == ADD_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
			if (resultData != null) {
				if (resultData.getData() != null) {
					mAdapter.addImage(resultData.getData());
					mAdapter.notifyDataSetChanged();
				} else {
					ClipData clipData = resultData.getClipData();
					if (clipData != null) {
						for (int index = 0; index < clipData.getItemCount(); ++index) {
							ClipData.Item item = clipData.getItemAt(index);
							if (item != null) {
								mAdapter.addImage(item.getUri());
								mAdapter.notifyDataSetChanged();
							}
						}
					}
				}
			}
		}
	}
}

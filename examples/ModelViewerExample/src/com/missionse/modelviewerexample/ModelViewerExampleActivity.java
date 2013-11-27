package com.missionse.modelviewerexample;

import java.util.HashMap;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.missionse.modelviewer.ModelAnimationController;
import com.missionse.modelviewer.ModelController;
import com.missionse.modelviewer.ModelViewerFragment;
import com.missionse.modelviewer.ModelViewerFragmentFactory;
import com.missionse.modelviewer.ObjectLoadedListener;
import com.missionse.modelviewer.ObjectPickedListener;

/**
 * Provides an activity that is an example of how to use the ModelViewer library.
 */
public class ModelViewerExampleActivity extends Activity implements ObjectPickedListener, ObjectLoadedListener {
	private ModelViewerFragment mFragment;
	private ModelController mController;
	private ModelAnimationController mAnimator;
	private Menu mOptionsMenu;
	private TextView mObjectListText;

	private HashMap<String, Integer> mDefaultColors;

	private static final int HIGHLIGHT_COLOR = Color.BLUE;
	private static final int ROTATION_DURATION = 4000;
	private static final int ANIMATION_DURATION = 250;

	/**
	 * Constructor.
	 */
	public ModelViewerExampleActivity() {
		mDefaultColors = new HashMap<String, Integer>();
	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_model_viewer_example);

		mFragment = ModelViewerFragmentFactory.createObjModelFragment(R.raw.multiobjects_obj);
		mFragment.registerObjectPickedListener(this);
		mFragment.registerObjectLoadedListener(this);

		getFragmentManager().beginTransaction().replace(R.id.content_frame, mFragment).commit();

		mObjectListText = (TextView) findViewById(R.id.object_list);
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu)	{
		mOptionsMenu = menu;
		getMenuInflater().inflate(R.menu.model_viewer_example, mOptionsMenu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		if (mFragment != null && mController != null && mAnimator != null) {
			switch (item.getItemId()) {
				case R.id.action_rotate:
					if (mAnimator.isRotating()) {
						mAnimator.stopRotation();
					} else {
						mAnimator.startXRotation(ROTATION_DURATION);
					}
					item.setChecked(mAnimator.isRotating());
					return true;
				case R.id.action_lock:
					if (mController.isTranslationLocked()) {
						mController.unlockTranslation();
					} else {
						mController.lockTranslation();
					}
					item.setChecked(mController.isTranslationLocked());
					return true;
				case R.id.action_center:
					mAnimator.translateTo(0f, 0f, 0f, ANIMATION_DURATION);
					return true;
				case R.id.action_reset:
					mAnimator.rotateTo(0f, 0f, 0f, ANIMATION_DURATION);
					mAnimator.scaleTo(1f, ANIMATION_DURATION);
					mAnimator.translateTo(0f, 0f, 0f, ANIMATION_DURATION);
					mOptionsMenu.findItem(R.id.action_rotate).setChecked(false);
					return true;
				default:
					return super.onOptionsItemSelected(item);
			}
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void objectPicked(final String objectName) {
		int objectColor = mController.getAmbientColor(objectName);

		if (!mDefaultColors.containsKey(objectName)) {
			mDefaultColors.put(objectName, objectColor);
			mController.setAmbientColor(objectName, HIGHLIGHT_COLOR);
		} else {
			mController.setAmbientColor(objectName, mDefaultColors.get(objectName));
			mDefaultColors.remove(objectName);
		}
	}

	@Override
	public void onObjectLoaded() {
		mController = mFragment.getController();
		mAnimator = mFragment.getAnimator();
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				String objectList = "";
				for (String object : mController.getObjectList()) {
					objectList += object + "\n";
				}
				mObjectListText.setText(objectList);
			}
		});
	}
}

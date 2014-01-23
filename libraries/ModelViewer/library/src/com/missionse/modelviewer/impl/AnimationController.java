package com.missionse.modelviewer.impl;

import rajawali.Object3D;
import rajawali.animation.Animation3D;
import rajawali.animation.Animation3D.RepeatMode;
import rajawali.animation.RotateAnimation3D;
import rajawali.animation.ScaleAnimation3D;
import rajawali.animation.TranslateAnimation3D;
import rajawali.math.vector.Vector3;
import rajawali.math.vector.Vector3.Axis;

import com.missionse.modelviewer.ModelAnimationController;
import com.missionse.modelviewer.ModelViewerRenderer;

/**
 * Provides a set of functions used to animate a model.
 */
public class AnimationController implements ModelAnimationController {

	private ModelViewerRenderer mRenderer;
	private Object3D mObjectGroup;
	private Animation3D mRotationAnim, mScaleAnim, mTranslationAnim;

	private static final int FULL_ROTATION = 360;

	/**
	 * Constructor.
	 * @param renderer The renderer containing the object group.
	 */
	public AnimationController(final ModelViewerRenderer renderer) {
		mRenderer = renderer;
	}

	/**
	 * Sets the object group to be animated.
	 * @param objectGroup The object group on which animations will be performed.
	 */
	public void setObjectGroup(final Object3D objectGroup) {
		mObjectGroup = objectGroup;
	}

	@Override
	public void startXRotation(final long durationMs) {
		stopRotation();

		if (mObjectGroup != null) {
			mRotationAnim = new RotateAnimation3D(Axis.Y, FULL_ROTATION);
			mRotationAnim.setDuration(durationMs);
			mRotationAnim.setRepeatMode(RepeatMode.INFINITE);
			mRotationAnim.setTransformable3D(mObjectGroup);
			mRenderer.registerAnimation(mRotationAnim);

			mRotationAnim.play();
		}
	}

	@Override
	public void startYRotation(final long durationMs) {
		stopRotation();

		if (mObjectGroup != null) {
			mRotationAnim = new RotateAnimation3D(Axis.X, FULL_ROTATION);
			mRotationAnim.setDuration(durationMs);
			mRotationAnim.setRepeatMode(RepeatMode.INFINITE);
			mRotationAnim.setTransformable3D(mObjectGroup);
			mRenderer.registerAnimation(mRotationAnim);

			mRotationAnim.play();
		}
	}

	@Override
	public void rotateTo(final float xDegrees, final float yDegrees, final float zDegrees, final long durationMs) {
		stopRotation();

		if (mObjectGroup != null) {
			mRotationAnim = new RotateAnimation3D(xDegrees, yDegrees, zDegrees);
			mRotationAnim.setDuration(durationMs);
			mRotationAnim.setRepeatMode(RepeatMode.NONE);
			mRotationAnim.setTransformable3D(mObjectGroup);
			mRenderer.registerAnimation(mRotationAnim);

			mRotationAnim.play();
		}
	}

	@Override
	public void stopRotation() {
		if (mRotationAnim != null) {
			mRotationAnim.pause();
			mRenderer.unregisterAnimation(mRotationAnim);
			mRotationAnim = null;
		}
	}

	@Override
	public boolean isRotating() {
		return (mRotationAnim != null && mRotationAnim.isPlaying());
	}

	@Override
	public void scaleTo(final float scale, final long durationMs) {
		stopScaling();

		if (mObjectGroup != null) {
			mScaleAnim = new ScaleAnimation3D(scale);
			mScaleAnim.setDuration(durationMs);
			mScaleAnim.setRepeatMode(RepeatMode.NONE);
			mScaleAnim.setTransformable3D(mObjectGroup);
			mRenderer.registerAnimation(mScaleAnim);

			mScaleAnim.play();
		}
	}

	@Override
	public void stopScaling() {
		if (mScaleAnim != null) {
			mScaleAnim.pause();
			mRenderer.unregisterAnimation(mScaleAnim);
			mScaleAnim = null;
		}
	}

	@Override
	public boolean isScaling() {
		return (mScaleAnim != null && mScaleAnim.isPlaying());
	}

	@Override
	public void translateTo(final float x, final float y, final float z, final long durationMs) {
		stopTranslation();

		if (mObjectGroup != null) {
			mTranslationAnim = new TranslateAnimation3D(new Vector3(x, y, z));
			mTranslationAnim.setDuration(durationMs);
			mTranslationAnim.setRepeatMode(RepeatMode.NONE);
			mTranslationAnim.setTransformable3D(mObjectGroup);
			mRenderer.registerAnimation(mTranslationAnim);

			mTranslationAnim.play();
		}
	}

	@Override
	public void stopTranslation() {
		if (mTranslationAnim != null) {
			mTranslationAnim.pause();
			mRenderer.unregisterAnimation(mTranslationAnim);
			mTranslationAnim = null;
		}
	}

	@Override
	public boolean isTranslating() {
		return (mTranslationAnim != null && mTranslationAnim.isPlaying());
	}
}

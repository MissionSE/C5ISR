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

public class AnimationController implements ModelAnimationController {

	private ModelViewerRenderer renderer;
	private Object3D object;
	private Animation3D rotationAnim, scaleAnim, translationAnim;

	public AnimationController(final ModelViewerRenderer modelRenderer) {
		renderer = modelRenderer;
	}

	public void setObject(final Object3D object3D) {
		object = object3D;
	}

	@Override
	public void startXRotation(final long duration) {
		stopRotation();

		if (object != null) {
			rotationAnim = new RotateAnimation3D(Axis.Y, 360);
			rotationAnim.setDuration(8000);
			rotationAnim.setRepeatMode(RepeatMode.INFINITE);
			rotationAnim.setTransformable3D(object);
			renderer.registerAnimation(rotationAnim);

			rotationAnim.play();
		}
	}

	@Override
	public void startYRotation(final long duration) {
		stopRotation();

		if (object != null) {
			rotationAnim = new RotateAnimation3D(Axis.X, 360);
			rotationAnim.setDuration(duration);
			rotationAnim.setRepeatMode(RepeatMode.INFINITE);
			rotationAnim.setTransformable3D(object);
			renderer.registerAnimation(rotationAnim);

			rotationAnim.play();
		}
	}

	@Override
	public void rotateTo(final float xDegrees, final float yDegrees, final float zDegrees, final long duration) {
		stopRotation();

		if (object != null) {
			rotationAnim = new RotateAnimation3D(xDegrees, yDegrees, zDegrees);
			rotationAnim.setDuration(duration);
			rotationAnim.setRepeatMode(RepeatMode.NONE);
			rotationAnim.setTransformable3D(object);
			renderer.registerAnimation(rotationAnim);

			rotationAnim.play();
		}
	}

	@Override
	public void stopRotation() {
		if (rotationAnim != null) {
			rotationAnim.pause();
			renderer.unregisterAnimation(rotationAnim);
			rotationAnim = null;
		}
	}

	@Override
	public boolean isRotating() {
		return (rotationAnim != null && rotationAnim.isPlaying());
	}

	@Override
	public void scaleTo(final float scale, final long duration) {
		stopScaling();

		if (object != null) {
			scaleAnim = new ScaleAnimation3D(scale);
			scaleAnim.setDuration(duration);
			scaleAnim.setRepeatMode(RepeatMode.NONE);
			scaleAnim.setTransformable3D(object);
			renderer.registerAnimation(scaleAnim);

			scaleAnim.play();
		}
	}

	@Override
	public void stopScaling() {
		if (scaleAnim != null) {
			scaleAnim.pause();
			renderer.unregisterAnimation(scaleAnim);
			scaleAnim = null;
		}
	}

	@Override
	public boolean isScaling() {
		return (scaleAnim != null && scaleAnim.isPlaying());
	}

	@Override
	public void translateTo(final float x, final float y, final float z, final long duration) {
		stopTranslation();

		if (object != null) {
			translationAnim = new TranslateAnimation3D(new Vector3(x, y, z));
			translationAnim.setDuration(duration);
			translationAnim.setRepeatMode(RepeatMode.NONE);
			translationAnim.setTransformable3D(object);
			renderer.registerAnimation(translationAnim);

			translationAnim.play();
		}
	}

	@Override
	public void stopTranslation() {
		if (translationAnim != null) {
			translationAnim.pause();
			renderer.unregisterAnimation(translationAnim);
			translationAnim = null;
		}
	}

	@Override
	public boolean isTranslating() {
		return (translationAnim != null && translationAnim.isPlaying());
	}
}

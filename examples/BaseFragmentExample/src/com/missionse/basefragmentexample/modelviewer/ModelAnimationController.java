package com.missionse.basefragmentexample.modelviewer;

import rajawali.Object3D;
import rajawali.animation.Animation3D;
import rajawali.animation.Animation3D.RepeatMode;
import rajawali.animation.RotateAnimation3D;
import rajawali.animation.ScaleAnimation3D;
import rajawali.animation.TranslateAnimation3D;
import rajawali.math.vector.Vector3;
import rajawali.math.vector.Vector3.Axis;


public class ModelAnimationController {

	private ModelViewerRenderer renderer;
	private Object3D objectGroup;
	private Animation3D rotationAnim, scaleAnim, translationAnim;

	public ModelAnimationController(final ModelViewerRenderer modelRenderer) {
		renderer = modelRenderer;
	}

	public void setObject(final Object3D object3D) {
		objectGroup = object3D;
	}

	public void startXRotation(final long durationMS) {
		stopRotation();

		if (objectGroup != null) {
			rotationAnim = new RotateAnimation3D(Axis.Y, 360);
			rotationAnim.setDuration(8000);
			rotationAnim.setRepeatMode(RepeatMode.INFINITE);
			rotationAnim.setTransformable3D(objectGroup);
			renderer.registerAnimation(rotationAnim);

			rotationAnim.play();
		}
	}

	public void startYRotation(final long durationMS) {
		stopRotation();

		if (objectGroup != null) {
			rotationAnim = new RotateAnimation3D(Axis.X, 360);
			rotationAnim.setDuration(durationMS);
			rotationAnim.setRepeatMode(RepeatMode.INFINITE);
			rotationAnim.setTransformable3D(objectGroup);
			renderer.registerAnimation(rotationAnim);

			rotationAnim.play();
		}
	}

	public void rotateTo(final float xDegrees, final float yDegrees, final float zDegrees, final long durationMS) {
		stopRotation();

		if (objectGroup != null) {
			rotationAnim = new RotateAnimation3D(xDegrees, yDegrees, zDegrees);
			rotationAnim.setDuration(durationMS);
			rotationAnim.setRepeatMode(RepeatMode.NONE);
			rotationAnim.setTransformable3D(objectGroup);
			renderer.registerAnimation(rotationAnim);

			rotationAnim.play();
		}
	}

	public void stopRotation() {
		if (rotationAnim != null) {
			rotationAnim.pause();
			renderer.unregisterAnimation(rotationAnim);
			rotationAnim = null;
		}
	}

	public boolean isRotating() {
		return (rotationAnim != null && rotationAnim.isPlaying());
	}

	public void scaleTo(final float scale, final long durationMS) {
		stopScaling();

		if (objectGroup != null) {
			scaleAnim = new ScaleAnimation3D(scale);
			scaleAnim.setDuration(durationMS);
			scaleAnim.setRepeatMode(RepeatMode.NONE);
			scaleAnim.setTransformable3D(objectGroup);
			renderer.registerAnimation(scaleAnim);

			scaleAnim.play();
		}
	}

	public void stopScaling() {
		if (scaleAnim != null) {
			scaleAnim.pause();
			renderer.unregisterAnimation(scaleAnim);
			scaleAnim = null;
		}
	}

	public boolean isScaling() {
		return (scaleAnim != null && scaleAnim.isPlaying());
	}

	public void translateTo(final float x, final float y, final float z, final long durationMS) {
		stopTranslation();

		if (objectGroup != null) {
			translationAnim = new TranslateAnimation3D(new Vector3(x, y, z));
			translationAnim.setDuration(durationMS);
			translationAnim.setRepeatMode(RepeatMode.NONE);
			translationAnim.setTransformable3D(objectGroup);
			renderer.registerAnimation(translationAnim);

			translationAnim.play();
		}
	}

	public void stopTranslation() {
		if (translationAnim != null) {
			translationAnim.pause();
			renderer.unregisterAnimation(translationAnim);
			translationAnim = null;
		}
	}

	public boolean isTranslating() {
		return (translationAnim != null && translationAnim.isPlaying());
	}
}

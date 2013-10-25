package com.missionse.modelviewer.impl;

import rajawali.Object3D;
import rajawali.animation.Animation3D;
import rajawali.animation.Animation3D.RepeatMode;
import rajawali.animation.RotateAnimation3D;
import rajawali.lights.DirectionalLight;
import rajawali.math.vector.Vector3;
import rajawali.math.vector.Vector3.Axis;
import rajawali.parser.LoaderOBJ;
import rajawali.parser.ParsingException;
import android.content.Context;

import com.missionse.modelviewer.ModelViewerFragment;

public class ObjModelFragment extends ModelViewerFragment {

	@Override
	protected ModelViewerRenderer createRenderer(final int modelID) {
		return new ObjModelRenderer(getActivity(), modelID);
	}

	private final class ObjModelRenderer extends ModelViewerRenderer {

		private final int modelID;

		private DirectionalLight directionalLight;
		private Object3D objectGroup;
		private Animation3D rotationAnim;

		public ObjModelRenderer(final Context context, final int model) {
			super(context);
			modelID = model;
		}

		@Override
		protected void initScene() {
			directionalLight = new DirectionalLight();
			directionalLight.setPosition(0, 0, 4);
			directionalLight.setPower(1);

			getCurrentScene().addLight(directionalLight);
			getCurrentCamera().setZ(16);

			LoaderOBJ objParser = new LoaderOBJ(getContext().getResources(),
					getTextureManager(), modelID);
			try	{
				objParser.parse();
				objectGroup = objParser.getParsedObject();
				addChild(objectGroup);

				rotationAnim = new RotateAnimation3D(Axis.Y, 360);
				rotationAnim.setDuration(8000);
				rotationAnim.setRepeatMode(RepeatMode.INFINITE);
				rotationAnim.setTransformable3D(objectGroup);

			} catch (ParsingException e) {
				e.printStackTrace();
			}

			registerAnimation(rotationAnim);
		}

		@Override
		protected void setAutoRotation(final boolean autoRotate) {
			if (!isAutoRotating()) {
				if (autoRotate) {
					rotationAnim.reset();
					rotationAnim.play();
				}
			} else if (!autoRotate) {
				rotationAnim.pause();
			}
		}

		@Override
		protected boolean isAutoRotating() {
			return rotationAnim.isPlaying();
		}

		@Override
		protected void rotate(final float xAngle, final float yAngle, final float zAngle) {
			objectGroup.rotateAround(Vector3.Y, xAngle);
			objectGroup.rotateAround(Vector3.X, yAngle);
			objectGroup.rotateAround(Vector3.Z, zAngle);
		}

		@Override
		protected void scale(final float scaleFactor) {
			Vector3 scale = objectGroup.getScale();
			objectGroup.setScaleX(scale.x * scaleFactor);
			objectGroup.setScaleY(scale.y * scaleFactor);
			objectGroup.setScaleZ(scale.z * scaleFactor);
		}
	}
}

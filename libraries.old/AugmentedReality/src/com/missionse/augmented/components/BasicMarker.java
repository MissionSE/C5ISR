package com.missionse.augmented.components;

import gl.GLCamera;
import gl.MarkerObject;
import util.Vec;
import worldData.World;
import android.opengl.Matrix;

public abstract class BasicMarker implements MarkerObject {

	private float[] mInvertedCameraMatrix = new float[16];
	private float[] mResultPosVec = { 0, 0, 0, 1 };
	private float[] mAntiCameraMarkerRotMatrix = new float[16];

	protected GLCamera mCamera;
	protected World mWorld; 
	protected int mId;

	public BasicMarker(int id, GLCamera camera, World world) {
		mCamera = camera;
		mId = id;
		mWorld = world;
	}

	@Override
	public int getMyId() {
		return mId;
	}

	@Override
	public void OnMarkerPositionRecognized(float[] markerRotMatrix,
			int startOffset, int end) {
		
		abstractPrePosition();

		Matrix.invertM(mInvertedCameraMatrix, 0, mCamera.getRotationMatrix(), 0);

		float[] markerCenterPosVec = { markerRotMatrix[startOffset + 12],
				markerRotMatrix[startOffset + 13],
				markerRotMatrix[startOffset + 14], 1 };
		Matrix.multiplyMV(mResultPosVec, 0, mInvertedCameraMatrix, 0,
				markerCenterPosVec, 0);

		Vec camPos = mCamera.getPosition();
		setObjectPos(new Vec(mResultPosVec[0] + camPos.x, mResultPosVec[1]
				+ camPos.y, mResultPosVec[2] + camPos.z));

		Matrix.multiplyMM(mAntiCameraMarkerRotMatrix, 0, mInvertedCameraMatrix,
				0, markerRotMatrix, startOffset);

		// clear the translation values:
		mAntiCameraMarkerRotMatrix[12] = 0;
		mAntiCameraMarkerRotMatrix[13] = 0;
		mAntiCameraMarkerRotMatrix[14] = 0;

		setObjRotation(mAntiCameraMarkerRotMatrix);

		abstractPostPosition();
		
	}
	
	/*
	 * Processing that can be done after a marker has been detected and positioned
	 * into the world
	 */
	protected void abstractPostPosition(){}
	
	/*
	 * Processing that can be done before a marker has been detected and positioned
	 * into the world 
	 */
	protected void abstractPrePosition(){}

	/**
	 * Control how the object will rotate
	 * @param rotMatrix
	 */
	public abstract void setObjRotation(float[] rotMatrix);

	/** 
	 * Control how the object will be positioned 
	 * @param positionVec
	 */
	public abstract void setObjectPos(Vec positionVec);

}
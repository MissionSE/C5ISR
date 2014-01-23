package com.missionse.augmented.components;

import gl.GLCamera;
import gl.MarkerObject;

import java.util.Date;

import util.Vec;
import android.opengl.Matrix;
import android.util.Log;

public abstract class TimerMarker implements MarkerObject {
	
	public static int REFRESH_RATE = 300;
	public static int THRESH_HOLD = 500;
	
	private float[] invertedCameraMatrix = new float[16];
	private float[] resultPosVec = { 0, 0, 0, 1 };
	private float[] antiCameraMarkerRotMatrix = new float[16];
	
	protected Thread mCheckTimeThread =null;
	protected volatile boolean mContinueThread = true;;
	
	protected GLCamera myCamera;
	protected int myId;
	protected volatile long mLastUpdatedTime;
	
	public TimerMarker(int id, GLCamera camera) {
		myCamera = camera;
		myId = id;
		mLastUpdatedTime = 0;
	}

	@Override
	public int getMyId() {
		return myId;
	}

	@Override
	public void OnMarkerPositionRecognized(float[] markerRotMatrix,
			int startOffset, int end) {
		
		Matrix.invertM(invertedCameraMatrix, 0, myCamera.getRotationMatrix(), 0);

		float[] markerCenterPosVec = { markerRotMatrix[startOffset + 12],
				markerRotMatrix[startOffset + 13],
				markerRotMatrix[startOffset + 14], 1 };
		Matrix.multiplyMV(resultPosVec, 0, invertedCameraMatrix, 0,
				markerCenterPosVec, 0);

		Vec camPos = myCamera.getPosition();
		setObjectPos(new Vec(resultPosVec[0] + camPos.x, resultPosVec[1]
				+ camPos.y, resultPosVec[2] + camPos.z));

		Matrix.multiplyMM(antiCameraMarkerRotMatrix, 0, invertedCameraMatrix,
				0, markerRotMatrix, startOffset);

		// clear the translation values:
		antiCameraMarkerRotMatrix[12] = 0;
		antiCameraMarkerRotMatrix[13] = 0;
		antiCameraMarkerRotMatrix[14] = 0;

		// addAngle(antiCameraMarkerRotMatrix, sideAngle);
		// sideAngle = 0;

		setObjRotation(antiCameraMarkerRotMatrix);
		
		
		mLastUpdatedTime = (new Date()).getTime();
		if(mCheckTimeThread == null || !mCheckTimeThread.isAlive()){
			mContinueThread = true;
			mCheckTimeThread = createCheckTimeThread();
			mCheckTimeThread.start();
		}

		/*
		 * alternative method which does not work for now:
		 * 
		 * its not so clear to me if it would be better to extract the rotation
		 * angles and store them directly in the myRotation field. now its still
		 * possible to rotate the mesh in addition to the rotation by the marker
		 * matrix but its not possible to read the angle values and use them for
		 * something else then this concrete scenario
		 */

		// float[] resultingAngles = { 0, 0, 0, 1 };
		//
		// getAngles(resultingAngles, antiCameraMarkerRotMatrix);
		//
		// if (myTargetMesh.myRotation == null)
		// myTargetMesh.myRotation = new Vec(resultingAngles[0],
		// resultingAngles[1], resultingAngles[2]);
		// else {
		// myTargetMesh.myRotation.x = resultingAngles[0];
		// myTargetMesh.myRotation.y = resultingAngles[1];
		// myTargetMesh.myRotation.z = resultingAngles[2];
		// }
	}
	
	private Thread createCheckTimeThread(){
		Thread lRetValue = new Thread(new Runnable(){
			
			@Override
			public void run(){
				while(mContinueThread){
					try {
						Thread.sleep(REFRESH_RATE);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				
					
					long lDelay = getDelay();
					if(lDelay > THRESH_HOLD){
						forceUpdateCall();
					}
				}
			}
		});
		return lRetValue;
	}
	
	public long getDelay(){
		return (new Date()).getTime() - mLastUpdatedTime;
	}
	
	public void forceUpdateCall(){
		setObjectPos(new Vec(0,0,0));
	}
	
	public boolean isUpdated(){
		long lDelay = getDelay();
		boolean lRetValue = false;
		if(lDelay > THRESH_HOLD){
			mContinueThread = false;
			lRetValue = false;
		}else{
			lRetValue = true;
		}
		
		StringBuilder lsb = new StringBuilder();
		lsb.append("<" + lRetValue + "> with a delay of: " + lDelay);
		Log.w(getClass().getSimpleName(),lsb.toString());
		return lRetValue;
			
	}

	public abstract void setObjRotation(float[] rotMatrix);

	public abstract void setObjectPos(Vec positionVec);
}

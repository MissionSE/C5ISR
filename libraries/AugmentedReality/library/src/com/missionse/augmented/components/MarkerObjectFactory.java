package com.missionse.augmented.components;

import gl.MarkerObject;
import gl.scenegraph.MeshComponent;
import util.Vec;
import worldData.Obj;

import com.missionse.augmented.setups.BasicMultiSetup;

public class MarkerObjectFactory {
	
	
	public static MarkerObject createTimeoutMarker(final BasicMultiSetup tSetup, int tId, final MeshComponent tMesh){
		
		return new TimerMarker(tId,tSetup.getCamera()){
			MeshComponent mMesh = tMesh;
			Obj mObject;
			boolean mFirstTime = true;
			
			@Override
			public void setObjRotation(float[] rotMatrix) {
				mMesh.setRotationMatrix(rotMatrix);	
			}

			@Override
			public void setObjectPos(Vec positionVec) {
				if(mFirstTime){
					mFirstTime = false;
					mObject = new Obj();
					mObject.setComp(mMesh);
					tSetup.getWorld().add(mObject);
				}
				
				if(isUpdated()){
					mMesh.setPosition(positionVec);
				}else{
					if(mMesh != null || mObject != null){
						mObject.remove(mMesh);
						tSetup.getWorld().remove(mMesh);
						mFirstTime = true;
					}	
				}
			}
		};
	}
	

}

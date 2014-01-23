package com.missionse.augmented.components;

import gl.GLCamera;
import gl.scenegraph.MeshComponent;
import util.Vec;
import worldData.Obj;
import worldData.World;

public class StationaryMarker extends BasicMarker {
	
	private volatile MeshComponent mMesh;
	private volatile boolean mFirstTime;
	private volatile Obj mMeshContainer;

	public StationaryMarker(int id, GLCamera camera, World world, MeshComponent mesh) {
		super(id, camera,world);
		mMesh = mesh;
	}

	@Override
	public void setObjRotation(float[] rotMatrix) {
		if(mMesh != null)
			mMesh.setRotationMatrix(rotMatrix);
	}

	@Override
	public void setObjectPos(Vec positionVec) {
		if (mFirstTime) {
			mFirstTime = false;
			mMeshContainer = new Obj();
			mMeshContainer.setComp(mMesh);
			mWorld.add(mMeshContainer);
		}
		mMesh.setPosition(positionVec);
	}

}

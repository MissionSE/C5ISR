package com.missionse.augmented.setups;

import geo.GeoObj;
import gl.CustomGLSurfaceView;
import gl.GL1Renderer;
import gl.GLCamera;
import gl.GLFactory;
import gl.GLRenderer;
import gl.MarkerObject;
import gl.scenegraph.MeshComponent;
import gui.GuiSetup;

import java.util.HashMap;
import java.util.Map;

import markerDetection.MarkerDetectionSetup;
import markerDetection.MarkerObjectMap;
import markerDetection.UnrecognizedMarkerListener;
import system.EventManager;
import util.Vec;
import worldData.Obj;
import worldData.SystemUpdater;
import worldData.World;
import actions.Action;
import actions.ActionCalcRelativePos;
import actions.ActionMoveCameraBuffered;
import actions.ActionRotateCameraBuffered;
import android.app.Activity;
import android.util.Log;
import android.util.SparseArray;
import android.widget.LinearLayout;

import com.missionse.augmented.interfaces.ModifiableWorldSetup;
import commands.Command;

/**
 * Basic Setup to handle both marker and geo objects 
 *
 */


public class BasicMultiSetup extends MarkerDetectionSetup implements ModifiableWorldSetup {
	
	private GLCamera mCamera;
	private World mWorld;
	private MarkerObjectMap mMarkerMap;
	private GLFactory mGLFactory;
	private boolean mWaitForGps = false;
	
	private Map<MeshComponent,Obj> mMeshToObjMap; 
	private SparseArray<MarkerObject> mIdToMarkerMap;

	
	public BasicMultiSetup(boolean tWaitForGps){
		mWaitForGps = tWaitForGps;
		mMeshToObjMap = new HashMap<MeshComponent,Obj>();
		mIdToMarkerMap = new SparseArray<MarkerObject>();
		mMarkerMap = new MarkerObjectMap();
		
		mCamera = new GLCamera(new Vec(0, 0, 6));
		mWorld = new World(mCamera);

	}
	
		
	@Override
	public UnrecognizedMarkerListener _a2_getUnrecognizedMarkerListener() {
		return new UnrecognizedMarkerListener() {
			@Override
			public void onUnrecognizedMarkerDetected(int markerCode,
					float[] mat, int startIdx, int endIdx, int rotationValue) {
				StringBuilder sb = new StringBuilder();
				for(Map.Entry<Integer, MarkerObject> e : mMarkerMap.entrySet()){
					sb.append("(" + e.getKey() + ") is set to " + e.getValue()+ "\n");
				}
				System.out.println("unrecognized markerCode=" + markerCode + "\n" + sb.toString());
				Log.w(getClass().getSimpleName(), "unrecognized markerCode=" + markerCode + "\n" + sb.toString());
			}};
	}

	@Override
	public void _a3_registerMarkerObjects(MarkerObjectMap markerObjectMap) {
		mMarkerMap.putAll(markerObjectMap);
		getDetectionThread().setMarkerObjectMap(mMarkerMap);
	}

	@Override
	public void _a_initFieldsIfNecessary() {
		
	}

	@Override
	public void _b_addWorldsToRenderer(GL1Renderer glRenderer,
			GLFactory objectFactory, GeoObj currentPosition) {
		mGLFactory = objectFactory;
		glRenderer.addRenderElement(mWorld);
	}

	@Override
	public void _c_addActionsToEvents(EventManager eventManager,
			CustomGLSurfaceView arView, SystemUpdater updater) {
		ActionMoveCameraBuffered move = new ActionMoveCameraBuffered(mCamera, 5,
				25);
		arView.addOnTouchMoveAction(move);
		eventManager.addOnTrackballAction(move);
		Action rot = new ActionRotateCameraBuffered(mCamera);
		updater.addObjectToUpdateCycle(rot);
		eventManager.addOnOrientationChangedAction(rot);
		eventManager.addOnLocationChangedAction(new ActionCalcRelativePos(
				mWorld, mCamera));
		
		//TODO: Roberto, wait for valid gps?
	}

	@Override
	public void _d_addElementsToUpdateThread(SystemUpdater updater) {
		updater.addObjectToUpdateCycle(mWorld);
	}

	@Override
	public void _e2_addElementsToGuiSetup(GuiSetup guiSetup, Activity activity) {
		//TODO: Roberto, and default gui elements like valid gps track bar'
		_e3_addElementsToGuiSetup(guiSetup,activity);		
	}
	
	public void _e3_addElementsToGuiSetup(GuiSetup guiSetup, Activity activity){}
	
	public GLCamera getCamera(){
		return mCamera;
	}
	
	public World getWorld(){
		return mWorld;
	}
	
	public MarkerObjectMap getMarkerMap(){
		return mMarkerMap;
	}
	
	public boolean isWaitingOnGps(){
		return mWaitForGps;
	}

	
	@Override
	public boolean addObject(MeshComponent tComponent) {
		Obj o = new Obj();
		o.setComp(tComponent);
		mMeshToObjMap.put(tComponent, o);
		mWorld.add(o);
		return true;
	}


	@Override
	public boolean removeObject(MeshComponent tComponent) {
		Obj o = mMeshToObjMap.remove(tComponent);
		if(o != null){
			mWorld.remove(o);
		}else{
			Log.d("BASICMULTISETUP", "Unable to remove object. Does not exist " + tComponent);
			return false;
		}
		return true;
	}


	@Override
	public int addMarker(MarkerObject tComponent) {
		mIdToMarkerMap.put(tComponent.getMyId(), tComponent);
		mMarkerMap.put(tComponent);
		return tComponent.getMyId();
	}

	@Override
	public int removeMarker(MarkerObject tComponent) {
		mIdToMarkerMap.remove(tComponent.getMyId());
		mMarkerMap.remove(tComponent.getMyId());
		return tComponent.getMyId();
	}

	@Override
	public GLFactory getGLFactory() {
		if(mGLFactory == null)
			mGLFactory = GLFactory.getInstance();
		return mGLFactory;
	}


	@Override
	public boolean hideObject(MeshComponent tComponent) {
		Obj o = mMeshToObjMap.get(tComponent);
		if( o != null){
			o.remove(tComponent);
		}else{
			Log.e("BASICMULTISETUP", "Unable to hide object. " + tComponent);
			return false;
		}
		return true;
	}


	@Override
	public boolean hideMarker(MarkerObject tComponent) {
		MarkerObject o = mMarkerMap.remove(tComponent.getMyId());
		return true;
	}

	@Override
	public boolean showObject(MeshComponent tComponent) {
		Obj o = mMeshToObjMap.get(tComponent);
		if( o != null){
			o.setComp(tComponent);
		}else{
			Log.e("BASICMULTISETUP", "Unable to show object. " + tComponent);
			return false;
		}
		return true;
	}


	@Override
	public boolean showMarker(MarkerObject tComponent) {
		mMarkerMap.put(tComponent.getMyId(),mIdToMarkerMap.get(tComponent.getMyId()));
		return true;
	}
	
	public void removeAll(){
		removeAllObjects();
		removeAllMarkers();
	}
	
	public void removeAllObjects(){
		//remvoe all objs from world and remove all components from objects
		for(Map.Entry<MeshComponent,Obj> e : mMeshToObjMap.entrySet()){
			mWorld.remove((Obj)e.getValue());
			((Obj)e.getValue()).remove((MeshComponent)e.getKey());
		}
		mMeshToObjMap.clear();
		mMeshToObjMap = new HashMap<MeshComponent,Obj>();
	}
	
	public void removeAllMarkers(){
		mIdToMarkerMap.clear();
		mMarkerMap.clear();
		
		mIdToMarkerMap = new SparseArray<MarkerObject>();
		mMarkerMap = new MarkerObjectMap();
	}

}

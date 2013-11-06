package com.missionse.augmented.setups;

import geo.GeoObj;
import gl.Color;
import gl.CustomGLSurfaceView;
import gl.GL1Renderer;
import gl.GLCamera;
import gl.GLFactory;
import gl.GLRenderer;
import gl.animations.AnimationFaceToCamera;
import gl.scenegraph.MeshComponent;
import gl.scenegraph.Shape;
import gui.GuiSetup;
import markerDetection.MarkerDetectionSetup;
import markerDetection.MarkerObjectMap;
import markerDetection.UnrecognizedMarkerListener;
import system.EventManager;
import util.IO;
import util.Vec;
import worldData.Obj;
import worldData.SystemUpdater;
import worldData.World;
import actions.Action;
import actions.ActionMoveCameraBuffered;
import actions.ActionRotateCameraBuffered;
import android.app.Activity;
import android.util.Log;
import android.widget.Button;

import com.missionse.augmented.components.TimerMarker;
import com.missionse.augmentedreality.R;
import commands.Command;
import commands.ui.CommandShowToast;
import components.ProximitySensor;

public class MultiMarkerSetup extends MarkerDetectionSetup {

	private GLCamera camera;
	private World world;
	private MarkerObjectMap markermap;
	
	public MultiMarkerSetup(boolean value){
		super(value);
	}
	 

	@Override
	public void _a_initFieldsIfNecessary() {
		camera = new GLCamera(new Vec(0, 0, 10));
		world = new World(camera);
	}

	@Override
	public UnrecognizedMarkerListener _a2_getUnrecognizedMarkerListener() {
		return new UnrecognizedMarkerListener() {
			@Override
			public void onUnrecognizedMarkerDetected(int markerCode,
					float[] mat, int startIdx, int endIdx, int rotationValue) {
				System.out.println("unrecognized markerCode=" + markerCode);
				Log.w(getClass().getSimpleName(), "unrecognized markerCode=" + markerCode);
			}
		};
	}

	@Override
	public void _a3_registerMarkerObjects(MarkerObjectMap markerObjectMap) {
		markermap = markerObjectMap;
	
		_x_addDefaultMarkers(markermap);
		
	}
	
	public void _x_addDefaultMarkers(MarkerObjectMap markerObjectMap){
		/*
		 * example for more complex behavior:
		 */
		markerObjectMap.put(new BasicMarker(2, camera) {

			MeshComponent targetMesh;
			boolean firstTime = true;

			@Override
			public void setObjectPos(Vec positionVec) {
				/*
				 * the first time this method is called an object could be
				 * created and added to the world
				 */
				if (firstTime) {
					firstTime = false;
					Obj aNewObject = new Obj();
					targetMesh = GLFactory.getInstance().newArrow();
					aNewObject.setComp(targetMesh);
					world.add(aNewObject);
				}
				targetMesh.setPosition(positionVec);
			}

			@Override
			public void setObjRotation(float[] rotMatrix) {
				if (targetMesh != null) {
					targetMesh.setRotationMatrix(rotMatrix);
				}
			}
		});
		
		markerObjectMap.put(new BasicMarker(4,camera){
			
			MeshComponent mesh;
			boolean firstTime = true;
			
			@Override
			public void setObjRotation(float[] rotMatrix) {
				if(mesh != null){
					mesh.setRotationMatrix(rotMatrix);
				}
				
			}

			@Override
			public void setObjectPos(Vec positionVec) {
				if(firstTime){
					firstTime = false;
					Obj lNewObject = new Obj();
					//Button b = new Button(getActivity());
					//b.setText("Hello");
					//mesh = GLFactory.getInstance().newTexturedSquare("buttonId", IO.loadBitmapFromView(b));
					mesh = GLFactory.getInstance().newTexturedSquare("checkId", IO.loadBitmapFromId(getActivity(), R.drawable.correctcirclegreen));
					//mesh = GLFactory.getInstance().newTexturedSquare("trashCan",IO.loadBitmapFromAssetsFolder(getActivity(), "trashbin.obj") );
					mesh.setOnClickCommand(new CommandShowToast(getActivity(),"Don't touch me."));
					mesh.addChild(new AnimationFaceToCamera(camera, 0.5f));
					mesh.setScale(new Vec(3, 3, 3));
					//mesh.setColor(Color.red());
					mesh.setRotation(new Vec(0,0,0));
					lNewObject.setComp(mesh);
					world.add(lNewObject);
				}
				mesh.setPosition(positionVec);
				
			}
			
		});
		
		markerObjectMap.put(new TimerMarker(5,camera){
			
			MeshComponent mesh;
			Obj mObject;
			boolean firstTime = true;
			boolean isVisable = false;
			
			@Override
			public void setObjRotation(float[] rotMatrix) {
				//if(mesh != null){
				//	mesh.setRotationMatrix(rotMatrix);
				//}
				
			}

			@Override
			public void setObjectPos(Vec positionVec) {
				if(firstTime){
					firstTime = false;
					mObject = new Obj();
					Button b = new Button(getActivity());
					b.setText("Roberto's Computer");
					mesh = GLFactory.getInstance().newTexturedSquare("buttonId", IO.loadBitmapFromView(b));
					mesh.setOnClickCommand(new CommandShowToast(getActivity(),"Power is off"));
					//mesh.addChild(new AnimationFaceToCamera(camera, 0.5f));
					mesh.setScale(new Vec(2, 2, 2));
					mesh.setColor(Color.red());
					//mesh.setRotation(new Vec(180,180,0));
					mObject.setComp(mesh);
					world.add(mObject);
				}
				
				if(isUpdated()){
					mesh.setPosition(positionVec);
				}else{
					if(mesh != null || mObject != null){
						mObject.remove(mesh);
						world.remove(mObject);
						firstTime = true;
					}	
				}
				//mesh.setPosition(positionVec);
			}
			
		});
	}

	@Override
	public void _b_addWorldsToRenderer(GL1Renderer renderer,
			GLFactory objectFactory, GeoObj currentPosition) {
		
		_y_addDefaultObjects(world);
		renderer.addRenderElement(world);
		
	}
	
	public void _y_addDefaultObjects(World world){
		MeshComponent arrow = GLFactory.getInstance().newArrow();
		arrow.setPosition(new Vec(0, 0, 4));

		MeshComponent circle = GLFactory.getInstance().newCircle(
				Color.redTransparent());
		circle.setScale(new Vec(4, 4, 4));

		final MeshComponent itemMesh = new Shape();
		itemMesh.addChild(arrow);
		itemMesh.addChild(circle);
		itemMesh.setPosition(Vec.getNewRandomPosInXYPlane(camera.getPosition(),
				5, 10));

		Obj itemToCollect = new Obj();
		itemToCollect.setComp(new ProximitySensor(camera, 3f) {
			@Override
			public void onObjectIsCloseToCamera(GLCamera myCamera2, Obj obj,
					MeshComponent m, float currentDistance) {
				//catchedNumber++;
				//new CommandShowToast(myTargetActivity, "You got me "
				//		+ catchedNumber + " times").execute();
				itemMesh.setPosition(Vec.getNewRandomPosInXYPlane(
						camera.getPosition(), 5, 20));
			}
		});

		itemToCollect.setComp(itemMesh);

		world.add(itemToCollect);
	
	}

	@Override
	public void _c_addActionsToEvents(EventManager eventManager,
			CustomGLSurfaceView arView, SystemUpdater updater) {
		arView.addOnTouchMoveListener(new ActionMoveCameraBuffered(camera, 5,
				25));
		Action rot = new ActionRotateCameraBuffered(camera);
		updater.addObjectToUpdateCycle(rot);
		eventManager.addOnOrientationChangedAction(rot);
		eventManager.addOnTrackballAction(new ActionMoveCameraBuffered(camera,
				1, 25));

	}

	@Override
	public void _d_addElementsToUpdateThread(SystemUpdater updater) {
		updater.addObjectToUpdateCycle(world);

	}

	@Override
	public void _e2_addElementsToGuiSetup(GuiSetup guiSetup, Activity activity) {
//		guiSetup.addButtonToBottomView(new Command() {
//
//			@Override
//			public boolean execute() {
//
//				Vec rayPosition = new Vec();
//				Vec rayDirection = new Vec();
//				camera.getPickingRay(rayPosition, rayDirection,
//						GLRenderer.halfWidth, GLRenderer.halfHeight);
//				
//				System.out.println("rayPosition=" + rayPosition);
//				System.out.println("rayDirection=" + rayDirection);
//
//				rayDirection.setLength(5);
//
//				// mesh1.setPosition(rayPosition.add(rayDirection));
//				MeshComponent mesh4 = new Shape();
//				mesh4.addChild(GLFactory.getInstance().newArrow());
//
//				Obj o = new Obj();
//				o.setComp(mesh4);
//				world.add(mesh4);
//
//				mesh4.setPosition(rayPosition.add(rayDirection));
//
//				return false;
//			}
//		}, "Place 2 meters infront");
		
	}
	

}

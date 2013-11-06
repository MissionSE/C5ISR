package com.missionse.augmented.components;

import gl.Color;
import gl.GLCamera;
import gl.GLFactory;
import gl.animations.AnimationFaceToCamera;
import gl.scenegraph.MeshComponent;
import gl.scenegraph.Shape;
import gui.simpleUI.ModifierGroup;
import gui.simpleUI.Theme;
import gui.simpleUI.Theme.ThemeColors;
import gui.simpleUI.modifiers.Headline;
import gui.simpleUI.modifiers.InfoText;
import util.IO;
import util.Vec;
import worldData.Obj;
import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.LinearLayout.LayoutParams;

import components.ProximitySensor;

public class MeshComponentFactory {
	
	
	public static Obj createProxyArrowWithCircle(final GLCamera camera){
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

		return itemToCollect;
	}
	
	public static MeshComponent createArrowWithCircle(){
		MeshComponent arrow = GLFactory.getInstance().newArrow();
		arrow.setPosition(new Vec(0, 0, 5));

		MeshComponent circle = GLFactory.getInstance().newCircle(
				Color.redTransparent());
		circle.setScale(new Vec(4, 4, 4));

		final MeshComponent itemMesh = new Shape();
		itemMesh.addChild(arrow);
		itemMesh.addChild(circle);

		
		return itemMesh;
	}
	
	public static MeshComponent createInfoBox(Activity activity,
			GLCamera camera, String title, Headline... headlines   ){
		ModifierGroup l = new ModifierGroup(Theme.A(activity,ThemeColors.initToBlue()));
		
		l.addModifier(new InfoText(title, Gravity.CENTER));
	
		for(Headline head : headlines){
			l.addModifier(head);
		}
		
		View v = l.getView(activity);

		MeshComponent button = GLFactory.getInstance().newTexturedSquare(
				"simpleUiId",
				IO.loadBitmapFromView(v, MeasureSpec.makeMeasureSpec(400,
						MeasureSpec.AT_MOST), LayoutParams.WRAP_CONTENT));
	
		button.addChild(new AnimationFaceToCamera(camera, 0.5f));
		button.setScale(new Vec(10, 10, 10));
		button.setColor(Color.red());
		
		return button;
	}
	
	public Headline createHeadlineInfo(String msg){
		return new Headline(com.missionse.augmentedreality.R.drawable.infoboxblue,msg);
	}
	public Headline createHeadlineWarning(String msg){
		return new Headline(com.missionse.augmentedreality.R.drawable.warningcirclered,msg);
	}
	
	

}

package com.missionse.augmented.interfaces;

import gl.GLFactory;
import gl.MarkerObject;
import gl.scenegraph.MeshComponent;

public interface ModifiableWorldSetup {
	
	public boolean addObject(MeshComponent tComponent);
	
	public boolean removeObject(MeshComponent tComponent);
	
	public int addMarker(MarkerObject tComponent);
	
	public int removeMarker(MarkerObject tComponent);
	
	public GLFactory getGLFactory();
	
	public boolean hideObject(MeshComponent tComponent);
	
	public boolean hideMarker(MarkerObject tComponent);
	
	public boolean showObject(MeshComponent tComponent);
	
	public boolean showMarker(MarkerObject tComponent);

}

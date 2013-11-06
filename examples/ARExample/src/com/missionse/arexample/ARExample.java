package com.missionse.arexample;

import gl.GLCamera;
import gl.GLFactory;
import gl.scenegraph.MeshComponent;
import markerDetection.MarkerObjectMap;
import system.ArActivity;
import util.Vec;
import worldData.World;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.missionse.augmented.components.MeshComponentFactory;
import com.missionse.augmented.interfaces.OnWorldUpdateListener;
import com.missionse.augmented.setups.BasicMultiSetup;
import com.missionse.augmented.setups.DefaultSetup;

import commands.ui.CommandShowToast;


public class ARExample extends Activity implements
		ActionBar.OnNavigationListener, OnWorldUpdateListener {

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current dropdown position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_arexample);

		// Set up the action bar to show a dropdown list.
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		// Set up the dropdown list navigation in the action bar.
		actionBar.setListNavigationCallbacks(
		// Specify a SpinnerAdapter to populate the dropdown list.
				new ArrayAdapter<String>(actionBar.getThemedContext(),
						android.R.layout.simple_list_item_1,
						android.R.id.text1, new String[] {
								getString(R.string.title_section1),
								getString(R.string.title_section2),
								getString(R.string.title_section3), }), this);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Restore the previously serialized current dropdown position.
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Serialize the current dropdown position.
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
				.getSelectedNavigationIndex());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.arexample, menu);
		return true;
	}

	@Override
	public boolean onNavigationItemSelected(int position, long id) {
		// When the given dropdown item is selected, show its contents in the
		// container view.
		final DummySectionFragment fragment = new DummySectionFragment();
		final OnWorldUpdateListener l = this;
		//final Activity act = fragment.get;
		Bundle args = new Bundle();
		args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
		fragment.setArguments(args);
		
		switch(position){
		case 0:
			fragment.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					BasicMultiSetup s = new BasicMultiSetup(false);
					MeshComponent m = GLFactory.getInstance().newCube();
					m.setScale(new Vec(2,2,2));
					m.setColor(gl.Color.red());
					m.setPosition(Vec.getNewRandomPosInXYPlane(s.getCamera().getPosition(), 10, 25));
					m.setOnClickCommand(new CommandShowToast(s.getActivity(),"Malfuction"));
					//MarkerObject o = MarkerObjectFactory.createTimeoutMarker(s,4,m);
					
					//s.addMarker(o);
					s.addObject(m);
					//ArActivity.startWithSetup(fragment.getActivity(), s);
					//ArActivity.startWithSetup(fragment.getActivity(), new MultiMarkerSetup());
					//DefaultSetup s1 = new DefaultSetup();
					//MeshComponent e = MeshComponentFactory.createInfoBox(
					//		fragment.getActivity(), 
					//		s1.getCamera(), 
					//		"Information", 
					//		MeshComponentFactory.createHeadlineInfo("Insert information here."));
					//e.setPosition(Vec.getNewRandomPosInXYPlane(
					//		s.getCamera().getPosition(), 
					//		7, 14));
					//s1.addMeshToWorld(e);
					//s1.addObjToWorld(MeshComponentFactory.createDefaultInfo(fragment.getActivity(), s1.getCamera()));
					DefaultSetup s1 = new DefaultSetup();
					s1.addOnWorldUpdateListener(l);
					ArActivity.startWithSetup(fragment.getActivity(),s1);
					
				}				
			});
			break;
		case 1:
			break;
		case 2:
			break;
		default:
			break;
		}
			
		getFragmentManager().beginTransaction().replace(R.id.container, (Fragment)fragment).commit();
		return true;
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";
		private OnClickListener mOnClickListener;
		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_arexample_dummy,
					container, false);
			TextView dummyTextView = (TextView) rootView
					.findViewById(R.id.section_label);
			dummyTextView.setText(Integer.toString(getArguments().getInt(
					ARG_SECTION_NUMBER)));
			
			Button dummyButton = (Button) rootView.findViewById(R.id.section_button);
			dummyButton.setText(Integer.toString(getArguments().getInt(
					ARG_SECTION_NUMBER)) + " Action");
			
			if(mOnClickListener != null)
				dummyButton.setOnClickListener(mOnClickListener);
			
			return rootView;
		}
		
		public void setOnClickListener(OnClickListener tListener){
			mOnClickListener = tListener;
		}
	}

	@Override
	public void onWorldUpdate(Activity activity, GLCamera camera, World world,
			MarkerObjectMap markerMap) {
		world.add(MeshComponentFactory.createDefaultInfo(activity, camera));
		new CommandShowToast(activity, "ADDING TO WORLD").execute();
		Log.e("HELLLLO", "WORLD UPDATED");
	}

}

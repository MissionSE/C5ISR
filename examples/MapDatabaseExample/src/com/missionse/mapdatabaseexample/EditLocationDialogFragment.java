package com.missionse.mapdatabaseexample;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.missionse.mapdatabaseexample.tasks.EditLocationTask;

/**
 * Provides a fragment that is used to edit an existing map location.
 */
public class EditLocationDialogFragment extends DialogFragment {

	private EditText mEditText;
	private Button mCancelButton;
	private Button mOkButton;

	private Activity mActivity;
	private String mId;
	private String mName;
	private String mLatitude;
	private String mLongitude;

	private static final String ID = "id";
	private static final String NAME = "name";
	private static final String LATITUDE = "latitude";
	private static final String LONGITUDE = "longitude";

	/**
	 * Creates a new EditLocationDialogFragment, packing the latitude and longitude.
	 * @param id The unique id used to identify the location.
	 * @param name The name of the location.
	 * @param latitude The latitude of the location being created, in degrees.
	 * @param longitude The longitude of the location being created, in degrees.
	 * @return A new EditLocationDialogFragment.
	 */
	public static EditLocationDialogFragment newInstance(final int id, final String name, final double latitude, final double longitude) {
		EditLocationDialogFragment fragment = new EditLocationDialogFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(ID, id);
		bundle.putString(NAME, name);
		bundle.putDouble(LATITUDE, latitude);
		bundle.putDouble(LONGITUDE, longitude);
		fragment.setArguments(bundle);

		return fragment;
	}

	@Override
	public void onAttach(final Activity activity) {
		super.onAttach(activity);
		mActivity = activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mActivity = null;
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mId = Integer.toString(getArguments().getInt(ID));
		mName = getArguments().getString(NAME);
		mLatitude = Double.toString(getArguments().getDouble(LATITUDE));
		mLongitude = Double.toString(getArguments().getDouble(LONGITUDE));
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_location_dialog, container);
		mEditText = (EditText) view.findViewById(R.id.text_name_entry);
		mEditText.requestFocus();
		mEditText.setText(mName);
		mEditText.selectAll();

		TextView latitude = (TextView) view.findViewById(R.id.text_latitude);
		latitude.setText(getResources().getText(R.string.title_latitude) + " " + mLatitude);

		TextView longitude = (TextView) view.findViewById(R.id.text_longitude);
		longitude.setText(getResources().getText(R.string.title_longitude) + " " + mLongitude);

        mCancelButton = (Button) view.findViewById(R.id.cancel_button);
		mCancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				getDialog().dismiss();
			}
		});

		mOkButton = (Button) view.findViewById(R.id.ok_button);
		mOkButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				if (mActivity != null) {
					new EditLocationTask((Context) mActivity, (MapLocationListener) mActivity).execute(
							mId,
							mEditText.getText().toString(),
							mLatitude,
							mLongitude);
				}

				getDialog().dismiss();
			}
		});

		getDialog().getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getDialog().setTitle(R.string.title_edit_location);

		return view;
	}
}

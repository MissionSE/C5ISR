package com.missionse.mapdatabaseexample;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.missionse.mapdatabaseexample.tasks.CreateLocationTask;

/**
 * Provides a fragment that is used to create a new map location.
 */
public class CreateLocationDialogFragment extends DialogFragment implements TextWatcher {

	private EditText mEditText;
	private Button mCancelButton;
	private Button mOkButton;

	private Activity mActivity;
	private String mLatitude;
	private String mLongitude;

	private static final String LATITUDE = "latitude";
	private static final String LONGITUDE = "longitude";

	/**
	 * Creates a new CreateLocationDialogFragment, packing the latitude and longitude.
	 * @param latitude The latitude of the location being created, in degrees.
	 * @param longitude The longitude of the location being created, in degrees.
	 * @return A new CreateLocationDialogFragment.
	 */
	public static CreateLocationDialogFragment newInstance(final double latitude, final double longitude) {
		CreateLocationDialogFragment fragment = new CreateLocationDialogFragment();
		Bundle bundle = new Bundle();
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

		mLatitude = Double.toString(getArguments().getDouble(LATITUDE));
		mLongitude = Double.toString(getArguments().getDouble(LONGITUDE));
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_location_dialog, container);
		mEditText = (EditText) view.findViewById(R.id.text_name_entry);
		mEditText.addTextChangedListener(this);
		mEditText.requestFocus();

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
					new CreateLocationTask((Context) mActivity, (MapLocationListener) mActivity).execute(
							mEditText.getText().toString(),
							mLatitude,
							mLongitude);
				}

				getDialog().dismiss();
			}
		});
		setOkButtonState();

		getDialog().getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getDialog().setTitle(R.string.title_create_location);

		return view;
	}

	private void setOkButtonState() {
		if (mEditText != null && mOkButton != null) {
			if (mEditText.getText().length() > 0) {
				mOkButton.setEnabled(true);
			} else {
				mOkButton.setEnabled(false);
			}
		}
	}

	@Override
	public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {
	}

	@Override
	public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
	}

	@Override
	public void afterTextChanged(final Editable s) {
		setOkButtonState();
	}
}

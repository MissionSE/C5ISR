package com.missionse.kestrelweather.kestrel;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.missionse.kestrelweather.R;

import java.util.ArrayList;
import java.util.List;

public class KestrelSimulationSettingsFragment extends Fragment {
	private SharedPreferences mSimulationPreferences = null;

	private Button mEditButton;
	private Button mSaveButton;
	private Button mCancelButton;

	private Spinner mPressureTrendSpinner;

	private List<EditText> mReportEntryFields = new ArrayList<EditText>();

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getActivity() != null) {
			mSimulationPreferences = getActivity().getSharedPreferences(
				KestrelSimulationSharedPreferences.SIMULATION_PREFERENCES, 0);
		}
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_kestrel_simulation_settings, container, false);

		if (contentView != null) {
			mEditButton = (Button) contentView.findViewById(R.id.kestrel_simulation_settings_edit_btn);
			mEditButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(final View view) {
					setAllFieldsEnabled(true);
					mEditButton.setEnabled(false);
					mSaveButton.setEnabled(true);
					mCancelButton.setEnabled(true);
				}
			});
			mSaveButton = (Button) contentView.findViewById(R.id.kestrel_simulation_settings_save_btn);
			mSaveButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(final View view) {
					saveCurrentValues();
					setAllFieldsEnabled(false);
					mEditButton.setEnabled(true);
					mSaveButton.setEnabled(false);
					mCancelButton.setEnabled(false);

				}
			});
			mCancelButton = (Button) contentView.findViewById(R.id.kestrel_simulation_settings_cancel_btn);
			mCancelButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(final View view) {
					setFieldValuesToSavedValues();
					setAllFieldsEnabled(false);
					mEditButton.setEnabled(true);
					mSaveButton.setEnabled(false);
					mCancelButton.setEnabled(false);
				}
			});

			mPressureTrendSpinner = (Spinner) contentView.findViewById(R.id.kestrel_pressure_trend_spinner);
			if (getActivity() != null) {
				ArrayAdapter<CharSequence> pressureTrendAdapter = ArrayAdapter.createFromResource(getActivity(),
					R.array.kestrel_simulation_pressure_trend_entries, android.R.layout.simple_spinner_item);
				pressureTrendAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				mPressureTrendSpinner.setAdapter(pressureTrendAdapter);
			}

			mReportEntryFields.add((EditText) contentView.findViewById(R.id.kestrel_temperature_edit_text));
			mReportEntryFields.add((EditText) contentView.findViewById(R.id.kestrel_humidity_edit_text));
			mReportEntryFields.add((EditText) contentView.findViewById(R.id.kestrel_pressure_edit_text));
			mReportEntryFields.add((EditText) contentView.findViewById(R.id.kestrel_heat_index_edit_text));
			mReportEntryFields.add((EditText) contentView.findViewById(R.id.kestrel_wind_speed_edit_text));
			mReportEntryFields.add((EditText) contentView.findViewById(R.id.kestrel_wind_direction_edit_text));
			mReportEntryFields.add((EditText) contentView.findViewById(R.id.kestrel_wind_chill_edit_text));
			mReportEntryFields.add((EditText) contentView.findViewById(R.id.kestrel_dew_point_edit_text));
		}

		setInitialButtonAndFieldStates();
		setFieldValuesToSavedValues();

		return contentView;
	}

	private void setInitialButtonAndFieldStates() {
		mEditButton.setEnabled(true);
		mSaveButton.setEnabled(false);
		mCancelButton.setEnabled(false);
		setAllFieldsEnabled(false);
	}

	private void setAllFieldsEnabled(final boolean enabled) {
		for (EditText editText : mReportEntryFields) {
			editText.setEnabled(enabled);
		}
		mPressureTrendSpinner.setEnabled(enabled);
	}

	private void setFieldValuesToSavedValues() {
		for (EditText editText : mReportEntryFields) {
			if (editText.getId() == R.id.kestrel_temperature_edit_text) {
				float savedTemperature = mSimulationPreferences.getFloat(
					KestrelSimulationSharedPreferences.KESTREL_TEMPERATURE, KestrelSimulationSharedPreferences.NONSENSE_FLOAT);
				if (savedTemperature != KestrelSimulationSharedPreferences.NONSENSE_FLOAT) {
					editText.setText("" + savedTemperature);
				}
			} else if (editText.getId() == R.id.kestrel_humidity_edit_text) {
				int savedHumidity = mSimulationPreferences.getInt(
					KestrelSimulationSharedPreferences.KESTREL_HUMIDITY, KestrelSimulationSharedPreferences.NONSENSE_INT);
				if (savedHumidity != KestrelSimulationSharedPreferences.NONSENSE_INT) {
					editText.setText("" + savedHumidity);
				}
			} else if (editText.getId() == R.id.kestrel_pressure_edit_text) {
				float savedPressure = mSimulationPreferences.getFloat(
					KestrelSimulationSharedPreferences.KESTREL_PRESSURE, KestrelSimulationSharedPreferences.NONSENSE_FLOAT);
				if (savedPressure != KestrelSimulationSharedPreferences.NONSENSE_FLOAT) {
					editText.setText("" + savedPressure);
				}
			} else if (editText.getId() == R.id.kestrel_heat_index_edit_text) {
				float savedHeatIndex = mSimulationPreferences.getFloat(
					KestrelSimulationSharedPreferences.KESTREL_HEAT_IDX, KestrelSimulationSharedPreferences.NONSENSE_FLOAT);
				if (savedHeatIndex != KestrelSimulationSharedPreferences.NONSENSE_FLOAT) {
					editText.setText("" + savedHeatIndex);
				}
			} else if (editText.getId() == R.id.kestrel_wind_speed_edit_text) {
				float savedWindSpeed = mSimulationPreferences.getFloat(
					KestrelSimulationSharedPreferences.KESTREL_WIND_SPD, KestrelSimulationSharedPreferences.NONSENSE_FLOAT);
				if (savedWindSpeed != KestrelSimulationSharedPreferences.NONSENSE_FLOAT) {
					editText.setText("" + savedWindSpeed);
				}
			} else if (editText.getId() == R.id.kestrel_wind_direction_edit_text) {
				int savedWindDirection = mSimulationPreferences.getInt(
					KestrelSimulationSharedPreferences.KESTREL_WIND_DIR, KestrelSimulationSharedPreferences.NONSENSE_INT);
				if (savedWindDirection != KestrelSimulationSharedPreferences.NONSENSE_INT) {
					editText.setText("" + savedWindDirection);
				}
			} else if (editText.getId() == R.id.kestrel_wind_chill_edit_text) {
				float savedWindChill = mSimulationPreferences.getFloat(
					KestrelSimulationSharedPreferences.KESTREL_WIND_CHILL, KestrelSimulationSharedPreferences.NONSENSE_FLOAT);
				if (savedWindChill != KestrelSimulationSharedPreferences.NONSENSE_FLOAT) {
					editText.setText("" + savedWindChill);
				}
			} else if (editText.getId() == R.id.kestrel_dew_point_edit_text) {
				float savedDewPoint = mSimulationPreferences.getFloat(
					KestrelSimulationSharedPreferences.KESTREL_DEW_PT, KestrelSimulationSharedPreferences.NONSENSE_FLOAT);
				if (savedDewPoint != KestrelSimulationSharedPreferences.NONSENSE_FLOAT) {
					editText.setText("" + savedDewPoint);
				}
			}
		}

		int savedPressureTrend = mSimulationPreferences.getInt(KestrelSimulationSharedPreferences.KESTREL_PRESSURE_TREND,
			KestrelSimulationSharedPreferences.NONSENSE_INT);
		if (savedPressureTrend == KestrelSimulationSharedPreferences.NONSENSE_INT) {
			mPressureTrendSpinner.setSelection(savedPressureTrend);
		} else {
			mPressureTrendSpinner.setSelection(0);
		}
	}

	private void saveCurrentValues() {
		SharedPreferences.Editor editor = mSimulationPreferences.edit();
		for (EditText editText : mReportEntryFields) {
			if (editText.getId() == R.id.kestrel_temperature_edit_text) {
				if (editText.getText() != null) {
					editor.putFloat(KestrelSimulationSharedPreferences.KESTREL_TEMPERATURE,
						Float.valueOf(editText.getText().toString()));
				}
			} else if (editText.getId() == R.id.kestrel_humidity_edit_text) {
				if (editText.getText() != null) {
					editor.putInt(KestrelSimulationSharedPreferences.KESTREL_HUMIDITY,
						Integer.valueOf(editText.getText().toString()));
				}
			} else if (editText.getId() == R.id.kestrel_pressure_edit_text) {
				if (editText.getText() != null) {
					editor.putFloat(KestrelSimulationSharedPreferences.KESTREL_PRESSURE,
						Float.valueOf(editText.getText().toString()));
				}
			} else if (editText.getId() == R.id.kestrel_heat_index_edit_text) {
				if (editText.getText() != null) {
					editor.putFloat(KestrelSimulationSharedPreferences.KESTREL_HEAT_IDX,
						Float.valueOf(editText.getText().toString()));
				}
			} else if (editText.getId() == R.id.kestrel_wind_speed_edit_text) {
				if (editText.getText() != null) {
					editor.putFloat(KestrelSimulationSharedPreferences.KESTREL_WIND_SPD,
						Float.valueOf(editText.getText().toString()));
				}
			} else if (editText.getId() == R.id.kestrel_wind_direction_edit_text) {
				if (editText.getText() != null) {
					editor.putInt(KestrelSimulationSharedPreferences.KESTREL_WIND_DIR,
						Integer.valueOf(editText.getText().toString()));
				}
			} else if (editText.getId() == R.id.kestrel_wind_chill_edit_text) {
				if (editText.getText() != null) {
					editor.putFloat(KestrelSimulationSharedPreferences.KESTREL_WIND_CHILL,
						Float.valueOf(editText.getText().toString()));
				}
			} else if (editText.getId() == R.id.kestrel_dew_point_edit_text) {
				if (editText.getText() != null) {
					editor.putFloat(KestrelSimulationSharedPreferences.KESTREL_DEW_PT,
						Float.valueOf(editText.getText().toString()));
				}
			}
		}

		editor.putInt(KestrelSimulationSharedPreferences.KESTREL_PRESSURE_TREND,
			mPressureTrendSpinner.getSelectedItemPosition());

		editor.commit();
	}
}

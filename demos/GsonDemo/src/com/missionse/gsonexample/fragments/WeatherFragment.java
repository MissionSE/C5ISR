package com.missionse.gsonexample.fragments;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.missionse.gsonexample.R;
import com.missionse.gsonexample.model.weather.WeatherData;
import com.missionse.gsonexample.util.HttpHandler;
/**
 * Display the weather information.
 * @author rvieras
 *
 */
public class WeatherFragment extends Fragment {
	
	private static final double NINE = 9;
	private static final double FIVE = 5;
	private static final double KELVIN_TO_CELCIUS = 273.15;
	private static final double C_TO_F_RATIO = NINE / FIVE;
	private static final double C_TO_F = 32;
	
	private Spinner mSpinner;
	private ImageView mImageView;
	private TextView mConditionName;
	private TextView mConditionDescription;
	private TextView mTempature;

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_weather, container, false);
	
		mSpinner = (Spinner) contentView.findViewById(R.id.weather_city_spinner);
		mSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				update();
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		mImageView = (ImageView) contentView.findViewById(R.id.weather_icon);
		mConditionName = (TextView) contentView.findViewById(R.id.condition_name);
		mConditionDescription = (TextView) contentView.findViewById(R.id.condition_description);
		mTempature = (TextView) contentView.findViewById(R.id.tempature_celius);
		
		update();
		
		return contentView;
	}
	
	private void update() {
		(new GetDataClass(this, mSpinner)).execute("");
	}
	
	private void updateGui(WeatherData weather) {
		mConditionName.setText(weather.getConditions().get(0).getName());
		mConditionDescription.setText(weather.getConditions().get(0).getDescription());
		
		double temp = weather.getMeasurements().getTempature();
		temp = temp - KELVIN_TO_CELCIUS;
		temp = (temp * C_TO_F_RATIO) + C_TO_F;
		mTempature.setText(temp + "°F");
		
		mImageView.setImageResource(R.drawable.weathersunnyday);
	}
	
	/**
	 * Internal class/thread to handle the execution of http. 
	 */
	private class GetDataClass extends AsyncTask<String, String, String> {

		private WeatherFragment mActivity;
		private Spinner mSpinner;
		private WeatherData mWeatherData;

		public GetDataClass(WeatherFragment activity, Spinner spinner) {
			this.mActivity = activity;
			this.mSpinner = spinner;
		}
		
		@Override
		protected String doInBackground(String... params) {
			StringBuilder sb = new StringBuilder();
			sb.append("http://api.openweathermap.org/data/2.5/weather?q=");
			sb.append(mSpinner.getSelectedItem().toString().trim());
			sb.append(",us");
			//whitespace is not acceptable in urls so need to replace with %20 which is the url
			//Equivalent to a space
			String url = sb.toString().replace(" ", "%20"); 
			Log.d("WeatherFragment", "URL>: " + url);
			String jsonResponse = HttpHandler.getResponse(url);
			Log.d("WeatherFragment", jsonResponse);
			Gson gson = new Gson();
			mWeatherData = (WeatherData) gson.fromJson(jsonResponse, WeatherData.class);
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			mActivity.updateGui(mWeatherData);
		}
	}

}

package com.missionse.kestrelweather.database.model.tables;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.missionse.kestrelweather.database.model.Entity;

import java.util.Map;

/**
 * Provides the model for OpenWeather.
 */
@DatabaseTable(daoClass = com.missionse.kestrelweather.database.model.tables.manipulators.OpenWeatherTable.class)
public class OpenWeather extends Entity {
	@DatabaseField(columnName = "conditioncode")
	private int mConditionCode;

	@DatabaseField(columnName = "description")
	private String mDescription;

	@DatabaseField(columnName = "name")
	private String mName;

	@DatabaseField(columnName = "country")
	private String mCountry;

	/**
	 * Default constructor.  Needed for API.
	 */
	public OpenWeather() {
		mConditionCode = 0;
		mDescription = "";
		mName = "";
		mCountry = "";
	}

	/**
	 * Gets the condition code.
	 * @return Return condition code.
	 */
	public int getConditionCode() {
		return mConditionCode;
	}

	/**
	 * Sets the condition code.
	 * @param conditionCode Condition code.
	 */
	public void setConditionCode(int conditionCode) {
		mConditionCode = conditionCode;
	}

	/**
	 * Gets the description.
	 * @return Description.
	 */
	public String getDescription() {
		return mDescription;
	}

	/**
	 * Sets the description.
	 * @param description Description.
	 */
	public void setDescription(String description) {
		mDescription = description;
	}

	/**
	 * Gets the name.
	 * @return The name.
	 */
	public String getName() {
		return mName;
	}

	/**
	 * Sets the name.
	 * @param name Name.
	 */
	public void setName(final String name) {
		mName = name;
	}

	/**
	 * Gets the country.
	 * @return The country.
	 */
	public String getCountry() {
		return mCountry;
	}

	/**
	 * Sets the country.
	 * @param country The country.
	 */
	public void setCountry(final String country) {
		mCountry = country;
	}

	@Override
	public Map<String, String> toMap() {
		Map<String, String> map = super.toMap();
		map.put("conditioncode", Integer.toString(mConditionCode));
		map.put("description", mDescription);
		map.put("name", mName);
		map.put("country", mCountry);

		return map;
	}

	@Override
	public void populate(JsonObject json) {
		super.populate(json);
		setName(json);
		setCountry(json);
		setDescription(json);
		setConditionCode(json);
	}

	private void setName(final JsonObject json) {
		JsonElement name = json.get("name");
		if (name != null) {
			setName(name.getAsString());
		} else {
			setName("");
		}
	}

	private void setCountry(final JsonObject json) {
		JsonElement country = json.get("country");
		if (country != null) {
			setCountry(country.getAsString());
		} else {
			setCountry("");
		}
	}

	private void setDescription(final JsonObject json) {
		JsonElement description = json.get("description");
		if (description != null) {
			setDescription(description.getAsString());
		} else {
			setDescription("");
		}
	}

	private void setConditionCode(final JsonObject json) {
		JsonElement conditionCode = json.get("conditioncode");
		if (conditionCode != null) {
			setConditionCode(conditionCode.getAsInt());
		} else {
			setConditionCode(0);
		}
	}
}

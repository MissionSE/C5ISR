package com.missionse.kestrelweather.database.model.tables;

import com.google.gson.JsonObject;
import com.j256.ormlite.field.DatabaseField;
import com.missionse.kestrelweather.database.model.Entity;

import java.util.Map;

/**
 * Created by rvieras on 2/24/14.
 */
public class OpenWeather extends Entity {
	@DatabaseField(columnName = "conditioncode")
	private int mConditionCode;

	@DatabaseField(columnName = "description")
	private String mDescription;

	public OpenWeather() {
		mConditionCode = 0;
		mDescription = "";
	}

	/**
	 * Getter.
	 * @return Return condition code.
	 */
	public int getConditionCode() {
		return mConditionCode;
	}

	/**
	 * Setter.
	 * @param conditionCode Condition code.
	 */
	public void setConditionCode(int conditionCode) {
		mConditionCode = conditionCode;
	}

	/**
	 * Getter.
	 * @return Description.
	 */
	public String getDescription() {
		return mDescription;
	}

	/**
	 * Setter.
	 * @param description Description.
	 */
	public void setDescription(String description) {
		mDescription = description;
	}

	@Override
	public Map<String, String> toMap() {
		Map<String, String> map = super.toMap();
		map.put("conditioncode", Integer.toString(mConditionCode));
		map.put("description", mDescription);

		return map;
	}

	@Override
	public void populate(JsonObject json) {
		super.populate(json);
		setConditionCode((json.get("conditioncode") == null ? 0 : json.get("conditioncode").getAsInt()));
		setDescription((json.get("description") == null ? "" : json.get("description").getAsString()));
	}
}

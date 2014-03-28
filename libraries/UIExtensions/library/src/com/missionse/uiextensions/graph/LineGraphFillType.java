package com.missionse.uiextensions.graph;

/**
 * Describes the fill type of a line.
 */
public enum LineGraphFillType {
	BACKSLASH, FORWARDSLASH, HORIZONTAL, VERTICAL;

	public static LineGraphFillType getNext(LineGraphFillType type) {
		if (type.ordinal() + 1 < LineGraphFillType.values().length) {
			return LineGraphFillType.values()[type.ordinal() + 1];
		} else {
			return LineGraphFillType.values()[0];
		}
	}
}

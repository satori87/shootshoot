package com.bg.badgame.core;

public class DirectionUtility {
	public enum CardinalDirection {
		NORTH,
		NORTHEAST,
		EAST,
		SOUTHEAST,
		SOUTH,
		SOUTHWEST,
		WEST,
		NORTHWEST,
		NO_DIRECTION
	}		
	public static float getRotationAngle(CardinalDirection direction) {
		switch (direction) {
		case NORTH:
			return 270;
		case NORTHEAST:
			return 225;
		case EAST:
			return 180;
		case SOUTHEAST:
			return 135;
		case SOUTH:
			return 90;
		case SOUTHWEST:
			return 45;
		case WEST:
			return 0;
		case NORTHWEST:
			return 315;
		default:
			break;
		}
		return 0;
	}
}

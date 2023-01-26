package com.gary.backendv2.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter
public class Location {
	double longitude;
	double latitude;

	protected Location() {}

	private Location(double longitude, double latitude) {
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public static Location of(double longitude, double latitude) {
		return new Location(longitude, latitude);
	}

	public static Location ofReversed(double latitude, double longitude) {
		return new Location(latitude, longitude);
	}

	public static Location undefined() {
		return new Location(0, 0);
	}

	public static Location defaultLocation() {
		return new Location(20.977728377285462, 52.21934563858324);
	}
}

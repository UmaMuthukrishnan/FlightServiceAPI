package com.afkl.exercises.spring.locations;

import lombok.Value;

//@Value
public class Coordinates {

    private double latitude, longitude;

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
    
    

}

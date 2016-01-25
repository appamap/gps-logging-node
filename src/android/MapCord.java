package com.geteventro.plugin;

public class MapCord {

	private double latitude=0.0;
	private double longitude=0.0;

	public MapCord()   //constructor
	{

	}

	public MapCord(double latitudeParm, double longitudeParm) {

		this.latitude=latitudeParm;
		this.longitude=longitudeParm;
	}

	public 	double getLatitude() {
		return latitude;
	}

	public void setLatitude(	double latitude) {
		this.latitude = latitude;
	}

	public 	double getLongitude() {
		return longitude;
	}

	public void setLongitude(	double longitude) {
		this.longitude = longitude;
	}
}

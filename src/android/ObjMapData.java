package com.geteventro.plugin;

public class ObjMapData {

	/*
	 * 
	private String placeStyleURL;
	private String lineColor;
	private String lineWidth;
	private String polyColor;
	private String polyWidth;
	private String polyOutline;
	private String polyFill;
	 */



	public float getDistanceTo() {
		return distanceTo;
	}

	public void setDistanceTo(float distanceTo) {
		this.distanceTo = distanceTo;
	}

	private float distanceTo;
	private String placeName;
	private String placeDescription;
	private String styleID;
	private String placeType;
	private String placeCord;

	//styles
	private String lineColor;
	private String lineWidth;
	private String polyColor;
	private String polyWidth;	
	private String polyOutline;
	private String polyFill;

	//extensions
	private String extName;
	private String extCategory;
	private String extMediafile;
	private String extIconfile;
	private String extFacebook;
	private String extTwitter;
	private String extWeb;
	private String extEmail;
	private String extTel;

	private double avCenterLat;
	private double avCenterLong;
	private double lastX;
	private double lastY;

	private boolean isLocked;

	public ObjMapData()   //constructor
	{

	}

	public ObjMapData(String appName) 
	{

	}

	public String getPlaceName()  //placeName
	{
		return placeName;
	}

	public void setPlaceName(String value) 
	{
		placeName = value;
	}

	public String getPlaceDescription()  //placeDescription
	{
		return placeDescription;
	}

	public void setPlaceDescription(String value) 
	{
		placeDescription = value;
	}
	public String getStyleID()  //placeStyleURL
	{
		return styleID;
	}

	public void setStyleID(String value) 
	{
		styleID = value;
	}
	public String getPlaceType()  //placeType
	{
		return placeType;
	}

	public void setPlaceType(String value) 
	{
		placeType = value;
	}

	public String getPlaceCord()  //placeCord
	{
		return placeCord;
	}

	public void setPlaceCord(String value) 
	{
		placeCord = value;
	}
	/////////////////////

	public String getLineColor()  //lineColor
	{
		return lineColor;
	}

	public void setLineColor(String value) 
	{
		lineColor = value;
	}

	public String getLineWidth()  //lineWidth
	{
		return lineWidth;
	}

	public void setLineWidth(String value) 
	{
		lineWidth = value;
	}

	public String getPolyColor()  //polyColor
	{
		return polyColor;
	}

	public void setPolyColor(String value) 
	{
		polyColor = value;
	}

	public String getPolyWidth()  //polyWidth
	{
		return polyWidth;
	}

	public void setPolyWidth(String value) 
	{
		polyWidth = value;
	}

	public String getPolyOutline()  //polyOutline
	{
		return polyOutline;
	}

	public void setPolyOutline(String value) 
	{
		polyOutline = value;
	}

	public String getPolyFill()  //polyOutline
	{
		return polyFill;
	}

	public void setPolyFill(String value) 
	{
		polyFill = value;
	}

	/////////////////////////////////


	public String getExtName()  //extName
	{
		return extName;
	}

	public void setExtName(String value) 
	{
		extName = value;
	}

	public String getExtCategory()  //extCategory
	{
		return extCategory;
	}

	public void setExtCategory(String value) 
	{
		extCategory = value;
	}

	public String getExtMediafile()  //extMediafile
	{
		return extMediafile;
	}

	public void setExtMediafile(String value) 
	{
		extMediafile = value;
	}

	public String getExtIconfile()  //extIconfile
	{
		return extIconfile;
	}

	public void setExtIconfile(String value) 
	{
		extIconfile = value;
	}

	public String getExtFacebook()  //extFacebook
	{
		return extFacebook;
	}

	public void setExtFacebook(String value) 
	{
		extFacebook = value;
	}

	public String getExtTwitter()  //extTwitter
	{
		return extTwitter;
	}

	public void setExtTwitter(String value) 
	{
		extTwitter = value;
	}

	public String getExtWeb()  //extWeb
	{
		return extWeb;
	}

	public void setExtWeb(String value) 
	{
		extWeb = value;
	}

	public String getExtEmail()  //extEmail
	{
		return extEmail;
	}

	public void setExtEmail(String value) 
	{
		extEmail = value;
	}

	public String getExtTel()  //extTel
	{
		return extTel;
	}

	public void setExtTel(String value) 
	{
		extTel = value;
	}

	///////////////////////////////////


	public double getAvCenterLat()  //avCenterLat
	{
		return avCenterLat;
	}

	public void setAvCenterLat(double value) 
	{
		avCenterLat = value;
	}

	public double getAvCenterLong()  //avCenterLong
	{
		return avCenterLong;
	}

	public void setAvCenterLong(double value) 
	{
		avCenterLong = value;
	}

	public double getLastX()  //lastX
	{
		return lastX;
	}

	public void setLastX(double value) 
	{
		lastX = value;
	}

	public double getLastY()  //lastY
	{
		return lastY;
	}

	public void setLastY(double value) 
	{
		lastY = value;
	}

	public boolean getLocked()  //locked
	{
		return isLocked;
	}

	public void setLocked(boolean value) 
	{
		isLocked = value;
	}

}

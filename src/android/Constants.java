package com.geteventro.plugin;

public interface Constants {

    
    //for gps logging
    public static final String AWSConnectionString = "http://52.88.57.143/update";
    public static final int GPSWalkOutRadius = 5;
    public static final int GPSBoundryTrigger = 25;
    public static final int horizontalAccuracy = 100;
    
    
    
    
	//shell
	public static final String hexHeaderBack = "#90000000";
	public static final String hexHeaderBackStack = "#90000000";

	public static final int CENTER_OPTION =1;
	public static final int GPS_OPTION =2;
	public static final int PLAYER_OPTION =3;
	public static final int CATEGORY_OPTION =4;
	public static final String INTENT_SERVICE_PACKAGE = "appamap.donaghy.liam";

	//AR ViewSkin

	public static final double AR_SCALE_UNDERLAY= 1.6;
	public static final int BACKGROUND_ALPHA= 150;
	public static final int BORDER_ALPHA= 200;
	public static final float BORDER_STROKE= 3.5f;
	public static final int TEXT_SIZE_DIVIDER= 8;
	public static final int TWO= 2;
	public static final int MAX_AR_WIDTH = 330;
	public static final float IMG_SCALER = 2.95f;
	public static final int IMG_PADDING = 10;
	public static final int BIT_PADDING = 5;
	public static final float TWO_POINT_FIVE = 2.5f;
	public static final float TWO_POINT_TWO= 2.2f;
	public static final float FOUR_POINT_FIVE= 4.5f;


	public static final String APP_KEY ="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnD5ZZ6eSSEG8NU+mlrE9xyerD3dR3Kfjb0LROG1EMO5AqgLxfxXf7uWg595ao8qkd7OpFSbJ1cZ+/qYkJTT9UPnNqydSP1vyk5OjZvhrX4pmakTRCbAr+NeIT438pLoCujsJ4d4dgm8YI9bzYiN472hbmkmdxwJ5a+Mdi2RiCUX95/ZxTBuIJ/PBuvRLxI8z5rmYrd8VhsTG+wjeKbbDmc6MIPX1rcNwbF8Sn6mIdkf+6HsDwawZNEycPEER/cgoDV0RVz+ZGWtEhovuxKw1O+plTVqQNyAhMC3UC11xZuTtZumXKypNE1NfrZLWzAjuJyXL1f7ghhLjVNx3LdABdQIDAQAB";
    public static final int EXPANSION_VERSION= 12;  //316799305
	public static final long EXPANSION_SIZE= 316799305L;
	public static final String LICENSE_CHECK = "license_check";

	//support

	public static final String supportEmial = "admin@appamap.com";

	//xml related

	public static final String XML_COMPILE_PATH = "DirectionsResponse/route/leg/step";

	//intents

	public static final String GMAIL_LOWER = "gmail.com";
	public static final String GMAIL_UPPER = "GMAIL.COM";
	public static final String ROUTE_ERROR_TITLE = "Route Error";
	public static final String ROUTE_ERROR_MESSAGE = "Unable to create route";
	public static final String TIMER_VIDEO_DELAY = "Video is delayed";
	public static final int MAP_ICON_SIZE_DIVIDER = 5;
	public static final String CATEGORY_ALL = "All";
	public static final String FACE_BOOK = "facebook://facebook.com/wall";
	public static final String FACE_PACKAGE = "com.facebook.katana";
	public static final String FACE_NOT_INSTALLED = "Facebook unavailable";
	public static final String TWITTER_URI = "";
	public static final String TWITTER_PACKAGE = "com.twitter.android";
	public static final String TWITTER_NOT_INSTALLED = "Facebook unavailable";

	public static final String INTENT_EMAIL = "INTENT_EMAIL";
	public static final String INTENT_PHONE = "INTENT_PHONE";
	public static final String INTENT_EMPTY = "INTENT_EMPTY";
	public static final int INTENT_SECOND = 1000;
	public static final int INTENT_LENGTH = 3000;

	//progress


	public static final int ANIMATION_DURATION = 1500;
	public static final int MAP_ICONS_SIZE= 125;

	//draw routes

	public static final String SD_DIRECTORY = "/APPAMAP/";
	public static final String SD_FILENAME = "route.txt";
	public static final String SD_ENCODING = "iso-8859-1"; 

	//contrix

	//NSString *partOne = @"http://maps.googleapis.com/maps/api/directions/json?origin="; concentrix
	//NSString *partOne = @"http://maps.googleapis.com/maps/api/directions/json?&dirflg=w&origin=";

	public static final String GOOGLE_ROUTE_API = "http://maps.googleapis.com/maps/api/directions/xml?&dirflg=w&origin="; 
	public static final String GOOGLE_ROUTE_API_PART_TWO = "&sensor=true&units=metric&mode=walking&region=gb"; //walking
	//public static final String GOOGLE_ROUTE_API_PART_TWO = "&sensor=true&region=gb"; //car

	//listing available

	public static final String GENERIC_LISTING_UNAVAILABLE = "No Listing";
	public static final String UNKNOWN_QR_CODE = "Unknown Code";

	//global views

	public static final int CONTENT_VIEW_ID = 10101010;

	//iconn text menu

	public static final int TOUR_RUNNING_INDICATOR = 1500;
	public static final int LIST_PREFERED_HEIGHT = 65;
	public static final int TEXTVIEW_IMG_PADDING = 25;
	public static final int TEXTVIEW_WORDING_PADDING = 5;
	public static final int TEXTVIEW_BITMAP_SIZE = 150;
	public static final int TEXTVIEW_FONTSIZE = 30;

	
	//table image sizes

	public static final int TABLE_IMG_DIM = 100;

	//guide tags

	public static final int FACEBOOK = 2;
	public static final int TWITTER = 3;
	public static final int WEBSITE = 1;
	public static final int TELEPHONE = 4;
	public static final int EMAIL = 5;
	public static final int VIDEO = 6;
	public static final int DIRECTIONS = 7;
	public static final int RUNSCREENID = 7;
	public static final int FAQ = 8;

	public static final int FACEBOOK_INTRO = 3;
	public static final int TWITTER_INTRO = 4;
	public static final int WEBSITE_INTRO = 2;
	public static final int TELEPHONE_INTRO = 5;
	public static final int EMAIL_INTRO = 6;
	public static final int VIDEO_INTRO = 7;
	public static final int RUNSCREENID_INTRO = 1;
	public static final int FAQ_INTRO = 8;


	//tab navigation

	public static final String INFO = "INFO";
	public static final String MAP = "MAP";
	public static final String CAMERA = "CAMERA";
	public static final String GUIDE = "GUIDE";
	public static final String SCAN = "SCAN";
	public static final String KEY = "KEY";
	public static final String INTENT = "INTENT";

	//map buttons

	public static final String CENTER_ON = "center.png";
	public static final String CENTER_OFF = "noCenter.png";
	public static final String KEY_ON = "key.png";
	public static final String KEY_OFF = "noKey.png";
	public static final String GPS_ON = "gps.png";
	public static final String GPS_OFF = "noGPS.png";
	public static final String PLAY_ON = "play.png";
	public static final String PLAY_OFF = "noPlay.png";
	public static final String GPS_TEXT = "GPS accuracy (m)";
	public static final String MAP_KEY = "0rsQAVZWPyQ7CtkE1sRKwPeABI-IbkdT6xIYcPg";

	//map general

	public static final int SCROLL_GPS_STEP = 10;
	public static final int SCROLL_GPS_MAX = 150;
	public static final int MAP_METERS_IN_KM = 1000;
	public static final int E6 = 1000000;
	public static final String DECIMAL_FORMAT = "#.00";

	//fragments

	public static final String INTRO_FRAG = "INTRO_FRAG";
	public static final String PLAYER_FRAG = "PLAYER_FRAG";
	public static final String MAP_FRAG = "MAP_FRAG";
	public static final String CAM_FRAG = "CAM_FRAG";
	public static final String KILLDIALOG = "KILLDIALOG";
	public static final String GUIDE_FRAG = "GUIDE_FRAG";
	public static final String BROWSER_FRAG = "BROWSER_FRAG";
	public static final String SCAN_FRAG = "SCAN_FRAG";
	public static final String INTENT_FRAG = "INTENT_FRAG";
	public static final String TOUR_FRAG = "TOUR_FRAG";

	//scanner

	public static final String TEL_LOWER = "tel:";
	public static final String TEL = "TEL:";
	public static final String MAIL = "SMTP:";
	public static final String HTTP = "HTTP:";
	public static final String SMS = "SMSTO:";
	public static final String PLAYER = "PLAYER:";
	public static final String ROUTE = "ROUTE:";
	public static final String TRIP_LOCK = "TRIP_LOCK";

	//AR vars

	public static final int ICON_SIZE = 125;
	public static final int DEGREE_RANGE =60;
	public static final int STACK_SPACE = 90;
	public static final int SHUNT_BOOST = 35;
	public static final int northSplitOne = 360;
	public static final int northSplitTwo = 0;
	public static final int accelXvalMult = 12;
	public static final int minRand = 5;
	public static final int maxRand = 15;
	public static final int MaxTiltAR = 20;
	public static final int MinTiltAR = 5;
	public static final int FlatARCenter = 100;
	public static final int RotationARDuration = 1000;
	public static final int TranslationARDuration = 1000;
	public static final int ThreadARDuration = 1000;
	public static final int PlaceImgSizeWidth = 80;
	public static final int PlaceImgSizeHeight = 80;
	public static final int UnderlayImgSizeWidth = 257;
	public static final int UnderlayImgSizeHeight = 157;
	public static final float UnderlayHeightDivider = (float) 4.2;
	public static final float UnderlayWidthDivider = (float) 2.5;
	public static final int UnderlayAlpha = 204; 
	public static final int BitMapSpacer = 15; 
	public static final String ARUnderlay = "speech.png";
	public static final int Ellipse = 13;
	public static final int EllipseTable = 35;
	public static final String CATEGORY_TEXT = "Category";
	public static final float MID_SCREEN = (float) 1.5;
	public static final float END_SCREEN = (float) 2.5;

	//dialogs

	//media type

	public static final String MP3 = "mp3";
	public static final String MP4 = "mp4";

	//kml map filters

	public static final String POINT = "point";
	public static final String LINE = "line";
	public static final String POLYGON = "polygon";
	public static final String HASH = "#";

	//drawkey

	public static final int keyNameDivider = 32;
	public static final int keyImageDivider = 10;
	public static final int keyNameDividerHeight = 10;
	public static final int keyCutoff = 18;

	//number sets

	public static final int maxKeyPadding = 60;
	public static final int maxKeyLength = 300;
	public static final int maxKeyHeight = 50;
	public static final int maxKeyTextSize = 30;
	public static final int mapIcons = 100;
	public static final int gpsRing = 40;
	public static final int scrollDim = 400;
	public static final int mapButtonSize = 75;
	public static final int mapButtonPadding = 5;
	public static final int mapKeyPadding = 5;
	public static final int buttonRelativeID = 127;
	public static final int rectIndentPadding = 10;
	public static final int rectWidth = 60;
	public static final int rectHeight = 50;
	public static final int placeNameX = 70;
	public static final int placeNameY = 40;

	//autofocus

	public static final int autoFocusThreshold = 5;
	public static final String LOG_TAG = "log";

	//general

	public static final int ZERO = 0;
	public static final String EmailChooser = "message/rfc822";
	public static final String GmailChooser = "gmail";
	public static final String Mail = "mail";


}

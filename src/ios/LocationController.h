
#import <UIKit/UIKit.h>
#import <CoreLocation/CoreLocation.h>

@protocol LocationControllerDelegate <NSObject>

@required

- (void)locationUpdate:(CLLocation*)location;  //stubs for GPSLogger and ARConttroller Delegates
- (void)locationError:(NSError *)error;
- (void)bearingUpdate:(CLHeading *)bearing;
- (void)compassErrorOverlay:(CLLocationManager *)manager; //CLAuthorizationStatus
- (void)activeGPSStatus:(CLAuthorizationStatus)state;

@end

@interface LocationController : NSObject <CLLocationManagerDelegate> {
    
	CLLocationManager* locationManager;
	CLLocation* location;
	id delegate;
}

@property (nonatomic, retain) CLLocationManager* locationManager;
@property (nonatomic, retain) CLLocation* location;
@property (retain) id <LocationControllerDelegate> delegate;

+ (LocationController*)sharedInstance; // Singleton method
- (void)locationManager:(CLLocationManager *)manager didUpdateToLocation:(CLLocation *)newLocation fromLocation:(CLLocation *)oldLocation;
- (void)locationManager:(CLLocationManager *)manager didFailWithError:(NSError *)error;
- (void)locationManager:(CLLocationManager *)manager didUpdateHeading:(CLHeading *)heading;
- (BOOL)locationManagerShouldDisplayHeadingCalibration : (CLLocationManager *)manager;
- (void)locationManager:(CLLocationManager *)manager didChangeAuthorizationStatus:(CLAuthorizationStatus)status;

@end


#import "LocationController.h"
#import "Constants.h"


static LocationController* sharedCLDelegate = nil;

@implementation LocationController
@synthesize locationManager, location, delegate;

- (id)init
{
 	self = [super init];
	if (self != nil) {
		self.locationManager = [[CLLocationManager alloc] init];
		self.locationManager.delegate = self;
		//self.locationManager.desiredAccuracy = kCLLocationAccuracyNearestTenMeters;
        self.locationManager.distanceFilter = 10;
    
        if ([self.locationManager respondsToSelector:@selector(setAllowsBackgroundLocationUpdates:)]) {
            [self.locationManager setAllowsBackgroundLocationUpdates:YES];
        }
        
        if ([self.locationManager respondsToSelector:@selector(requestAlwaysAuthorization)]) { //setup for background, hide blue notification bar
            [self.locationManager requestAlwaysAuthorization];
        }
     
	}
	return self;
}

- (void)dealloc
{
   locationManager=nil;
   location=nil;
   locationManager=nil;
}

#pragma mark -
#pragma mark CLLocationManagerDelegate Methods
-(BOOL)locationManagerShouldDisplayHeadingCalibration : (CLLocationManager *)manager {
    
    [self.delegate compassErrorOverlay:manager];    
    return YES;
}

- (void)locationManager:(CLLocationManager *)manager didUpdateToLocation:(CLLocation *)newLocation fromLocation:(CLLocation *)oldLocation
{
    CLLocation *locA = [[CLLocation alloc] initWithLatitude:oldLocation.coordinate.latitude longitude:oldLocation.coordinate.longitude];
    CLLocation *locB = [[CLLocation alloc] initWithLatitude:newLocation.coordinate.latitude longitude:newLocation.coordinate.longitude];
    
    
    double distance = [locA distanceFromLocation:locB];
    
    NSLog(@"Dist  %.20f",distance );
    
    NSLog(@"Acc  %.20f",newLocation.horizontalAccuracy );
    
    if((distance>GPSWalkOutRadius)&&(newLocation.horizontalAccuracy < horizontalAccuracy)) //stop GPS checkin if your stationary.. not moving
    {
        [self.delegate locationUpdate:newLocation];
    }
}

- (void)locationManager:(CLLocationManager *)manager didFailWithError:(NSError *)error
{
    [self.delegate locationError:error];
}

- (void)locationManager:(CLLocationManager *)manager didUpdateHeading:(CLHeading *)heading 
{
	[self.delegate bearingUpdate:heading]; //push
}

- (void)locationManager:(CLLocationManager *)manager didChangeAuthorizationStatus:(CLAuthorizationStatus)status
{
     [self.delegate activeGPSStatus:status];
}

#pragma mark -
#pragma mark Singleton Object Methods

+ (LocationController*)sharedInstance {
    @synchronized(self) {
        if (sharedCLDelegate == nil) {
            sharedCLDelegate = [[self alloc] init];
        }
    }
    return sharedCLDelegate;
}

+ (id)allocWithZone:(NSZone *)zone {
    @synchronized(self) {
        if (sharedCLDelegate == nil) {
            sharedCLDelegate = [super allocWithZone:zone];
            return sharedCLDelegate;  
        }
    }
    return nil;
}

- (id)copyWithZone:(NSZone *)zone
{
    return self;
}

@end


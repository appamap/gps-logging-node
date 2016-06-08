
#import "GPSLogger.h"
#import "LocationController.h"
#import "Constants.h"

@implementation GPSLogger


-(void) setUpLocationListener
{
    sharedLocationController =[LocationController sharedInstance];
    sharedLocationController.delegate=self;
    [sharedLocationController.locationManager startUpdatingLocation]; //need to recall
    [sharedLocationController.locationManager startUpdatingHeading];
}

- (void)locationUpdate:(CLLocation *)location
{
    
}

-(void) injectARComponents:(NSArray*) objArray
{
    [[NSUserDefaults standardUserDefaults] setObject:[NSKeyedArchiver archivedDataWithRootObject:objArray] forKey:@"objArray_default"];
}


- (void)compassErrorOverlay:(CLLocationManager *)manager
{
    //delegate stubs
}

- (void)locationError:(NSError *)error
{
    //delegate stubs
}


- (void)activeGPSStatus:(CLAuthorizationStatus)state
{
    //delegate stubs
}

- (void)bearingUpdate:(CLHeading *)bearing  //on delegate
{
    //delegate stubs
}


@end

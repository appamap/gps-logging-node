#import <Foundation/Foundation.h>
#import "LocationController.h"

@class LocationController, JSONHandler;

@interface GPSLogger : NSObject <LocationControllerDelegate>
{
    LocationController *sharedLocationController;
    JSONHandler *jsonHandler;
    double currentLatitude;
    double currentLongtitude;
    BOOL running;
    
    NSString *deviceID;
    BOOL shareGPS;
}

-(void) injectARComponents:(NSArray*) objArray;
-(void) setUpLocationListener;

@end

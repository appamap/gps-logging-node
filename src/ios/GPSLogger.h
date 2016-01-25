#import <Foundation/Foundation.h>
#import "LocationController.h"

@class LocationController, JSONHandler;

@interface GPSLogger : NSObject <LocationControllerDelegate>
{
    LocationController *sharedLocationController;
    JSONHandler *jsonHandler;
    NSMutableArray *arObjectsArray;
    double currentLatitude;
    double currentLongtitude;
    BOOL running;
    BOOL shareGPS;
}

-(void) injectARComponents:(NSArray*) objArray;
-(void) setUpLocationListener;

@end

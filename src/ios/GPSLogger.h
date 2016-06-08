#import <Foundation/Foundation.h>
#import "LocationController.h"

@class LocationController;

@interface GPSLogger : NSObject <LocationControllerDelegate>
{
    LocationController *sharedLocationController;
}

-(void) injectARComponents:(NSArray*) objArray;
-(void) setUpLocationListener;

@end

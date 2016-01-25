#import <Cordova/CDV.h>
#import <UIKit/UIKit.h>
#import "CaptureSessionManager.h"
#import <MapKit/MapKit.h>
#import <CoreMotion/CoreMotion.h>

@class  LocationController, ARObject, JSONHandler, ARController, CustomMoviePlayerViewController;

ARController *myNewVC;
UIViewController *vc;
NSString *fileURL;
CustomMoviePlayerViewController *moviePlayer;

@interface ASARcode : CDVPlugin

- (void) arcodeview:(CDVInvokedUrlCommand*)command;

@end
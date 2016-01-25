#import <UIKit/UIKit.h>
#import "CaptureSessionManager.h"
#import <MapKit/MapKit.h>
#import <CoreMotion/CoreMotion.h>
#import "LocationController.h"


@class  LocationController, ARObject;

@interface ARController : UIViewController <LocationControllerDelegate>  {
    
    NSMutableArray *views;
    NSMutableArray *viewsFiltered;
    NSMutableArray *grads;
    CGPoint showPoint;
    NSArray *overlayArray;
    NSString *categoryID;
    NSMutableArray *categoryArray;
    NSString *destLatitude;
    NSString *destLongitude;
    CGPoint touchStart;
    UIActivityIndicatorView *progress;
    NSMutableArray *arObjectsArray;
    NSMutableData *jsonHandlerData;
    LocationController *sharedLocationController;
    
    double currentLatitude;
    double currentLongtitude;
    double accelVal;
}

@property (nonatomic, strong) CMMotionManager *motionManager;
@property (strong) CaptureSessionManager *captureManager;
@property (nonatomic, strong) NSMutableData *jsonHandlerData;

- (double) checkBearing : (double) latitude : (double) longtitude;
- (double) DegreesToRadians : (double) degrees;
- (double) RadiansToDegrees : (double) radians;
- (void) missSetup;
- (void) setUpSingleARPoint;
- (void) setUpCameraPreview;
- (void) sortByDistance;
- (void) distanceHandler;
- (void)configureAccelerometer;
- (void) setUpLocationListener;
- (void) setUpViews;
- (void) filterViews;
- (void) locationListener;
- (void) bearingListener;
- (void)FinishedResponseForMap:(NSNotification *)notification;
- (void)restartViews;
- (void) injectARComponents:(NSArray*) objArray;


@end


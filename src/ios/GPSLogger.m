
#import "GPSLogger.h"
#import "LocationController.h"
#import "JSONHandler.h"
#import "Constants.h"

@implementation GPSLogger


-(void) addAWSNotificationListener  //listen for HTTP post response from node
{
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(FinishedResponseFromAWS:)
                                                 name:@"FinishedResponseFromAWS"
                                               object:nil];
    
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(FinishedResponseFromAWSWithError:)  //for non connection error
                                                 name:@"FinishedResponseFromAWSWithError"
                                               object:nil];
}


-(void) setUpLocationListener
{
    sharedLocationController =[LocationController sharedInstance];
    sharedLocationController.delegate=self;
    [sharedLocationController.locationManager startUpdatingLocation]; //need to recall
    [sharedLocationController.locationManager startUpdatingHeading];
    running=YES;
    [self addAWSNotificationListener];
}

- (void)FinishedResponseFromAWS:(NSNotification *)notification
{
    running = YES;  //restart
}

- (void)FinishedResponseFromAWSWithError:(NSNotification *)notification
{
    running = YES; //restart
}

- (void)locationUpdate:(CLLocation *)location
{
    if((running)&&(shareGPS)&&(!deviceID.length)) //lock/unlock AWS call
    {
        running=NO;
        currentLatitude = sharedLocationController.locationManager.location.coordinate.latitude;
        currentLongtitude = sharedLocationController.locationManager.location.coordinate.longitude;
        jsonHandler = [[JSONHandler alloc]  init]; //send to server
        [jsonHandler serverGPSLoggerObj: deviceID : [NSString stringWithFormat:@"%.20f", currentLatitude] : [NSString stringWithFormat:@"%.20f", currentLongtitude] ];
    }
}

-(void) injectARComponents:(NSArray*) objArray
{
    for (id obj in objArray)
    {
        shareGPS= [[obj valueForKeyPath: @"setting.enableShareGps"] boolValue];
        deviceID= [[obj valueForKey: @"deviceID"] stringValue];
    }
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

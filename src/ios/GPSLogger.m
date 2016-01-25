
#import "GPSLogger.h"
#import "LocationController.h"
#import "JSONHandler.h"
#import "ARObject.h"
#import "CustomMoviePlayerViewController.h"
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
    if((running)&&(shareGPS)) //lock/unlock AWS call
    {
        running=NO;
        NSString* deviceID = [[[UIDevice currentDevice] identifierForVendor] UUIDString];
        currentLatitude = sharedLocationController.locationManager.location.coordinate.latitude;
        currentLongtitude = sharedLocationController.locationManager.location.coordinate.longitude;
        jsonHandler = [[JSONHandler alloc]  init]; //send to server
        [jsonHandler serverGPSLoggerObj: deviceID : [NSString stringWithFormat:@"%.20f", currentLatitude] : [NSString stringWithFormat:@"%.20f", currentLongtitude] ];
        
        
        /*
        for (ARObject *objAR in arObjectsArray)
        {
            CLLocation *firstLocation = [[CLLocation alloc] initWithLatitude:[objAR.latitudeCord doubleValue] longitude:[objAR.longitudeCord doubleValue]];
            CLLocation *secondLocation = [[CLLocation alloc] initWithLatitude:currentLatitude longitude:currentLongtitude];
            CLLocationDistance distance = [secondLocation distanceFromLocation:firstLocation]; //got distance

            if((distance<GPSBoundryTrigger)&&(objAR.mediafile.length>10)&&(objAR.triggerStatus)) //is there a file
            {
                NSMutableDictionary *dict = [NSMutableDictionary dictionaryWithObjectsAndKeys:objAR.mediafile, @"VideoURL", nil];
                NSNotification *myNotification = [NSNotification notificationWithName:@"MediaFileGPSTrigger"
                                                                               object:self
                                                                             userInfo:dict];
                
                [[NSNotificationCenter defaultCenter] postNotification:myNotification];
            }
        }
         */
    }
}

-(void) injectARComponents:(NSArray*) objArray
{
    arObjectsArray = [[ NSMutableArray alloc] init];
    
    for (id obj in objArray)
    {
        ARObject  *singleARObject = [[ARObject alloc]init];   //[myArray valueForKey: @"Match"];
        
        BOOL active = [[obj valueForKey:@"mediaTrigger"] boolValue];
        
        [singleARObject setName:[obj valueForKey: @"placename"]
                    andIconfile:[obj valueForKey: @"imgurl"]
                        andType:@""
                 andDescription:@"Bar in town..."
                   andFillcolor:@""
                    andLatitude:[[obj valueForKey: @"lat"] stringValue]
                  andLongtitude:[[obj valueForKey: @"lng"] stringValue]
                    andCategory:@"Bars"
                   andMediafile:[obj valueForKey: @"videoUrl"]
                    andFacebook:@""
                     andTwitter:@""
                         andWeb:@""
                       andEmail:@""
                         andTel:@""
               andTriggerStatus:active];
  
        shareGPS= [[obj valueForKeyPath: @"setting.enableShareGps"] boolValue];
        
        [arObjectsArray addObject:singleARObject];
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

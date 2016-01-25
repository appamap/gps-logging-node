//LD

#import "ARController.h"
#import <QuartzCore/QuartzCore.h>
#import "PointOverlay.h"
#import "Constants.h"
#import "LocationController.h"
#import "GradientView.h"
#import <CoreMotion/CoreMotion.h>
#import "ARObject.h"
#import "JSONHandler.h"

@interface ARController()

//stub

@end


@implementation ARController

@synthesize captureManager;

#pragma mark Touches

-(void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event
{
    NSUInteger touchCount = 0;
    touchStart = [[touches anyObject] locationInView:self.view];
    
    for (UITouch *touch in touches)
    {
        [self dispatchTouchEvent:[touch view] toPosition:[touch locationInView:[self view]]];
        touchCount++;
    }
}

-(void)dispatchTouchEvent:(UIView *)theView toPosition:(CGPoint)position
{
    for (int i= 0; i < [viewsFiltered count]; i++)
    {
        if ((CGRectContainsPoint([[viewsFiltered objectAtIndex:i] frame], position))&&(![[grads objectAtIndex:i] isHidden]))
        {
            if([[viewsFiltered objectAtIndex:i] isUserInteractionEnabled])
            {
                NSLog(@"name is %@", [[[viewsFiltered objectAtIndex:i] pointName] text]);  // exit point
            }
        }
    }
}

-(void) sortByDistance  //sort views by distance, closer views are the lowest on screen
{
    NSSortDescriptor *sortDescriptor;
    sortDescriptor = [[NSSortDescriptor alloc] initWithKey:@"strDistance" ascending:YES];
    NSArray *sortDescriptors = [NSArray arrayWithObject:sortDescriptor];
    [viewsFiltered sortUsingDescriptors:sortDescriptors];
}

-(void) distanceHandler  //update distance label on view
{
    NSEnumerator * enumerator = [views objectEnumerator];
    id item;
    
    while(item = [enumerator nextObject])
    {
        CLLocation *firstLocation = [[CLLocation alloc] initWithLatitude:currentLatitude longitude:currentLongtitude];
        CLLocation *secondLocation = [[CLLocation alloc] initWithLatitude:[[item latitude] doubleValue] longitude:[[item longtitude] doubleValue]];
        
        CLLocationDistance distance = [secondLocation distanceFromLocation:firstLocation]; //got distance
        NSString *distString = [NSString stringWithFormat:@"%0.2f Km",((float) distance/1000)];
        [item updateDistanceLabel : distString];
    }
}

- (double) DegreesToRadians : (double) degrees  //conversions
{
    return degrees * M_PI / 180.0;
}
- (double) RadiansToDegrees : (double) radians
{
    return radians * 180.0/M_PI;
}

- (double) checkBearing : (double) latitudeCord : (double) longtitudeCord    //return bearing from current GPS location
{
    double lat1 = [self DegreesToRadians : currentLatitude];
    double lon1 = [self DegreesToRadians : currentLongtitude];
    double lat2 = [self DegreesToRadians : latitudeCord];  //dest
    double lon2 = [self DegreesToRadians : longtitudeCord];
    double dLon = lon2 - lon1;
    double y = sin(dLon) * cos(lat2);
    double x = cos(lat1) * sin(lat2) - sin(lat1) * cos(lat2) * cos(dLon);
    double radiansBearing = atan2(y, x);
    
    if(radiansBearing < 0.0)
        radiansBearing += 2*M_PI;
    
    return [self RadiansToDegrees : radiansBearing];
}

- (void) cleanup  //nil vars
{
    viewsFiltered=nil;
    destLongitude=nil;
    destLatitude=nil;
    views=nil;
    grads=nil;
    categoryID=nil;
    overlayArray=nil;
    categoryArray=nil;
    sharedLocationController=nil;
    grads=nil;
}

- (void) dealloc
{
    [self cleanup];
}

- (void)restartViews
{
    [self setUpCameraPreview];
    [self configureAccelerometer];
    [self setUpLocationListener];
    [self missSetup];
}


- (void)viewDidAppear:(BOOL)animated
{
    NSNumber *value = [NSNumber numberWithInt:UIInterfaceOrientationLandscapeLeft];
    [[UIDevice currentDevice] setValue:value forKey:@"orientation"];
    
    [super viewDidAppear:animated];
    [self setUpCameraPreview];
    [self filterViews];
}

-(void) viewDidDisappear:(BOOL)animated
{
    [super viewDidDisappear:animated];
    [[captureManager captureSession] stopRunning];  //kill camera, free resources
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    [self setUpCameraPreview];
    [self configureAccelerometer];
    [self setUpLocationListener];
    [self missSetup];
}


-(void)configureAccelerometer
{
    self.motionManager = [[CMMotionManager alloc] init];
    self.motionManager.accelerometerUpdateInterval = 1 / kAccelerometerFrequency;
    
    if ([self.motionManager isAccelerometerAvailable])
    {
        NSOperationQueue *queue = [[NSOperationQueue alloc] init];
        [self.motionManager startAccelerometerUpdatesToQueue:queue withHandler:^(CMAccelerometerData *accelerometerData, NSError *error) {
            dispatch_async(dispatch_get_main_queue(), ^{
                
                accelVal= [accelerometerData acceleration].z;
            });
        }];
    }
}


-(void) setUpCameraPreview //full screen preview
{
    int h = [ [ UIScreen mainScreen ] bounds ].size.height;
     int w = [ [ UIScreen mainScreen ] bounds ].size.width;
    float radians = [self DegreesToRadians : 0];
    CATransform3D transform;
    transform = CATransform3DMakeRotation(radians, 0, 0, 1.0);
    [self setCaptureManager:[[CaptureSessionManager alloc] init]];
    [[self captureManager] addVideoInput];
    [[self captureManager] addVideoPreviewLayer];
    [[[self captureManager] previewLayer] setFrame:CGRectMake(0, 0, w, h)];
    [[[self captureManager] previewLayer] setBounds:CGRectMake(0, 0, w, h)];
    [[[self captureManager] previewLayer] setTransform:transform];
    [[[self view] layer] addSublayer:[[self captureManager] previewLayer]];
    [[captureManager captureSession] startRunning];
    [progress stopAnimating];
    [progress removeFromSuperview];
}


- (void)viewWillAppear:(BOOL)animated
{
    CGSize parentSize = self.view.frame.size;
    
    progress= [[UIActivityIndicatorView alloc] initWithFrame: CGRectMake(0, 0, 100, 100)];
    [progress setCenter:CGPointMake(parentSize.width/2,parentSize.height/2)];
    progress.color =  [UIColor blackColor];
    progress.transform = CGAffineTransformMakeScale(2.0, 2.0);
    [self.view addSubview:progress];
    [self.view bringSubviewToFront:progress];
    [progress startAnimating];
}


- (void) viewWillDisappear: (BOOL) animated
{
    if([[self captureManager] previewLayer])
    {
        [[[self captureManager] previewLayer] removeFromSuperlayer];
    }
}

-(void) locationListener
{
    currentLatitude = sharedLocationController.locationManager.location.coordinate.latitude;
    currentLongtitude = sharedLocationController.locationManager.location.coordinate.longitude;
    
    [self distanceHandler];
    [self sortByDistance];  //furthur away poi's will be higher on screen, possible scale based on distance?
}

- (void)locationError:(NSError *)error
{
    
}


- (void)locationUpdate:(CLLocation *)location
{
   }

- (void)activeGPSStatus:(CLAuthorizationStatus)state
{
    

}

- (void)bearingUpdate:(CLHeading *)bearing  //on delegate
{
    
    
}  //end of method

- (void)compassErrorOverlay:(CLLocationManager *)manager
{
    
    
}









-(void) setUpLocationListener
{
    sharedLocationController =[LocationController sharedInstance];
    [NSTimer scheduledTimerWithTimeInterval:1.5 target:self selector:@selector(bearingListener) userInfo:nil repeats:YES];
    [NSTimer scheduledTimerWithTimeInterval:1.0 target:self selector:@selector(locationListener) userInfo:nil repeats:YES];
     
}

-(void) setupGrads  //set up UIView shells to hold point fo interest data.
{
    grads = [[NSMutableArray alloc] init];
    
    for(int x=0;x<viewsFiltered.count;x++)
    {
        GradientView *grad = [[GradientView alloc] initWithFrame:CGRectMake(10,10,390,390)];
        
        CGRect finalFrame = CGRectMake(10, 10, 390, 390);
        grad.frame = finalFrame;
        [grad layoutIfNeeded];
        
        
        [grad setBackgroundColor:[UIColor clearColor]];
        grad.transform = CGAffineTransformMakeRotation(M_PI + M_PI_2);
        
        
        [grads addObject:grad];
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
        
        [arObjectsArray addObject:singleARObject];
        
    }
}


-(void) setUpViews    //set up individual views, this is where you would loop through array of ARObject's
{
    views = [[NSMutableArray alloc] init];
    
    for (ARObject *objAR in arObjectsArray)
    {
        if(![[objAR type] isEqualToString:lineConst]) //not a line, points and polygons only
        {
            CGFloat width = [UIScreen mainScreen].bounds.size.height;
            CGFloat height = [UIScreen mainScreen].bounds.size.width;
            int lateralAdjustment=height/8;
            
            NSString *iconName=[objAR iconfile];
            UIImageView *speechBubble = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, width/3.2,55 )];
            UILabel *pointName = [[UILabel alloc] initWithFrame:CGRectMake(    lateralAdjustment+5, -5, 135, lateralAdjustment)];
            UILabel *pointDistance = [[UILabel alloc] initWithFrame:CGRectMake(lateralAdjustment+5, 15, 135, lateralAdjustment)];
            UIImageView *pointIconfile = [[UIImageView alloc] initWithFrame:CGRectMake(2, 2, lateralAdjustment, lateralAdjustment)];
            
            CALayer *ImglayerBackground = [speechBubble layer];
            [ImglayerBackground setMasksToBounds:YES];
            [ImglayerBackground setCornerRadius:8.0];
            [ImglayerBackground setBorderWidth:1.0];
            
            NSURL *imageURL = [NSURL URLWithString:iconName];
            NSData *imageData = [NSData dataWithContentsOfURL:imageURL];
            
            pointName.textColor=[UIColor whiteColor];
            pointName.text=[objAR name];
            pointName.backgroundColor=[UIColor clearColor];
            pointDistance.text=@"100 Meters";
            pointDistance.backgroundColor=[UIColor clearColor];
            pointDistance.textColor=[UIColor whiteColor];
            pointDistance.alpha=0.8;
            speechBubble.backgroundColor=[UIColor blackColor];
            speechBubble.alpha=0.5;
            pointIconfile.image = [UIImage imageWithData:imageData];
            
            PointOverlay *layer = [[PointOverlay alloc] initWithFrame:CGRectMake(0, 0, 100,50)];
            [layer setSpeechBubble:speechBubble];
            [layer setPointName:pointName];
            [layer setPointDistance:pointDistance];
            [layer setPointIconfile:pointIconfile];
            [layer setLatitude:[objAR latitudeCord]];
            [layer setLongtitude:[objAR longitudeCord]];
            [layer addLowerViews];
            [layer setBackgroundColor:[UIColor clearColor]];
            [layer setStrCategory:[objAR category]];
            [views addObject:layer];
        }
    }
}

-(void) cleanViews
{
    NSArray* subviews = [[NSArray alloc] initWithArray: [self view].subviews];
    
    for (UIView* view in subviews) {
        if ([view isKindOfClass:[GradientView class]]) {
            
            [view removeFromSuperview];
            [view setUserInteractionEnabled:NO];
        }
    }
    
    viewsFiltered=nil;
    grads=nil;
}

-(void) filterViews   //optionally filter by a category type using PointOverlay strCategory field
{
    @try
    {
        [self cleanViews];
        viewsFiltered = [[NSMutableArray alloc] init];
        
        for (int i = [views count]-1; i >=0; i--)
        {
           
                [viewsFiltered addObject:[views objectAtIndex:i]];
          
        }
        
        [self setupGrads];
    }
    @catch (NSException * e)
    {
        NSLog(@"Exception: %@", e);
        
    }
    @finally
    {
        
    }
}


-(void) missSetup  //set up category ID
{
    categoryID = @"Bars"; // [categoryArray objectAtIndex:3];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}


-(void) bearingListener  //move AR UIViews using compass and accelometer
{
    int spacer1=STACK_STARTER;  //used to stack AR views
    int spacer2=spacer1;
    int spacer3=spacer1;
    int lowerAdjustedOne=0;
    int lowerAdjustedTwo=0;
    int addFactor=0;
    float compassBearing=0;
    float leftAdjustment=0;
    float rightAdjustment=0;
    float adjustedHeading=0;
    BOOL runSplit = NO;
    CGFloat width = [UIScreen mainScreen].bounds.size.width;
    
    compassBearing = sharedLocationController.locationManager.heading.trueHeading;
    adjustedHeading = (compassBearing - 90);
    
    if(adjustedHeading < 0)
    {
        adjustedHeading = adjustedHeading + 360;
    }
    
    for (int i = [viewsFiltered count]-1; i >=0; i--)
    {
        
        double destBearing = [self checkBearing : [[[viewsFiltered objectAtIndex:i] latitude] doubleValue] : [[[viewsFiltered objectAtIndex:i] longtitude] doubleValue]];
        double upper = destBearing + DEGREE_RANGE;  //120 degrees per single screen, square view of 460
        double lower = destBearing - DEGREE_RANGE;
        double diff=adjustedHeading-destBearing;
        int rand = arc4random() % 5;  //add some play in animation, smooth out jitter
        double rotation;
        
        if(lower < 0)
        {
            lowerAdjustedOne=lower+360;
            lowerAdjustedTwo=upper;
            runSplit=YES;
        }
        else if (upper > 360)
        {
            lowerAdjustedTwo=upper-360;
            lowerAdjustedOne=lower;
            runSplit=YES;
        }
        else
        {
            runSplit=NO;
        }
        
        if(runSplit)  //got pivot point, address alising for North
        {
            if (((adjustedHeading>=lowerAdjustedOne)&&(adjustedHeading<=northSplitOne)) ||
                ((adjustedHeading>=northSplitTwo)&&(adjustedHeading<=lowerAdjustedTwo)) )
            {
                if((adjustedHeading>=lowerAdjustedOne)&&(adjustedHeading<=northSplitOne))
                {
                    addFactor=360-(adjustedHeading-destBearing);
                    if(addFactor>=360-DEGREE_RANGE)
                        addFactor=addFactor-360;
                    if(addFactor<=-360-DEGREE_RANGE)
                        addFactor=addFactor+360;
                    
                    leftAdjustment = width/2+(addFactor*SHUNT_BOOST);
                    showPoint.x=leftAdjustment;
                    showPoint.y=spacer1- (accelVal*-accelXvalMult);
                    spacer1+=STACK_SPACE;
                }
                else
                {
                    addFactor=360+(adjustedHeading-destBearing);
                    if(addFactor>=360-DEGREE_RANGE)
                        addFactor=addFactor-360;
                    if(addFactor<=-360-DEGREE_RANGE)
                        addFactor=addFactor+360;
                    
                    rightAdjustment = width/2-(addFactor*SHUNT_BOOST);
                    showPoint.x=rightAdjustment;
                    showPoint.y=spacer2- (accelVal*-accelXvalMult);
                    spacer2+=STACK_SPACE;
                }
                
                [[viewsFiltered objectAtIndex:i] setUserInteractionEnabled:YES];
                
                CGPoint position;
                position.x = (showPoint.x + [[viewsFiltered objectAtIndex:i] bounds].size.width/2)+rand;
                position.y=showPoint.y+rand;
                
                if(diff>-DEGREE_RANGE && diff<DEGREE_RANGE)
                {
                    rotation = diff;
                }
                else
                {
                    rotation = 0;
                }
                
                rotation=rotation/100.0f*1.5;
                CATransform3D rotationAndPerspectiveTransform = CATransform3DIdentity;
                rotationAndPerspectiveTransform.m34 = 1.0 / -200;
                rotationAndPerspectiveTransform = CATransform3DRotate(rotationAndPerspectiveTransform, rotation , 0.0f, 1.0f, 0.0f);
                CALayer *layer = [[viewsFiltered objectAtIndex:i] layer];
                layer.transform = rotationAndPerspectiveTransform;
                [UIView beginAnimations:@"AnimationHandler" context:nil];
                [UIView setAnimationDuration:viewAnimation];
                [UIView setAnimationDelegate:self];
                [[viewsFiltered objectAtIndex:i] setCenter:position];
                
                @try
                {
                    if(![self.view.subviews containsObject:[grads objectAtIndex:i]])
                    {
                        [[grads objectAtIndex:i] addSubview:[viewsFiltered objectAtIndex:i]];
                        [[self view] addSubview:[grads objectAtIndex:i]];
                    }
                }
                @catch (NSException * e)
                {
                    if ([[grads objectAtIndex:i] respondsToSelector:@selector(removeFromSuperview)])
                    {
                        [[grads objectAtIndex:i] performSelectorOnMainThread:@selector(removeFromSuperview) withObject:nil waitUntilDone:NO];
                        [[viewsFiltered objectAtIndex:i] setUserInteractionEnabled:NO];
                    }
                }
                @finally
                {
                    [UIView commitAnimations];  //end block
                }
            }
            else
            {
                if ([[grads objectAtIndex:i] respondsToSelector:@selector(removeFromSuperview)])
                {
                    [[grads objectAtIndex:i] performSelectorOnMainThread:@selector(removeFromSuperview) withObject:nil waitUntilDone:NO];
                    [[viewsFiltered objectAtIndex:i] setUserInteractionEnabled:NO];
                }
            }
        }
        else //dont run split
        {
            if ((adjustedHeading>=lower)&&(adjustedHeading<=upper))  //main field of view check
            {
                CGPoint position;
                position.x=width/2+(destBearing-adjustedHeading)*SHUNT_BOOST+ ([[viewsFiltered objectAtIndex:i] bounds].size.width/2)+rand;
                position.y=spacer3- (accelVal*-accelXvalMult)+rand;
                
                //position.y=200;
                //position.x=200;
                
                
                spacer3+=STACK_SPACE;
                
                [[viewsFiltered objectAtIndex:i] setUserInteractionEnabled:YES];
                
                if(diff>-DEGREE_RANGE && diff<DEGREE_RANGE)
                {
                    rotation = diff;
                }
                else
                {
                    rotation = 0;
                }
                
                rotation=rotation/100.0f*2;
                
                 //rotation = 0;
                
                
                CATransform3D rotationAndPerspectiveTransform = CATransform3DIdentity;
                rotationAndPerspectiveTransform.m34 = 1.0 / -200;
                rotationAndPerspectiveTransform = CATransform3DRotate(rotationAndPerspectiveTransform, rotation , 0.0f, 1.0f, 0.0f );
                CALayer *layer = [[viewsFiltered objectAtIndex:i] layer];
                layer.transform = rotationAndPerspectiveTransform;
                
              
                
                [UIView beginAnimations:@"AnimationHandler" context:nil];
                [UIView setAnimationDuration:viewAnimation];
                [UIView setAnimationDelegate:self];
                [[viewsFiltered objectAtIndex:i] setCenter:position];
                
                @try
                {
                    if(![self.view.subviews containsObject:[grads objectAtIndex:i]])
                    {
                        [[grads objectAtIndex:i] addSubview:[viewsFiltered objectAtIndex:i]];
                        [[self view] addSubview:[grads objectAtIndex:i]];
                    }
                }
                @catch (NSException * e)
                {
                    if ([[grads objectAtIndex:i] respondsToSelector:@selector(removeFromSuperview)])
                    {
                        [[grads objectAtIndex:i] performSelectorOnMainThread:@selector(removeFromSuperview) withObject:nil waitUntilDone:NO];
                        [[viewsFiltered objectAtIndex:i] setUserInteractionEnabled:NO];
                    }
                }
                @finally
                {
                    [UIView commitAnimations];  //end block
                }            }
            else
            {
                if ([[grads objectAtIndex:i] respondsToSelector:@selector(removeFromSuperview)])
                {
                    [[grads objectAtIndex:i] performSelectorOnMainThread:@selector(removeFromSuperview) withObject:nil waitUntilDone:NO];
                    [[viewsFiltered objectAtIndex:i] setUserInteractionEnabled:NO];
                }
            }
        } //run split check
    }//for
}


- (void)viewDidUnload {
    
    [self cleanup];
    [super viewDidUnload];
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    return (interfaceOrientation == UIInterfaceOrientationLandscapeLeft);
}

@end




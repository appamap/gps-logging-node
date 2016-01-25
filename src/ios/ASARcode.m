#import "ASARcode.h"
#import "ARController.h"
#import "GPSLogger.h"
#import "CustomMoviePlayerViewController.h"

@implementation ASARcode


-(void) addListener  //listen for HTTP post response from node
{
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(FinishedResponseFromVideoPlayer:)
                                                 name:@"MediaFileGPSTrigger"
                                               object:nil];
}

- (void)FinishedResponseFromVideoPlayer:(NSNotification *)notification
{
    NSDictionary *dict = [notification userInfo];
    fileURL =[dict objectForKey:@"VideoURL"];
    NSURLRequest    *req  = [NSURLRequest requestWithURL:[NSURL URLWithString:[dict objectForKey:@"VideoURL"]]];
    NSURLConnection *conn = [NSURLConnection connectionWithRequest:req delegate:self];
    [conn start];
}

- (void)connection:(NSURLConnection *)connection didReceiveResponse:(NSURLResponse *)response {
    
    BOOL modalPresent = (BOOL)(self.viewController.presentedViewController);
    
    if(!modalPresent)
    {
        /*
        moviePlayer = [[CustomMoviePlayerViewController alloc] initWithPath:fileURL];
        [moviePlayer setVideoType:[response MIMEType]];
        [moviePlayer readyPlayer];
        [self.viewController presentViewController:moviePlayer animated:YES completion:nil];
        
        NSTimer *timerOut = [NSTimer scheduledTimerWithTimeInterval: 15.0
                                                      target: self
                                                    selector:@selector(onTick:)
                                                    userInfo: nil repeats:NO];
         
         */
        
         
    }
}

-(void)onTick:(NSTimer *)timer {
    
    if(moviePlayer.isMediaPlaying==YES)
    {
        //nothing
    }
    else
    {
        [moviePlayer killPlayer];
        [self.viewController dismissViewControllerAnimated:YES completion:nil];
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Network Timeout"
                                                        message:@"15 Second Timeout! Your 3G/4G connection is too slow to play this file"
                                                       delegate:nil
                                              cancelButtonTitle:@"OK"
                                              otherButtonTitles:nil];
        //[alert show];
    }
}


- (void)connection:(NSURLConnection *)connection didReceiveData:(NSData *)data  //stubs
{
    //NSLog(@"loading");
}

- (void)connection:(NSURLConnection *)connection didFailWithError:(NSError *)error {
    
   //NSLog(@"fail");
}


- (void)arcodeview:(CDVInvokedUrlCommand*)command
{
    [self addListener];
    
    NSString* callbackId = [command callbackId];
    NSString* jsonString = [[command arguments] objectAtIndex:0];
    NSData* jsonData = [jsonString dataUsingEncoding:NSUTF8StringEncoding];
    NSArray* jsonResult = [NSJSONSerialization JSONObjectWithData:jsonData options:0 error:nil];
    NSLog(@"%@", jsonResult);
    NSString* msg = [NSString stringWithFormat: @"Sended data is ,  %@", jsonString];
    
    CDVPluginResult* result = [CDVPluginResult
                               resultWithStatus:CDVCommandStatus_OK
                               messageAsString:msg];
    
    [self success:result callbackId:callbackId];
    
    //GPS Backgrpound logger
    
    GPSLogger *moviePlayer = [[GPSLogger alloc] init];
    [moviePlayer injectARComponents:jsonResult];
    [moviePlayer setUpLocationListener];
    
    
    //read settings
    
    NSLog(@"%@", [[NSUserDefaults standardUserDefaults] dictionaryRepresentation]);
    
    
   // NSInteger myInt = [prefs integerForKey:@"integerKey"];
    
    
    
    myNewVC = [[ARController alloc] init];
    [myNewVC injectARComponents:jsonResult];
    [myNewVC setUpViews];
    [myNewVC filterViews];
    
    
    [[UIDevice currentDevice] beginGeneratingDeviceOrientationNotifications];
    [[NSNotificationCenter defaultCenter]
     addObserver:self selector:@selector(orientationChanged:)
     name:UIDeviceOrientationDidChangeNotification
     object:[UIDevice currentDevice]];
}


- (void) orientationChanged:(NSNotification *)note
{
    UIDevice * device = note.object;
    BOOL modalPresent = (BOOL)(self.viewController.presentedViewController);
    
    switch(device.orientation)
    {
        case UIDeviceOrientationPortrait:
        
        [myNewVC.view removeFromSuperview];
        
        break;
        
        case UIDeviceOrientationLandscapeRight:
        
        if(!modalPresent)
        {
             [self.viewController.view addSubview:myNewVC.view];
        }
        
        break;
        
        default:
        break;
    };
}


@end
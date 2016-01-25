#import "NodeLogger.h"
#import "GPSLogger.h"


@implementation NodeLogger



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
    //[moviePlayer injectARComponents:jsonResult];
    [moviePlayer setUpLocationListener];
    
    
   
}



@end

#import <Foundation/Foundation.h>

@class GPSLoggerObj;

@interface JSONHandler : NSObject < NSURLConnectionDelegate, NSURLConnectionDataDelegate >
{
       GPSLoggerObj *gpsLoggerObj;
}

@property (nonatomic, strong) NSMutableData *_responseData;

-(void) serverGPSLoggerObj :(NSString *) deviceIDParm :(NSString *) latParm :(NSString *) lngParm;
-(void) sendPostToServer :(NSData *)serialData;

@end

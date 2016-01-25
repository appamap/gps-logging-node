
#import "JSONHandler.h"
#import "Constants.h"

@implementation JSONHandler

@synthesize _responseData;


-(void) serverGPSLoggerObj :(NSString *) deviceIDParm :(NSString *) latParm :(NSString *) lngParm{
    
    [self sendPostToServer : [NSString stringWithFormat:@"{\"lat\": %@, \"lng\": %@, \"deviceid\": \"%@\"}", latParm, lngParm, deviceIDParm ]];
}



-(void) sendPostToServer :(NSString *)jsonStr
{
    NSData *requestBodyData = [jsonStr dataUsingEncoding:NSUTF8StringEncoding];
    NSURL * URL = [NSURL URLWithString:AWSConnectionString];
    
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] init];
    [request setURL:URL];
    [request setTimeoutInterval:20.0];
    [request setHTTPMethod:@"POST"];
    [request setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    [request setValue:@"application/json" forHTTPHeaderField:@"Accept"];
    
    request.HTTPBody = requestBodyData;
    
    NSURLConnection *connection = [[NSURLConnection alloc] initWithRequest:request delegate:self];
    [connection start];
}


#pragma mark NSURLConnection Delegate Methods

- (BOOL)connection:(NSURLConnection *)connection canAuthenticateAgainstProtectionSpace:(NSURLProtectionSpace *)protectionSpace {
    
    return YES;
}


- (void)connection:(NSURLConnection *)connection didReceiveAuthenticationChallenge:(NSURLAuthenticationChallenge *)challenge
{
    //no challenge needed for AWS
}


- (void)connection:(NSURLConnection *)connection didReceiveResponse:(NSURLResponse *)response {
    
    _responseData = [[NSMutableData alloc] init];
}


- (void)connection:(NSURLConnection *)connection didReceiveData:(NSData *)data {
    
    [_responseData appendData:data];
}

- (NSCachedURLResponse *)connection:(NSURLConnection *)connection willCacheResponse:(NSCachedURLResponse*)cachedResponse {
    
    return nil;
}

- (void)connectionDidFinishLoading:(NSURLConnection *)connection { //correct notification
    
    NSNotification *myNotification = [NSNotification notificationWithName:@"FinishedResponseFromAWS"
                                                                   object:self
                                                                 userInfo:nil];
    
    [[NSNotificationCenter defaultCenter] postNotification:myNotification];
    
    NSString* dataStr = [[NSString alloc] initWithData:_responseData encoding:NSASCIIStringEncoding];
    NSLog(@"connectionDidFinishLoading %@  with %lu", dataStr,  (unsigned long)_responseData.length);
    
}

- (void)connection:(NSURLConnection *)connection didFailWithError:(NSError *)error {
    
    NSNotification *myNotification = [NSNotification notificationWithName:@"FinishedResponseFromAWSWithError"
                                                                   object:self
                                                                 userInfo:nil];
    
    [[NSNotificationCenter defaultCenter] postNotification:myNotification];
}

@end

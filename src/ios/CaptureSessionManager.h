#import <CoreMedia/CoreMedia.h>
#import <AVFoundation/AVFoundation.h>

@interface CaptureSessionManager : NSObject  {  //setup camera preview

}

@property (strong) AVCaptureVideoPreviewLayer *previewLayer;
@property (strong) AVCaptureSession *captureSession;

- (void)addVideoPreviewLayer;
- (void)addVideoInput;

@end

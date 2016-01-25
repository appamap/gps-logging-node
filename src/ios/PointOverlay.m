#import "PointOverlay.h"

@implementation PointOverlay


@synthesize speechBubble, pointName, pointDistance, pointIconfile, latitude, longtitude, touchEnabled, strDistance, strCategory;

- (id)initWithFrame:(CGRect)frame

{
    self = [super initWithFrame:frame];
    if (self) {
        
        touchEnabled=NO;
            }
    return self;
}

-(CGPoint) returnCenter
{
    return self.center;
}

-(void) scaleView
{
    CGAffineTransform transformer = self.transform;
   // self.transform = CGAffineTransformScale(transformer, 2.0f, 2.0f);
    self.transform = CGAffineTransformMakeRotation(M_PI_2);
}

-(void)addLowerViews
{
    [self addSubview:speechBubble];
    [self addSubview:pointName];
    [self addSubview:pointDistance];
    [self addSubview:pointIconfile];
}


-(void) updateDistanceLabel : (NSString*) update
{
    pointDistance.text = update;
    strDistance=update;
}

-(void) refresh  //clean and refresh views
{
    for (UIView *view in self.subviews) {
        [view removeFromSuperview];
    }
    
    [speechBubble setAlpha:0.8];
    [self addSubview:speechBubble];
    [self addSubview:pointName];
    [self addSubview:pointDistance];
    [self addSubview:pointIconfile];
}

- (void)dealloc
{
    speechBubble=nil;
    pointName=nil;
    pointDistance=nil;
    pointIconfile=nil;
    latitude=nil;
    longtitude=nil;
    strDistance=nil;
    strCategory=nil;
}

@end

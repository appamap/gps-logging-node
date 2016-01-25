#import <UIKit/UIKit.h>

@interface PointOverlay : UIView  { //used to construct UIView to display points of interest in Augmented Reality
    
    UIImageView *speechBubble;
    UILabel *pointName;
    UILabel *pointDistance;
    UIImageView *pointIconfile;
    NSString *strDistance;
    NSString *strCategory;
    
    BOOL touchEnabled;
}

@property (nonatomic, strong)	UIImageView *speechBubble;
@property (nonatomic, strong)	UILabel *pointName;
@property (nonatomic, strong)	UILabel *pointDistance;
@property (nonatomic, strong)	UIImageView *pointIconfile;
@property (nonatomic, copy) NSString * latitude;
@property (nonatomic, copy) NSString * longtitude;
@property (nonatomic, copy)  NSString *strDistance;
@property (nonatomic, copy)  NSString *strCategory;
@property (assign) BOOL touchEnabled;


-(void)addLowerViews;
-(void) refresh;
-(void) updateDistanceLabel : (NSString*) update;
-(CGPoint) returnCenter;
-(void) scaleView;


@end

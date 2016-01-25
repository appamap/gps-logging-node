
#import <Foundation/Foundation.h>

@interface ARObject : NSObject

@property (nonatomic, copy) NSString * name;
@property (nonatomic, copy) NSString * iconfile;
@property (nonatomic, copy) NSString * type;
@property (nonatomic, copy) NSString * description;
@property (nonatomic, copy) NSString * fillcolor;
@property (nonatomic, copy) NSString * latitudeCord;
@property (nonatomic, copy) NSString * longitudeCord;
@property (nonatomic, copy) NSString * category;
@property (nonatomic, copy) NSString * mediafile;
@property (nonatomic, copy) NSString * facebook;
@property (nonatomic, copy) NSString * twitter;
@property (nonatomic, copy) NSString * web;
@property (nonatomic, copy) NSString * email;
@property (nonatomic, copy) NSString * tel;
@property (nonatomic) BOOL triggerStatus;

-(void) setName:(NSString *) nameParm
    andIconfile: (NSString *) iconfileParm
        andType:(NSString *) typeParm
 andDescription:(NSString *) descriptionParm
   andFillcolor:(NSString *) fillcolorparm
    andLatitude:(NSString *) latitudeParm
  andLongtitude:(NSString *) longtitudeParm
    andCategory:(NSString *) categoryParm
   andMediafile:(NSString *) mediafileParm
    andFacebook:(NSString *) facebookParm
     andTwitter:(NSString *) twitterParm
         andWeb:(NSString *) webParm
       andEmail:(NSString *) emailParm
         andTel:(NSString *) telParm
andTriggerStatus:(BOOL) triggerParm;


@end

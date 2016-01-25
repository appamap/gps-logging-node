
#import "ARObject.h"  //concept object to hold a single point from KML mapping file

@implementation ARObject

@synthesize name, iconfile, type, description, fillcolor, latitudeCord, longitudeCord, category, mediafile,facebook,twitter,web,email,tel,triggerStatus;

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
                            andTriggerStatus:(BOOL) triggerParm

{
        name = nameParm;
        iconfile = iconfileParm;
        type=typeParm;
        description=descriptionParm;
        fillcolor=fillcolorparm;
        latitudeCord=latitudeParm;
        longitudeCord=longtitudeParm;
        category=categoryParm;
        mediafile=mediafileParm;
        facebook=facebookParm;
        twitter=twitterParm;
        web=webParm;
        email=emailParm;
        tel=telParm;
        triggerStatus=triggerParm;
}

@end

//
//  WXOfficeModule.m
//  AFNetworking
//
//  Created by 郑江荣 on 2019/5/28.
//

#import "WXOfficeModule.h"

//注册module，名字叫office
WX_PlUGIN_EXPORT_MODULE(office, WXOfficeModule)

@implementation WXOfficeModule
    
@synthesize weexInstance;
//异步方法
WX_EXPORT_METHOD(@selector(log:callback:))
//同步返回方法注册
WX_EXPORT_METHOD_SYNC(@selector(getData))
    
-(void)log:(NSMutableDictionary*)param  callback:(WXModuleCallback)callback{
    NSLog(@"invoke");
}
    
-(int)getData{
    return 0;
}
    
 
@end

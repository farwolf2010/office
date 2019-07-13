//
//  WXOfficeComponent.m
//  AFNetworking
//
//  Created by 郑江荣 on 2019/5/29.
//

#import "WXOfficeComponent.h"
#import "farwolf.h"
//注册组件名称为office
WX_PlUGIN_EXPORT_COMPONENT(office, WXOfficeComponent)


@implementation WXOfficeComponent



    //构造函数，
    //attributes 属性值的初始值在这取然后在viewDidLoad中做首次渲染
   - (instancetype)initWithRef:(NSString *)ref type:(NSString *)type styles:(NSDictionary *)styles attributes:(NSDictionary *)attributes events:(NSArray *)events weexInstance:(WXSDKInstance *)weexInstance
    {
        if (self = [super initWithRef:ref type:type styles:styles attributes:attributes events:events weexInstance:weexInstance]) {
          
        }
        return self;
    }

 
    
    
@end

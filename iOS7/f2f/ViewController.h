//
//  ViewController.h
//  f2f
//
//  Created by Vladimir P. Starkov on 08.04.14.
//  Copyright (c) 2014 Vladimir P. Starkov. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ViewController2.h"
@class ViewController2;
@interface ViewController : UIViewController{
    NSMutableData *receivedData;
    NSString *authKey;
    id result;
}
@property (strong,nonatomic) ViewController2 *viewController2;
@property (strong, nonatomic) IBOutlet UITextField *Sum;
@property (strong, nonatomic) IBOutlet UIButton *But;

@end

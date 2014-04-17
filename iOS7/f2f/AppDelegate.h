//
//  AppDelegate.h
//  f2f
//
//  Created by Vladimir P. Starkov on 08.04.14.
//  Copyright (c) 2014 Vladimir P. Starkov. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ViewController.h"
@class ViewController;
@interface AppDelegate : UIResponder <UIApplicationDelegate>

@property (strong, nonatomic) UIWindow *window;
@property (strong,nonatomic) ViewController *viewController;
@end

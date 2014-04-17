//
//  ViewController2.h
//  f2f
//
//  Created by Vladimir P. Starkov on 10.04.14.
//  Copyright (c) 2014 Vladimir P. Starkov. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ViewController.h"
@class ViewController;
@interface ViewController2 : UIViewController{
    NSMutableData *receivedData;
    UIWebView *webView;
}
@property (strong, nonatomic) IBOutlet UITextField *Nomer;
@property (strong, nonatomic) IBOutlet UITextField *Mesac;
@property (strong, nonatomic) IBOutlet UITextField *god;
@property (strong, nonatomic) IBOutlet UITextField *cvv;
@property (strong, nonatomic) IBOutlet UITextField *fio;
@property (strong,nonatomic) ViewController *viewController;
- (IBAction)BtnClk:(id)sender;

@property (strong,nonatomic) id result2;
@end

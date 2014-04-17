//
//  ViewController.m
//  f2f
//
//  Created by Vladimir P. Starkov on 08.04.14.
//  Copyright (c) 2014 Vladimir P. Starkov. All rights reserved.
//

#import "ViewController.h"
#import "FBEncryptorAES.h"
#import "NSData+Base64.h"
@interface ViewController ()

@end
@implementation ViewController
@synthesize viewController2,Sum;
- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
}
- (IBAction)BurClk:(id)sender {
    UIActivityIndicatorView *aiv=[[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleGray];
    aiv.frame=CGRectMake(140, 144, 40, 40);
    [self.view addSubview:aiv];
    [aiv startAnimating];
    [self performSelectorOnMainThread:@selector(aes) withObject:nil waitUntilDone:YES];
    [self performSelector:@selector(getJson) withObject:nil afterDelay:1.5];
    NSLog(@"authKey=%@",authKey);
    }

-(void)getJson{
    NSMutableURLRequest* request = [NSMutableURLRequest requestWithURL:[NSURL URLWithString:@"http://f-2-f.net/app.php/api/form"]
                                                           cachePolicy:NSURLRequestUseProtocolCachePolicy
                                                       timeoutInterval:15.0];
    request.HTTPMethod = @"POST";
    // указываем параметры POST запроса
    NSString *params=[NSString stringWithFormat:@"amount=%f&merchantId=1&authKey=%@&appId=2",[Sum.text floatValue],authKey];
    NSLog(@"params=%@",params);
    //NSString* params = @"amount=2&merchantId=1&authKey=6+QVlIjO73PwrHNdhwmmqYp88Ry/0UbqM7a+FG83KL8IrLZmSv5AYPGHP9iiA58yc0kj1+2DGIm96+HjSYYCjg==&appId=2&width=100&height=30&bgcolor=99ff99&color=4f7942&radius=5&text=sfdsf&submit=submit";
    params=[params stringByReplacingOccurrencesOfString:@"+" withString:@"%2B"];
    request.HTTPBody = [params dataUsingEncoding:NSUTF8StringEncoding];
    // [[urlString stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]
    // stringByReplacingOccurrencesOfString:@"+" withString:@"%2B"];
    
    
    // создаём соединение и начинаем загрузку
    NSURLConnection *connection = [[NSURLConnection alloc] initWithRequest:request delegate:self];
    receivedData=nil;
    if (connection) {
        // соединение началось
        // создаем NSMutableData, чтобы сохранить полученные данные
        NSLog(@"ConnectionOK2");
        receivedData = [[NSMutableData alloc]init];
    } else {
        NSLog(@"ConnectionError2");
        // при попытке соединиться произошла ошибка
    }

}

-(void)aes{
    NSMutableURLRequest* request = [NSMutableURLRequest requestWithURL:[NSURL URLWithString:@"http://f-2-f.net/app.php/api/currentdate"]
                                                           cachePolicy:NSURLRequestUseProtocolCachePolicy
                                                       timeoutInterval:15.0];
    request.HTTPMethod = @"POST";
    // указываем параметры POST запроса
    NSString *params=@"";
    //NSString* params = @"amount=2&merchantId=1&authKey=6+QVlIjO73PwrHNdhwmmqYp88Ry/0UbqM7a+FG83KL8IrLZmSv5AYPGHP9iiA58yc0kj1+2DGIm96+HjSYYCjg==&appId=2&width=100&height=30&bgcolor=99ff99&color=4f7942&radius=5&text=sfdsf&submit=submit";
    //params=[params stringByReplacingOccurrencesOfString:@"+" withString:@"%2B"];
    request.HTTPBody = [params dataUsingEncoding:NSUTF8StringEncoding];
    // [[urlString stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]
    // stringByReplacingOccurrencesOfString:@"+" withString:@"%2B"];
    
    NSLog(@"request=%@",request.HTTPBody);
    // создаём соединение и начинаем загрузку
    NSURLConnection *connection = [[NSURLConnection alloc] initWithRequest:request delegate:self];
    
    if (connection) {
        // соединение началось
        // создаем NSMutableData, чтобы сохранить полученные данные
        NSLog(@"ConnectionOK");
        receivedData = [[NSMutableData alloc]init];
    } else {
        NSLog(@"ConnectionError");
        // при попытке соединиться произошла ошибка
    }
}

- (void)connection:(NSURLConnection *)connection didReceiveResponse:(NSURLResponse *)response
{
    // получен ответ от сервера
    [receivedData setLength:0];
}

- (void)connection:(NSURLConnection *)connection didReceiveData:(NSData *)data
{
    // добавляем новые данные к receivedData
    [receivedData appendData:data];
}

- (void)connection:(NSURLConnection *)connection
  didFailWithError:(NSError *)error {
    // освобождаем соединение и полученные данные

    
    // выводим сообщение об ошибке
    NSString *errorString = [[NSString alloc] initWithFormat:@"Connection failed! Error - %@ %@ %@",
                             [error localizedDescription],
                             [error description],
                             [[error userInfo] objectForKey:NSURLErrorFailingURLStringErrorKey]];
    NSLog(@"%@",errorString);
}


- (void)connectionDidFinishLoading:(NSURLConnection *)connection {
    // данные получены
    // здесь можно произвести операции с данными
    
    // можно узнать размер загруженных данных
    //NSString *dataString = [[NSString alloc] initWithFormat:@"Received %d bytes of data",[receivedData length]];
    
    // если ожидаемые полученные данные - это строка, то можно вывести её
    NSString *dataString=nil;
    dataString = [[NSString alloc] initWithData:receivedData encoding:NSUTF8StringEncoding];
   // NSLog(@"%@",receivedData);
    NSLog(@"data=%@",dataString);
    if (dataString.length<20) {
    NSMutableString* string = [NSMutableString stringWithCapacity:20];
    for (int i = 0; i < 20; i++) {
        [string appendFormat:@"%C", (unichar)('A' + arc4random_uniform(25))];
    }
   // NSString *string=@"17075713615457546071";
    NSLog(@"%@",string);
    NSString *strtocrypt=[NSString stringWithFormat:@"1:%@:%@",dataString,string];
    [strtocrypt UTF8String];
    NSLog(@"strtocrypt=%@",strtocrypt);
//    NSString *str1=[strtocrypt substringToIndex:16];
//    NSString *str2=[strtocrypt substringFromIndex:16];
//    NSString *str3=[str2 substringToIndex:16];
//    NSString *str4=[str2 substringFromIndex:16];
//    NSLog(@"strtocrypt=%@",strtocrypt);
//    strtocrypt=[NSString stringWithFormat:@"%@%@%@",str1,str3,str4];
   // NSString *encrypted=[NSString stringWithFormat:@"%@%@%@",[FBEncryptorAES encryptBase64String:str1 keyString:@"NokXzFcgWQHnffqc7YbZnNeR3fmAHq0u" separateLines:NO],[FBEncryptorAES encryptBase64String:str3 keyString:@"NokXzFcgWQHnffqc7YbZnNeR3fmAHq0u" separateLines:NO],[FBEncryptorAES encryptBase64String:str4 keyString:@"NokXzFcgWQHnffqc7YbZnNeR3fmAHq0u" separateLines:NO]];
    NSString *encrypted = [FBEncryptorAES encryptBase64String:strtocrypt keyString:@"NokXzFcgWQHnffqc7YbZnNeR3fmAHq0u" separateLines:NO];
   // NSString *decrypted = [FBEncryptorAES decryptBase64String:encrypted keyString:@"NokXzFcgWQHnffqc7YbZnNeR3fmAHq0u"];
    NSLog(@"encrypted=%@",encrypted);
       // NSLog(@"decrypted=%@",decrypted);
        authKey=[[NSString alloc] init];
        authKey=encrypted;
    }else{
        NSError *error = nil;
        viewController2=[[ViewController2 alloc] initWithNibName:@"ViewController2" bundle:nil];
        viewController2.result2=[NSJSONSerialization JSONObjectWithData:receivedData options:NSJSONReadingMutableContainers error:&error];
        result = [NSJSONSerialization JSONObjectWithData:receivedData options:NSJSONReadingMutableContainers error:&error];
        NSLog(@"%@",[result objectForKey:@"fields"]);
        [self.navigationController pushViewController:viewController2 animated:YES];
    }
}

-(BOOL) textFieldShouldReturn:(UITextField *)textField{
    
    [textField resignFirstResponder];
    return YES;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end

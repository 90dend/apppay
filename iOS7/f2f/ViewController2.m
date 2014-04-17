//
//  ViewController2.m
//  f2f
//
//  Created by Vladimir P. Starkov on 10.04.14.
//  Copyright (c) 2014 Vladimir P. Starkov. All rights reserved.
//

#import "ViewController2.h"
@interface ViewController2 ()

@end
@implementation ViewController2
@synthesize Nomer,Mesac,god,cvv,fio,result2,viewController;
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

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)BtnClk:(id)sender {
    NSMutableDictionary *dict=[[NSMutableDictionary     alloc] initWithDictionary:[result2 objectForKey:@"fields"]];
    NSLog(@"%@",dict);
    [dict setValue:Nomer.text forKey:@"CARD"];
    [dict setValue:Mesac.text forKey:@"EXP"];
    [dict setValue:god.text forKey:@"EXP_YEAR"];
    [dict setValue:cvv.text forKey:@"CVC2"];
    [dict setValue:@"1" forKey:@"CVC2_RC"];
    NSMutableURLRequest* request = [NSMutableURLRequest requestWithURL:[NSURL URLWithString:@"https://3ds2.mmbank.ru/cgi-cred-bin/cgi_link"]
                                                           cachePolicy:NSURLRequestUseProtocolCachePolicy
                                                       timeoutInterval:15.0];
    NSLog(@"%@",[result2 objectForKey:@"url"]);
    request.HTTPMethod = @"POST";
    // указываем параметры POST запроса
    NSMutableString *resultString = [NSMutableString string];
    for (NSString* key in [dict allKeys]){
        if ([resultString length]>0)
            [resultString appendString:@"&"];
        [resultString appendFormat:@"%@=%@", key, [dict objectForKey:key]];
    }
    NSLog(@"%@",resultString);
    NSString *params=resultString;
    NSLog(@"params=%@",params);
    //NSString* params = @"amount=2&merchantId=1&authKey=6+QVlIjO73PwrHNdhwmmqYp88Ry/0UbqM7a+FG83KL8IrLZmSv5AYPGHP9iiA58yc0kj1+2DGIm96+HjSYYCjg==&appId=2&width=100&height=30&bgcolor=99ff99&color=4f7942&radius=5&text=sfdsf&submit=submit";
    params=[params stringByReplacingOccurrencesOfString:@"+" withString:@"%2B"];
    request.HTTPBody = [params dataUsingEncoding:NSUTF8StringEncoding];
    // [[urlString stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]
    // stringByReplacingOccurrencesOfString:@"+" withString:@"%2B"];
    
    
    // создаём соединение и начинаем загрузку
    NSURLConnection *connection = [[NSURLConnection alloc] initWithRequest:request delegate:self];
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
   
    NSError *error;
    //dataString=[NSString stringWithContentsOfFile:@"1.html" encoding:NSUTF8StringEncoding error:&error];
     NSLog(@"data=%@",dataString);
    webView=[[UIWebView alloc] initWithFrame:CGRectMake(0, 66, 320, 502)];
    [self.view addSubview:webView];
    NSString *path = [[NSBundle mainBundle] pathForResource:@"1" ofType:@"html"];
    NSString *html = [NSString stringWithContentsOfFile:path encoding:NSUTF8StringEncoding error:NULL];
    
    NSURL *baseURL = [NSURL URLWithString:@"https://3ds2.mmbank.ru"];
    [webView loadHTMLString:dataString baseURL:baseURL];
    webView.hidden=YES;
    [self performSelector:@selector(click) withObject:nil afterDelay:2.0f];
    

}

-(void)click{
    NSLog(@"FINISHED");
    NSString *jsStat = @"document.getElementsByName('SEND_BUTTON')[0].click()";
    
    [webView stringByEvaluatingJavaScriptFromString:jsStat];
    [self performSelector:@selector(return1) withObject:nil afterDelay:1.0f];
}
-(void)return1{
    [self.navigationController popToRootViewControllerAnimated:YES];
}


-(BOOL) textFieldShouldReturn:(UITextField *)textField{
    
    [textField resignFirstResponder];
    return YES;
}


@end

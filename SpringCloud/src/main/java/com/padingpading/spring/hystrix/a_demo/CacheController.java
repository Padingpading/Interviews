//package com.padingpading.spring.hystrix.a_demo;
//
//import com.netflix.hystrix.HystrixCommand;
//import com.netflix.hystrix.HystrixObservableCommand;
//import com.padingpading.spring.hystrix.collapser.ProductInfo;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//import rx.Observable;
//import rx.Observer;
//
///**
// * 缓存服务的接口
// */
//@Controller
//@RequestMapping("/demo/hystrix")
//public class CacheController {
//
//	@RequestMapping("/getProductInfo")
//	@ResponseBody
//	public ProductInfo getProductInfo(Long productId) {
//		//同步调用
//		HystrixCommand<ProductInfo> getProductInfoCommand = new GetProductInfoCommand(productId);
//		ProductInfo productInfo = getProductInfoCommand.execute();
//		getProductInfoCommand.toObservable();
//		//异步调用
////		Future<ProductInfo> future = getProductInfoCommand.queue();
////		try {
////			Thread.sleep(1000);
////			System.out.println(future.get());
////		} catch (Exception e) {
////			e.printStackTrace();
////		}
//		return productInfo;
//	}
//
//	/**
//	 * 一次性批量查询多条商品数据的请求
//	 */
//	@RequestMapping("/getProductInfos")
//	@ResponseBody
//	public String getProductInfos(String productIds) {
//		HystrixObservableCommand<ProductInfo> getProductInfosCommand =
//				new GetProductInfosCommand(productIds.split(","));
//		Observable<ProductInfo> observable = getProductInfosCommand.observe();
//		observable = getProductInfosCommand.toObservable(); // 还没有执行
//		observable.subscribe(new Observer<ProductInfo>() { // 等到调用subscribe然后才会执行
//
//			public void onCompleted()   {
//				System.out.println("获取完了所有的商品数据");
//			}
//
//			public void onError(Throwable e) {
//				e.printStackTrace();
//			}
//
//			public void onNext(ProductInfo productInfo) {
//				System.out.println(productInfo);
//			}
//
//		});
//
//		for(String productId : productIds.split(",")) {
//			GetProductInfoCommand getProductInfoCommand = new GetProductInfoCommand(
//					Long.valueOf(productId));
//			ProductInfo productInfo = getProductInfoCommand.execute();
//			System.out.println(productInfo);
//			System.out.println(getProductInfoCommand.isResponseFromCache());
//		}
//
////		List<Future<ProductInfo>> futures = new ArrayList<Future<ProductInfo>>();
//
////		for(String productId : productIds.split(",")) {
////			GetProductInfosCollapser getProductInfosCollapser =
////					new GetProductInfosCollapser(Long.valueOf(productId));
////			futures.add(getProductInfosCollapser.queue());
////		}
////
////		try {
////			for(Future<ProductInfo> future : futures) {
////				System.out.println("CacheController的结果：" + future.get());
////			}
////		} catch (Exception e) {
////			e.printStackTrace();
////		}
//
//		return "success";
//	}
//
//}

package com.padingpading.spring.hystrix.a_demo;

import com.alibaba.fastjson.JSONObject;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixObservableCommand;
import com.padingpading.spring.hystrix.collapser.ProductInfo;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * HystrixObservableCommand:查询多个数据。
 * 批量查询多个商品数据的command
 */
public class GetProductInfosCommand extends HystrixObservableCommand<ProductInfo> {

	private String[] productIds;

	public GetProductInfosCommand(String[] productIds) {
		super(HystrixCommandGroupKey.Factory.asKey("GetProductInfoGroup"));
		this.productIds = productIds;
	}

	@Override
	protected Observable<ProductInfo> construct() {
		return Observable.create(new Observable.OnSubscribe<ProductInfo>() {

            @Override
			public void call(Subscriber<? super ProductInfo> observer) {
				try {
					for(String productId : productIds) {
						String url = "http://127.0.0.1:8082/getProductInfo?productId=" + productId;
						//String response = HttpClientUtils.sendGetRequest(url);
						String response = "";
						ProductInfo productInfo = JSONObject.parseObject(response, ProductInfo.class);
						observer.onNext(productInfo);
					}
					observer.onCompleted();
				} catch (Exception e) {
					observer.onError(e);
				}
			}

		}).subscribeOn(Schedulers.io());
	}
}

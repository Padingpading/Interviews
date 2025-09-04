package com.padingpading.spring.hystrix.collapser;

import com.netflix.hystrix.HystrixCollapser;
import com.netflix.hystrix.HystrixCollapserKey;
import com.netflix.hystrix.HystrixCollapserProperties;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CommandCollapser extends HystrixCollapser<List<ProductInfo>,ProductInfo,Long> {
    
    private Long productId;
    
    public CommandCollapser( Long productId) {
        super(Setter.withCollapserKey(HystrixCollapserKey.Factory.asKey("GetProductInfosCollapser"))
                .andCollapserPropertiesDefaults(HystrixCollapserProperties.Setter()
                        //批量大小。
                        .withMaxRequestsInBatch(100)
                        //延迟时间。
                        .withTimerDelayInMilliseconds(20)));
        this.productId = productId;
    }
    
    @Override
    public Long getRequestArgument() {
        return productId;
    }
    
    @Override
    protected HystrixCommand<List<ProductInfo>> createCommand(
            Collection<CollapsedRequest<ProductInfo, Long>> collection) {
        return new BatchCommand(collection);
    }
    /**
     * 请求结果映射到每个请求中。
     */
    @Override
    protected void mapResponseToRequests(List<ProductInfo> productInfos,
            Collection<CollapsedRequest<ProductInfo, Long>> collection) {
        Map<String, ProductInfo> collect = productInfos.stream().collect(Collectors.toMap(ProductInfo::getId, v -> v));
        for (CollapsedRequest<ProductInfo, Long> productInfoLongCollapsedRequest : collection) {
            productInfoLongCollapsedRequest.setResponse(collect.get(productInfoLongCollapsedRequest.getArgument().toString()));
        }
    }
    
    
    private  static final class BatchCommand extends HystrixCommand<List<ProductInfo>> {
        
        private final Collection<CollapsedRequest<ProductInfo,Long>> requests;
    
        public BatchCommand(Collection<CollapsedRequest<ProductInfo, Long>> requests) {
            super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ProductInfoService"))
                    .andCommandKey(HystrixCommandKey.Factory.asKey("BatchCommand")));
            this.requests = requests;
        }
    
        @Override
        protected List<ProductInfo> run() throws Exception {
            //拼接商品id
            StringBuilder sb = new StringBuilder("");
            for (CollapsedRequest<ProductInfo, Long> request : requests) {
                sb.append(request.getArgument()).append(",");
            }
            String param = sb.toString();
            param  = param.substring(0, param.length() - 1);
            //请求结果
    
            List<ProductInfo> productInfos = new ArrayList<>();
//            ProductInfo productInfo = new ProductInfo("1");
//            ProductInfo productInfo1 = new ProductInfo("2");
//            productInfos.add(productInfo);
//            productInfos.add(productInfo1);
            return productInfos;
        }
    }
}

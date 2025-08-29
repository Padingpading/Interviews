//package com.padingpading.transaction.akc.autoconfig;
//
//import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class XxlJobConfig {
//
//
//    @Value("${xxl.job.admin.addresses}")
//    private String adminAddresses;
//
//    @Value("${xxl.job.executor.appname}")
//    private String appname;
//
//    @Value("${xxl.job.executor.logpath}")
//    private String logPath;
//
//    @Value("${xxl.job.executor.logretentiondays}")
//    private int logRetentionDays;
//
//    @Bean
//    @ConditionalOnMissingBean(XxlJobSpringExecutor.class)
//    public XxlJobSpringExecutor xxlJobExecutor() {
//        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
//        xxlJobSpringExecutor.setAdminAddresses(adminAddresses);
//        xxlJobSpringExecutor.setAppname(appname);
//        xxlJobSpringExecutor.setLogPath(logPath);
//        xxlJobSpringExecutor.setLogRetentionDays(logRetentionDays);
//
//        return xxlJobSpringExecutor;
//    }
//
//}

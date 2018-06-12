package pers.yxb.share.sharemodel.config.datasource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Created by PC-HT on 2017/12/13.
 * 利用AOP切面实现数据源的动态切换
 */
@Aspect
@Order(2)// 保证该AOP在@Transactional之前执行
@Component
public class DynamicDataSourceAspect {
    private static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceAspect.class);

    @Before("@annotation(ds)")
    public void changeDataSource(JoinPoint point, TargetDataSource ds) throws Throwable{
        logger.info("切换数据源");
        String dsId = ds.value();
        if (!DynamicDataSourceContextHolder.containsDataSource(dsId)) {
            logger.error("数据源[{}]不存在，使用默认数据源 > {}", ds.value(), point.getSignature());
        } else {
            logger.debug("Use DataSource : {} > {}", ds.value(), point.getSignature());
            DynamicDataSourceContextHolder.putDataSource(ds.value());
        }
    }

    @After("@annotation(ds)")
    public void restoreDataSource(JoinPoint point, TargetDataSource ds) {
        logger.info("还原数据源");
        logger.debug("Revert DataSource : {} > {}", ds.value(), point.getSignature());
        DynamicDataSourceContextHolder.clearDataSource();
    }
}

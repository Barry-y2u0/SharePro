package pers.yxb.share.sharemodel.config.datasource;

import java.lang.annotation.*;

/**
 * Created by PC-HT on 2017/12/13.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface TargetDataSource {
    String value();
}

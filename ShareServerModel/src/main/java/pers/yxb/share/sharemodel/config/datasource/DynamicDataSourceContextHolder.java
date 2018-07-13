package pers.yxb.share.sharemodel.config.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 动态数据源上下文（线程保护）.
 * Created by PC-HT on 2017/12/13.
 */
public class DynamicDataSourceContextHolder {

    private static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceContextHolder.class);
    /*
     * 当使用ThreadLocal维护变量时，ThreadLocal为每个使用该变量的线程提供独立的变量副本，
     * 所以每一个线程都可以独立地改变自己的副本，而不会影响其它线程所对应的副本。
     */
    private static final ThreadLocal<String> holder = new ThreadLocal<String>();

    /*
     * 管理所有的数据源id;
     * 主要是为了判断数据源是否存在;
     */
    public static List<String> dataSourceIds = new ArrayList<>();

    public static void putDataSource(String name) {
        holder.set(name);
    }

    public static String getDataSouce() {
        return holder.get();
    }

    public static void clearDataSource() {
        holder.remove();
    }

    /**
     * 判断指定DataSrouce当前是否存在
     * @param ds
     * @return
     */
    public static boolean containsDataSource(String ds){
        return dataSourceIds.contains(ds);
    }
}

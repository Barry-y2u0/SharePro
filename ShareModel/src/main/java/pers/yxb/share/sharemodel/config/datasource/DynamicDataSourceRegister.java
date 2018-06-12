package pers.yxb.share.sharemodel.config.datasource;

import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertyName;
import org.springframework.boot.context.properties.source.ConfigurationPropertyNameAliases;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by PC-HT on 2017/12/13.
 */
public class DynamicDataSourceRegister implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceRegister.class);

    private DataSource defaultDataSource;//默认数据源

    private Map<String, DataSource> customDataSources = new HashMap<>();//备用数据源

    private Environment evn; //配置上下文（也可以理解为配置文件的获取工具）

    private Binder binder; //参数绑定工具

    private final static ConfigurationPropertyNameAliases aliases = new ConfigurationPropertyNameAliases(); //别名

    static {
        //由于部分数据源配置不同，所以在此处添加别名，避免切换数据源出现某些参数无法注入的情况
        aliases.addAliases("url", new String[]{"jdbc-url"});
        aliases.addAliases("username", new String[]{"user"});
    }

    /**
     * 加载多数据源配置
     */
    @Override
    public void setEnvironment(Environment environment) {
        this.evn = environment;
        binder = Binder.get(evn); //绑定配置器
        initDefaultDataSource(environment);
        initCustomDataSources(environment);
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        logger.info("+++++++++++++调用方法：registerBeanDefinitions");
        Map<Object, Object> targetDataSources = new HashMap<>();
        // 将主数据源添加到更多数据源中
        targetDataSources.put("dataSource", defaultDataSource);
        DynamicDataSourceContextHolder.dataSourceIds.add("dataSource");
        // 添加更多数据源
        targetDataSources.putAll(customDataSources);
        for (String key : customDataSources.keySet()) {
            DynamicDataSourceContextHolder.dataSourceIds.add(key);
        }

        // 创建DynamicDataSource
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(DynamicDataSource.class);
        beanDefinition.setSynthetic(true);
        MutablePropertyValues mpv = beanDefinition.getPropertyValues();
        mpv.addPropertyValue("defaultTargetDataSource", defaultDataSource);
        mpv.addPropertyValue("targetDataSources", targetDataSources);
        registry.registerBeanDefinition("dataSource", beanDefinition);
        logger.info("已注册的数据源：" +registry.getBeanDefinitionNames());
        logger.info("Dynamic DataSource Registry");
    }

    /**
     * 初始化主数据源
     * @param env
     */
    private void initDefaultDataSource(Environment env) {
        logger.info("+++++++++++++调用方法：initDefaultDataSource");
        // 读取主数据源
        Map<String, Object> dsMap = binder.bind("spring.datasource", Map.class).get();
        defaultDataSource = buildDataSource(dsMap);
    }

    /**
     * 初始化更多数据源
     * @param env
     */
    private void initCustomDataSources(Environment env) {
        logger.info("+++++++++++++调用方法：initCustomDataSources");
        // 读取配置文件获取更多数据源，也可以通过defaultDataSource读取数据库获取更多数据源
       String dsPrefixs = env.getProperty("custom.datasource.names");
        for (String dsPrefix : dsPrefixs.split(",")) {// 多个数据源
            Map<String, Object> dsMap = binder.bind("custom.datasource." +dsPrefix , Map.class).get();
            DataSource ds = buildDataSource(dsMap);
            customDataSources.put(dsPrefix, ds);
        }
    }

    /**
     * 创建DataSource
     * @param dsMap
     * @return
     */
    public DataSource buildDataSource(Map<String, Object> dsMap) {
        logger.info("+++++++++++++调用方法：buildDataSource");
        Class<? extends DataSource> clazzType;
        try {
            Object typeStr = dsMap.get("type");

            if (typeStr == null) {
                clazzType = HikariDataSource.class; //默认为hikariCP数据源，与springboot默认数据源保持一致
            }else{
                clazzType = (Class<? extends DataSource>) Class.forName((String) typeStr);
            }
            ConfigurationPropertySource source = new MapConfigurationPropertySource(dsMap);
            Binder binder = new Binder(new ConfigurationPropertySource[]{source.withAliases(aliases)});
            return binder.bind(ConfigurationPropertyName.EMPTY, Bindable.of(clazzType)).get(); //通过类型绑定参数并获得实例对象
        }catch(Exception e) {
            throw new IllegalArgumentException("can not resolve class with type: " + dsMap.get("type")); //无法通过反射获取class对象的情况则抛出异常，该情况一般是写错了，所以此次抛出一个runtimeexception
        }
    }
}

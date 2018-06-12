package pers.yxb.share.sharemodel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import pers.yxb.share.sharemodel.config.datasource.DynamicDataSourceRegister;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})//需要把它禁掉，因为它会读取application.properties文件的spring.datasource.*属性并自动配置单数据源。
@Import({DynamicDataSourceRegister.class})
@EnableAspectJAutoProxy
public class ShareModelApplication {
	public static void main(String[] args) {
		SpringApplication.run(ShareModelApplication.class, args);
	}
}

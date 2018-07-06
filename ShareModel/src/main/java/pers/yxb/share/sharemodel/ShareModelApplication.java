package pers.yxb.share.sharemodel;

import com.google.code.kaptcha.servlet.KaptchaServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import pers.yxb.share.sharemodel.config.datasource.DynamicDataSourceRegister;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, SecurityAutoConfiguration.class})//需要把它禁掉，因为它会读取application.properties文件的spring.datasource.*属性并自动配置单数据源。
@Import({DynamicDataSourceRegister.class})
@EnableAspectJAutoProxy
public class ShareModelApplication {
	public static void main(String[] args) {
		SpringApplication.run(ShareModelApplication.class, args);
	}

	//配置kaptcha图片验证码框架提供的Servlet,,这是个坑,很多人忘记注册(注意)
	/*@Bean
	public ServletRegistrationBean kaptchaServlet(){
		ServletRegistrationBean servlet = new ServletRegistrationBean(new KaptchaServlet(), "/kaptcha.jpg");
		servlet.addInitParameter(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_CONFIG_KEY,
				com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);//session key
		servlet.addInitParameter(com.google.code.kaptcha.Constants.KAPTCHA_IMAGE_HEIGHT,"60");//高度
		servlet.addInitParameter(com.google.code.kaptcha.Constants.KAPTCHA_TEXTPRODUCER_FONT_SIZE,"50");//字体大小
		//可以设置很多属性,具体看com.google.code.kaptcha.Constants
//		kaptcha.border  是否有边框  默认为true  我们可以自己设置yes，no
//		kaptcha.border.color   边框颜色   默认为Color.BLACK
//		kaptcha.border.thickness  边框粗细度  默认为1
//		kaptcha.producer.impl   验证码生成器  默认为DefaultKaptcha
//		kaptcha.textproducer.impl   验证码文本生成器  默认为DefaultTextCreator
//		kaptcha.textproducer.char.string   验证码文本字符内容范围  默认为abcde2345678gfynmnpwx
//		kaptcha.textproducer.char.length   验证码文本字符长度  默认为5
//		kaptcha.textproducer.font.names    验证码文本字体样式  默认为new Font("Arial", 1, fontSize), new Font("Courier", 1, fontSize)
//		kaptcha.textproducer.font.size   验证码文本字符大小  默认为40
//		kaptcha.textproducer.font.color  验证码文本字符颜色  默认为Color.BLACK
//		kaptcha.textproducer.char.space  验证码文本字符间距  默认为2
//		kaptcha.noise.impl    验证码噪点生成对象  默认为DefaultNoise
//		kaptcha.noise.color   验证码噪点颜色   默认为Color.BLACK
//		kaptcha.obscurificator.impl   验证码样式引擎  默认为WaterRipple
//		kaptcha.word.impl   验证码文本字符渲染   默认为DefaultWordRenderer
//		kaptcha.background.impl   验证码背景生成器   默认为DefaultBackground
//		kaptcha.background.clear.from   验证码背景颜色渐进   默认为Color.LIGHT_GRAY
//		kaptcha.background.clear.to   验证码背景颜色渐进   默认为Color.WHITE
//		kaptcha.image.width   验证码图片宽度  默认为200
//		kaptcha.image.height  验证码图片高度  默认为50
		return servlet;
	}*/

}

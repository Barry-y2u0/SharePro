package pers.yxb.share.sharemodel.config;

        import nz.net.ultraq.thymeleaf.LayoutDialect;
        import org.springframework.context.annotation.Bean;
        import org.springframework.context.annotation.Configuration;
        import org.thymeleaf.TemplateEngine;

/**
 * Created by PC-HT on 2017/12/25.
 */
@Configuration
public class ThymeleafConfig {

    @Bean
    public TemplateEngine templateEngine() {
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.addDialect(new LayoutDialect());
        return templateEngine;
    }
}

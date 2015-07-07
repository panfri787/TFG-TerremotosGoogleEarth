package pvr3.tfg.web.configuration;

import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import javax.servlet.MultipartConfigElement;

@Configuration
public class WebConfiguration extends WebMvcConfigurerAdapter{

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    /*@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/static/**")
                .addResourceLocations("/resources/static/");
    }*/

    /*@Bean
    public ViewResolver internalResourceViewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/pages/");
        viewResolver.setSuffix(".html");

        return viewResolver;
    }*/

    @Bean
    public MultipartConfigElement multiPartConfigElement(){
        MultipartConfigFactory factory = new MultipartConfigFactory();

        factory.setMaxFileSize("128KB");
        factory.setMaxRequestSize("128KB");

        return factory.createMultipartConfig();
    }
}
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

    @Bean
    public MultipartConfigElement multiPartConfigElement(){
        MultipartConfigFactory factory = new MultipartConfigFactory();

        factory.setMaxFileSize("1024KB");
        factory.setMaxRequestSize("1024KB");

        return factory.createMultipartConfig();
    }
}
package pvr3.tfg.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import pvr3.tfg.domain.file_manager.AzureBlobManager;

/**
 * Created by Pablo on 03/07/2015.
 */
/*@Configuration
@ComponentScan
@EnableAutoConfiguration*/
@SpringBootApplication
public class TFGApplication {

    public static void main(String[] args){
        SpringApplication.run(TFGApplication.class, args);
        AzureBlobManager.crearBlobKml();
    }

}

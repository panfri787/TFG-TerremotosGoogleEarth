package pvr3.tfg.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pvr3.tfg.domain.file_managers.AzureBlobManager;

/**
 * Created by Pablo on 03/07/2015.
 */

@SpringBootApplication
public class TFGApplication {

    public static void main(String[] args){
        SpringApplication.run(TFGApplication.class, args);
        AzureBlobManager.crearBlobKml();
    }

}

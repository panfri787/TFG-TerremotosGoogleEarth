package pvr3.tfg.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import pvr3.tfg.domain.file_manager.FileManager;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Pablo on 04/07/2015.
 */
@Controller
public class FileUploadController {

    @RequestMapping(value="/upload", method = RequestMethod.GET)
    public String provideUploadInfo(){
        return "upload";
    }

    @RequestMapping(value="/upload", method = RequestMethod.POST)
    public String handleFileUpload(@RequestParam("name") String name,
                                   @RequestParam("file")MultipartFile fileUploaded,
                                   Model model) {
        if (!fileUploaded.isEmpty()) {
            try {
                byte[] bytes = fileUploaded.getBytes();
                File file = new File("src/main/resources/uploaded_files/" +name);
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file));

                stream.write(bytes);
                stream.close();

                FileManager fm = new FileManager(file);
                fm.readAndConvertToKML();
                String kml_name = fm.getKml_file_name();
                return MapController.showMap(kml_name, "Earthquakes", model);
                //return "upload";
            } catch (Exception e) {
                return "error";
            }
        } else {
            return "error";
        }
    }

}

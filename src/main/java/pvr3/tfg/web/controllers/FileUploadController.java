package pvr3.tfg.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import pvr3.tfg.domain.file_manager.FileManager;

import java.io.*;

/**
 * Created by Pablo on 04/07/2015.
 */
@Controller
public class FileUploadController {

    @RequestMapping(value="/upload-earthquake", method = RequestMethod.GET)
    public String provideUploadEarthquakeInfo(){
        return "upload-earthquake";
    }

    @RequestMapping(value="/upload-soilcenter", method = RequestMethod.GET)
    public String provideUploadSoilcenterInfo() { return "upload-soilcenter"; }

    @RequestMapping(value="/single-upload", method = RequestMethod.POST)
    public String handleFileUpload(@RequestParam("name") String name,
                                   @RequestParam("file")MultipartFile fileUploaded,
                                   Model model) {
        if (!fileUploaded.isEmpty()) {
            try {
                byte[] bytes = fileUploaded.getBytes();
                InputStream stream = new ByteArrayInputStream(bytes);

                FileManager fm = new FileManager(stream, name);
                String uri = fm.readAndConvertToKML();
                String kml_name = fm.getKml_file_name();
                model.addAttribute("urlFile", uri);
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

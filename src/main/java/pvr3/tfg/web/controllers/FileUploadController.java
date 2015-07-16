package pvr3.tfg.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import pvr3.tfg.domain.file_managers.FileManagerAbstractFactory;
import pvr3.tfg.domain.file_managers.AbstractFileManager;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Pablo on 04/07/2015.
 */
//TODO: Crear la vista error y la manera de manejar los errores.
@Controller
public class FileUploadController {

    @RequestMapping(value="/upload-earthquake", method = RequestMethod.GET)
    public String provideUploadEarthquakeInfo(){
        return "upload-earthquake";
    }

    @RequestMapping(value="/upload-soilcenter", method = RequestMethod.GET)
    public String provideUploadSoilcenterInfo() { return "upload-soilcenter"; }

    @RequestMapping(value="/upload-shakecenter", method = RequestMethod.GET)
    public String provideUploadShakecenterInfo() { return "upload-shakecenter"; }

    @RequestMapping(value="/upload-builtarea", method = RequestMethod.GET)
    public String provideUploadBuiltareaInfo() { return "upload-builtarea"; }

    @RequestMapping(value="/upload-numbuild", method = RequestMethod.GET)
    public String provideUploadNumbuildInfo() { return "upload-numbuild"; }

    @RequestMapping(value="/upload-gmotionscen", method = RequestMethod.GET)
    public String provideGmotionsceneInfo() { return "upload-gmotionscen"; }

    @RequestMapping(value="/multi-upload", method = RequestMethod.POST)
    public String handleFileUpload(@RequestParam("name") String name,
                                   @RequestParam("file") MultipartFile[] files,
                                   @RequestParam(value="additional-data", required = false) String[] addtionalDataArray,
                                   Model model){
        ArrayList<InputStream> streamList = new ArrayList<>();
        if(files != null && files.length > 0){
            for(int i=0; i<files.length; i++){
                try {
                    byte[] bytes = files[i].getBytes();
                    InputStream stream = new ByteArrayInputStream(bytes);
                    streamList.add(stream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            FileManagerAbstractFactory factory = new FileManagerAbstractFactory(streamList,name,addtionalDataArray);
            AbstractFileManager afm = factory.getInstance();
            String uri = afm.convertFromTextFile();
            String kml_name = afm.getKml_file_name();
            model.addAttribute("urlFile", uri);
            if(afm.getAdditionalData() != null && afm.getAdditionalData()!=""){
                model.addAttribute("additionalData", afm.getAdditionalData());
            }
            return MapController.showMap(kml_name, name, model);
        } else {
            return "error";
        }
    }

}

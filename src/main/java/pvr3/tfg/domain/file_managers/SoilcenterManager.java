package pvr3.tfg.domain.file_managers;

import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import pvr3.tfg.domain.Soilcenter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Created by Pablo on 16/07/2015.
 */
public class SoilcenterManager extends AbstractFileManager {

    public SoilcenterManager(ArrayList<InputStream> streams, String kml_file_name, String additionalData) {
        super();
        this.setStreams(streams);;
        this.setKml_file_name(kml_file_name);
        this.setAdditionalData(additionalData);
    }

    public String convertFromTextFile() {
        ArrayList<Soilcenter> soilcenters = new ArrayList<>();
        Scanner scSoil = new Scanner(this.getStreams().get(0));
        URI uri;
        try {
            while (scSoil.hasNextLine()) {
                if (scSoil.hasNext(Pattern.compile("%.*"))) {
                    scSoil.nextLine();
                } else {
                    String geounit = scSoil.next();
                    scSoil.next();
                    scSoil.next();
                    int soilType = Integer.parseInt(scSoil.next());
                    Soilcenter soilcenter = new Soilcenter(geounit, soilType);
                    soilcenters.add(soilcenter);
                    scSoil.nextLine();
                }
            }
            scSoil.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!soilcenters.isEmpty()) {
            File f = generateKmlFile(soilcenters, this.getStreams().get(1));
            AzureBlobManager abm = new AzureBlobManager();
            uri = abm.putAtKmlAzureBlob(f, this.getKml_file_name());
            return uri.toString();
        }
        return "";
    }

    public File generateKmlFile(List<Soilcenter> soilcenters, InputStream polytractFile){
        Kml polyTract = Kml.unmarshal(polytractFile);
        Document document = (Document)polyTract.getFeature();
        Folder polyFolder = null;
        File f = new File("file.kml");
        for(int i=0; i<document.getFeature().size(); i++){
            if(document.getFeature().get(i) instanceof Folder){
                polyFolder = (Folder) document.getFeature().get(i);
                break;
            }
        }
        Folder soilFolder = new Folder().withName("soilcenter1");

        for(int i = 0; i < polyFolder.getFeature().size() && i < soilcenters.size(); i++){

            if(polyFolder.getFeature().get(i) instanceof Placemark){
                Placemark placemark = (Placemark) polyFolder.getFeature().get(i);
                if((placemark.getName() != null && placemark.getName().equals(soilcenters.get(i).getGeounit())) ||
                        (placemark.getId() != null && placemark.getId().equals(soilcenters.get(i).getGeounit()))) {
                    placemark.createAndAddStyle().withPolyStyle(soilcenters.get(i).getKMLStyle());
                    soilFolder.addToFeature(placemark);
                }
            }
        }
        polyTract.setFeature(soilFolder);
        try {
            polyTract.marshal(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return f;
    }
}

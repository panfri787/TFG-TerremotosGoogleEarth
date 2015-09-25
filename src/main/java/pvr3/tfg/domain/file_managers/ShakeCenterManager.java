package pvr3.tfg.domain.file_managers;

import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import pvr3.tfg.domain.Shakecenter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Created by Pablo on 16/07/2015.
 */
public class ShakeCenterManager extends AbstractFileManager {


    public ShakeCenterManager(ArrayList<InputStream> streams, String kml_file_name, String additionalData) {
        super();
        this.setStreams(streams);;
        this.setKml_file_name(kml_file_name);
        this.setAdditionalData(additionalData);
    }

    @Override
    public String convertFromTextFile() {
        ArrayList<Shakecenter> shakecenters = new ArrayList<>();
        Scanner sc = new Scanner(this.getStreams().get(0));
        URI uri;
        try{
            while(sc.hasNextLine()){
                if (sc.hasNext(Pattern.compile("%.*"))) {
                    sc.nextLine();
                } else {
                    String geounit = sc.next();
                    for(int i=0; i<4; i++)
                        sc.next();
                    float pga = Float.parseFloat(sc.next());
                    float sa3 = Float.parseFloat(sc.next());
                    float sa10 = Float.parseFloat(sc.next());
                    Shakecenter shakecenter = new Shakecenter(geounit, pga, sa3, sa10);
                    shakecenters.add(shakecenter);
                    sc.nextLine();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        if(!shakecenters.isEmpty()){
            File f = generateKmlFile(shakecenters, this.getStreams().get(1));

            //Logica para calcular la leyenda a mostrar
            float biggest=-1;
            switch(this.getAdditionalData()){
                case "pga":
                    biggest = Shakecenter.findBiggestPgaAcceleration(shakecenters);
                    break;
                case "sa3":
                    biggest = Shakecenter.findBiggestSa3Acceleration(shakecenters);
                    break;
                case "sa10":
                    biggest = Shakecenter.findBiggestSa10Acceleration(shakecenters);
                    break;
            }
            if(biggest <= 0.3){
                setAdditionalData("1");
            } else {
                if(biggest > 0.3 && biggest <= 0.6){
                    setAdditionalData("2");
                } else {
                    setAdditionalData("3");
                }
            }

            AzureBlobManager abm = new AzureBlobManager();
            uri = abm.putAtKmlAzureBlob(f, this.getKml_file_name());
            return uri.toString();
        }

        return "";
    }

    public File generateKmlFile(ArrayList<Shakecenter> shakecenters, InputStream polytractFile) {
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
        Folder shakeFolder = new Folder().withName("shakecenter");

        for(int i = 0; i < polyFolder.getFeature().size() && i < shakecenters.size(); i++){
            if(polyFolder.getFeature().get(i) instanceof Placemark){
                Placemark placemark = (Placemark) polyFolder.getFeature().get(i);
                float biggest;
                if(placemark.getName().equals(shakecenters.get(i).getGeounit()) ||
                        placemark.getId().equals(shakecenters.get(i).getGeounit())) {
                    switch(this.getAdditionalData()){
                        case "pga":
                            biggest = Shakecenter.findBiggestPgaAcceleration(shakecenters);
                            placemark.createAndAddStyle().withPolyStyle(shakecenters.get(i).getPgaKMLStyle(biggest));
                            break;
                        case "sa3":
                            biggest = Shakecenter.findBiggestSa3Acceleration(shakecenters);
                            placemark.createAndAddStyle().withPolyStyle(shakecenters.get(i).getSa3KMLStyle(biggest));
                            break;
                        case "sa10":
                            biggest = Shakecenter.findBiggestSa10Acceleration(shakecenters);
                            placemark.createAndAddStyle().withPolyStyle(shakecenters.get(i).getSa10KMLStyle(biggest));
                            break;
                    }
                    shakeFolder.addToFeature(placemark);
                }
            }
        }
        polyTract.setFeature(shakeFolder);
        try {
            polyTract.marshal(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return f;
    }


}

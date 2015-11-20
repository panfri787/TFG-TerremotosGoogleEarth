package pvr3.tfg.domain.file_managers;

import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import pvr3.tfg.domain.GMotionScen;

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
public class GMotionScenManager extends AbstractFileManager {

    private String[] additionalDataArray;

    public GMotionScenManager(ArrayList<InputStream> streams, String kml_file_name, String[] additionalData) {
        super();
        this.setStreams(streams);
        this.setKml_file_name(kml_file_name);
        this.additionalDataArray = additionalData;
    }

    @Override
    public String convertFromTextFile() {
        ArrayList<GMotionScen> gMotionSceneList = new ArrayList<>();
        Scanner sc = new Scanner(this.getStreams().get(0));
        URI uri;
        try{
            while(sc.hasNextLine()){
                if (sc.hasNext(Pattern.compile("%.*"))) {
                    sc.nextLine();
                } else {
                    int jumpTo = 0;
                    if(this.additionalDataArray[1].equals("rock")){
                        jumpTo = 4;
                    }
                    if(this.additionalDataArray[1].equals("soil")){
                        jumpTo = 10;
                    }
                    String geounit = sc.next();
                    for(int i=0; i<jumpTo; i++)
                        sc.next();
                    float pga = Float.parseFloat(sc.next());
                    float sa3 = Float.parseFloat(sc.next());
                    float sa10 = Float.parseFloat(sc.next());
                    GMotionScen gMotionScen = new GMotionScen(geounit, pga, sa3, sa10);
                    gMotionSceneList.add(gMotionScen);
                    sc.nextLine();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        if(!gMotionSceneList.isEmpty()){
            File f = generateKmlFile(gMotionSceneList, this.getStreams().get(1));

            //Logica para calcular la leyenda a mostrar
            float biggest=-1;
            switch(this.additionalDataArray[0]){
                case "pga":
                    biggest = GMotionScen.findBiggestPgaAcceleration(gMotionSceneList);
                    break;
                case "sa3":
                    biggest = GMotionScen.findBiggestSa3Acceleration(gMotionSceneList);
                    break;
                case "sa10":
                    biggest = GMotionScen.findBiggestSa10Acceleration(gMotionSceneList);
                    break;
            }
            if(biggest <= 0.3){
                super.setAdditionalData("1");
            } else {
                if(biggest > 0.3 && biggest <= 0.6){
                    super.setAdditionalData("2");
                } else {
                    super.setAdditionalData("3");
                }
            }

            AzureBlobManager abm = new AzureBlobManager();
            uri = abm.putAtKmlAzureBlob(f, this.getKml_file_name());
            return uri.toString();
        }

        return "";
    }

    public File generateKmlFile(ArrayList<GMotionScen> gmotionscenes, InputStream polytractFile) {
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
        Folder shakeFolder = new Folder().withName("gmotionscen");

        for(int i = 0; i < polyFolder.getFeature().size() && i < gmotionscenes.size(); i++){
            if(polyFolder.getFeature().get(i) instanceof Placemark){
                Placemark placemark = (Placemark) polyFolder.getFeature().get(i);
                float biggest;
                if((placemark.getName() != null && placemark.getName().equals(gmotionscenes.get(i).getGeounit())) ||
                        (placemark.getId() != null && placemark.getId().equals(gmotionscenes.get(i).getGeounit()))) {
                    switch(this.additionalDataArray[0]){
                        case "pga":
                            biggest = GMotionScen.findBiggestPgaAcceleration(gmotionscenes);
                            placemark.createAndAddStyle().withPolyStyle(gmotionscenes.get(i).getPgaKMLStyle(biggest));
                            break;
                        case "sa3":
                            biggest = GMotionScen.findBiggestSa3Acceleration(gmotionscenes);
                            placemark.createAndAddStyle().withPolyStyle(gmotionscenes.get(i).getSa3KMLStyle(biggest));
                            break;
                        case "sa10":
                            biggest = GMotionScen.findBiggestSa10Acceleration(gmotionscenes);
                            placemark.createAndAddStyle().withPolyStyle(gmotionscenes.get(i).getSa10KMLStyle(biggest));
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

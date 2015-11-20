package pvr3.tfg.domain.file_managers;

import de.micromata.opengis.kml.v_2_2_0.*;
import pvr3.tfg.domain.Coordinate;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Pablo on 16/07/2015.
 */
public abstract class AbstractFileManager {
    private ArrayList<InputStream> streams;
    private String kml_file_name;
    private String additionalData;

    protected HashMap<String,Coordinate> getCoordinatesFromCentroidFile(InputStream centroidFileStream){
        HashMap<String,Coordinate> coordinates = new HashMap<>();
        Kml centroidKml = Kml.unmarshal(centroidFileStream);
        Document document = (Document)centroidKml.getFeature();
        Folder centroidFolder = new Folder();
        for(int i=0; i<document.getFeature().size(); i++){
            if(document.getFeature().get(i) instanceof Folder){
                centroidFolder = (Folder) document.getFeature().get(i);
                break;
            }
        }

        for(int i = 0; i < centroidFolder.getFeature().size(); i++){

            if(centroidFolder.getFeature().get(i) instanceof Placemark){
                Placemark placemark = (Placemark) centroidFolder.getFeature().get(i);
                Point point = (Point)placemark.getGeometry();
                Coordinate coordinate = new Coordinate((float)point.getCoordinates().get(0).getLatitude(),
                        (float)point.getCoordinates().get(0).getLongitude());
                //Todo: Arreglar el follon que tengo aqui.
                if(placemark.getId()!= null && !placemark.getId().equals(placemark.getName())){
                    coordinates.put(placemark.getId(), coordinate);
                } else {
                    coordinates.put(placemark.getName(),coordinate);
                }
            }
        }
        return coordinates;
    }

    public abstract String convertFromTextFile();

    public String getKml_file_name() {
        return kml_file_name;
    }

    public String getAdditionalData() {
        return additionalData;
    }

    public ArrayList<InputStream> getStreams() {
        return streams;
    }

    public void setStreams(ArrayList<InputStream> streams) {
        this.streams = streams;
    }

    public void setKml_file_name(String kml_file_name) {
        this.kml_file_name = kml_file_name;
    }

    public void setAdditionalData(String additionalData) {
        this.additionalData = additionalData;
    }
}

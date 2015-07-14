package pvr3.tfg.domain;

import de.micromata.opengis.kml.v_2_2_0.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;


/**
 * Representation of one line of soilcenter file
 */
public class Soilcenter {
    /**
     * Geounit of the line
     */
    private String geounit;

    /**
     * Soil type of the line
     */
    private int soilType;

    /**
     * Constructor by params geounit and soilType
     * @param geounit geounit of the line
     * @param soilType Soil type of the line
     */
    public Soilcenter(String geounit, int soilType) {
        this.geounit = geounit;
        this.soilType = soilType;
    }

    /**
     * Returns the color of every type of soil
     * @return String with the color
     */
    private String getColor() {
        switch (this.soilType){
            case 1:
                return "641400F0";
            case 2:
                return "64143C32";
            case 3:
                return "7fcc3300";
            case 4:
                return "7f00aaff";
            case 5:
                return "7f2d52a0";
            default:
                return "";
        }
    }

    /**
     * Creates a PolyStyle Kml object to insert into Style object in a Placemark
     * @return The PolyStyle object with the color of the soil type
     */
    public PolyStyle getKMLStyle(){
        return KmlFactory.createPolyStyle().withColor(getColor());
    }

    public static File generateKmlFile(List<Soilcenter> soilcenters, InputStream polytractFile){
        Kml polyTract = Kml.unmarshal(polytractFile);
        Document document = (Document)polyTract.getFeature().withName("PolyTract.kml");
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
                if(placemark.getName().equals(soilcenters.get(i).getGeounit())) {
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

    public String getGeounit() {
        return geounit;
    }
}

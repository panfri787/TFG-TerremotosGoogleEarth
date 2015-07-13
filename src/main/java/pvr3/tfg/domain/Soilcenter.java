package pvr3.tfg.domain;

import de.micromata.opengis.kml.v_2_2_0.KmlFactory;
import de.micromata.opengis.kml.v_2_2_0.PolyStyle;


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

    public String getGeounit() {
        return geounit;
    }
}

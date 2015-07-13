package pvr3.tfg.domain;

import de.micromata.opengis.kml.v_2_2_0.KmlFactory;
import de.micromata.opengis.kml.v_2_2_0.PolyStyle;


/**
 * Created by Pablo on 30/06/2015.
 */
public class SoilcenterFile {
    private String geounit;

    private int soilType;

    public SoilcenterFile(String geounit, int soilType) {
        this.geounit = geounit;
        this.soilType = soilType;
    }

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

    public PolyStyle getKMLStyle(){
       return KmlFactory.createPolyStyle().withColor(getColor());
    }

    public String getGeounit() {
        return geounit;
    }

    public void setGeounit(String geounit) {
        this.geounit = geounit;
    }

    public int getSoiltype() {
        return soilType;
    }

    public void setSoiltype(int soiltype) {
        this.soilType = soiltype;
    }
}

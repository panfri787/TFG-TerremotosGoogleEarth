package pvr3.tfg.domain;

import de.micromata.opengis.kml.v_2_2_0.KmlFactory;
import de.micromata.opengis.kml.v_2_2_0.PolyStyle;

import java.util.List;

/**
 * Created by Pablo on 30/06/2015.
 */

public class GMotionScen {
    private String geounit;
    private float pga;
    private float sa_3;
    private float sa_10;
    private float soilPga;
    private float soilSa_3;
    private float soilSa_10;

    public GMotionScen(String geounit, float pga, float sa_3, float sa_10) {
        this.geounit = geounit;
        this.pga = pga;
        this.sa_3 = sa_3;
        this.sa_10 = sa_10;
    }

    public static float findBiggestPgaAcceleration(List<GMotionScen> gMotionScenList){
        float biggest = -1;
        for(GMotionScen g : gMotionScenList){
            if(g.getPga() > biggest){
                biggest = g.getPga();
            }
        }

        return biggest;
    }

    public static float findBiggestSoilPgaAcceleration(List<GMotionScen> gMotionScenList){
        float biggest = -1;
        for(GMotionScen g : gMotionScenList){
            if(g.getSoilPga() > biggest){
                biggest = g.getSoilPga();
            }
        }

        return biggest;
    }

    public static float findBiggestSa3Acceleration(List<GMotionScen> gMotionScenList){
        float biggest = -1;
        for(GMotionScen g : gMotionScenList){
            if(g.getSa_3() > biggest){
                biggest = g.getSa_3();
            }
        }

        return biggest;
    }

    public static float findBiggestSoilSa3Acceleration(List<GMotionScen> gMotionScenList){
        float biggest = -1;
        for(GMotionScen g : gMotionScenList){
            if(g.getSoilSa_3() > biggest){
                biggest = g.getSoilSa_3();
            }
        }

        return biggest;
    }

    public static float findBiggestSa10Acceleration(List<GMotionScen> gMotionScenList){
        float biggest = -1;
        for(GMotionScen g : gMotionScenList){
            if(g.getSa_10() > biggest){
                biggest = g.getSa_10();
            }
        }

        return biggest;
    }

    public static float findBiggestSoilSa10Acceleration(List<GMotionScen> gMotionScenList){
        float biggest = -1;
        for(GMotionScen g : gMotionScenList){
            if(g.getSoilSa_10() > biggest){
                biggest = g.getSoilSa_10();
            }
        }

        return biggest;
    }

    private String getColorPga(float biggest) {
        String color="";
        if(biggest <= 0.3){
            if (this.getPga() <= 0.06)
                color = "7f66ff99";
            if (this.getPga() <= 0.12 && this.getPga() > 0.06)
                color = "7fffcc00";
            if (this.getPga() <= 0.18 && this.getPga() > 0.12)
                color = "7fcc3300";
            if (this.getPga() <= 0.24 && this.getPga() > 0.18)
                color = "7f00aaff";
            if (this.getPga() > 0.24)
                color = "7f0000ff";
        } else {
            if (biggest > 0.3 && biggest <= 0.6){
                if (this.getPga() <= 0.12)
                    color = "7f66ff99";
                if (this.getPga() <= 0.24 && this.getPga() > 0.12)
                    color = "7fffcc00";
                if (this.getPga() <= 0.36 && this.getPga() > 0.24)
                    color = "7fcc3300";
                if (this.getPga() <= 0.48 && this.getPga() > 0.36)
                    color = "7f00aaff";
                if (this.getPga() > 0.48)
                    color = "7f0000ff";
            } else {
                if (this.getPga() <= 0.2)
                    color = "7f66ff99";
                if (this.getPga() <= 0.4 && this.getPga() > 0.2)
                    color = "7fffcc00";
                if (this.getPga() <= 0.6 && this.getPga() > 0.4)
                    color = "7fcc3300";
                if (this.getPga() <= 0.8 && this.getPga() > 0.6)
                    color = "7f00aaff";
                if (this.getPga() > 0.8)
                    color = "7f0000ff";
            }
        }

        return color;
    }

    private String getColorSa3(float biggest) {
        String color="";
        if(biggest <= 0.3){
            if (this.getSa_3() <= 0.06)
                color = "7f66ff99";
            if (this.getSa_3() <= 0.12 && this.getSa_3() > 0.06)
                color = "7fffcc00";
            if (this.getSa_3() <= 0.18 && this.getSa_3() > 0.12)
                color = "7fcc3300";
            if (this.getSa_3() <= 0.24 && this.getSa_3() > 0.18)
                color = "7f00aaff";
            if (this.getSa_3() > 0.24)
                color = "7f0000ff";
        } else {
            if (biggest > 0.3 && biggest <= 0.6){
                if (this.getSa_3() <= 0.12)
                    color = "7f66ff99";
                if (this.getSa_3() <= 0.24 && this.getSa_3() > 0.12)
                    color = "7fffcc00";
                if (this.getSa_3() <= 0.36 && this.getSa_3() > 0.24)
                    color = "7fcc3300";
                if (this.getSa_3() <= 0.48 && this.getSa_3() > 0.36)
                    color = "7f00aaff";
                if (this.getSa_3() > 0.48)
                    color = "7f0000ff";
            } else {
                if (this.getSa_3() <= 0.2)
                    color = "7f66ff99";
                if (this.getSa_3() <= 0.4 && this.getSa_3() > 0.2)
                    color = "7fffcc00";
                if (this.getSa_3() <= 0.6 && this.getSa_3() > 0.4)
                    color = "7fcc3300";
                if (this.getSa_3() <= 0.8 && this.getSa_3() > 0.6)
                    color = "7f00aaff";
                if (this.getSa_3() > 0.8)
                    color = "7f0000ff";
            }
        }

        return color;
    }

    private String getColorSa10(float biggest) {
        String color="";
        if(biggest <= 0.3){
            if (this.getSa_10() <= 0.06)
                color = "7f66ff99";
            if (this.getSa_10() <= 0.12 && this.getSa_10() > 0.06)
                color = "7fffcc00";
            if (this.getSa_10() <= 0.18 && this.getSa_10() > 0.12)
                color = "7fcc3300";
            if (this.getSa_10() <= 0.24 && this.getSa_10() > 0.18)
                color = "7f00aaff";
            if (this.getSa_10() > 0.24)
                color = "7f0000ff";
        } else {
            if (biggest > 0.3 && biggest <= 0.6){
                if (this.getSa_10() <= 0.12)
                    color = "7f66ff99";
                if (this.getSa_10() <= 0.24 && this.getSa_10() > 0.12)
                    color = "7fffcc00";
                if (this.getSa_10() <= 0.36 && this.getSa_10() > 0.24)
                    color = "7fcc3300";
                if (this.getSa_10() <= 0.48 && this.getSa_10() > 0.36)
                    color = "7f00aaff";
                if (this.getSa_10() > 0.48)
                    color = "7f0000ff";
            } else {
                if (this.getSa_10() <= 0.2)
                    color = "7f66ff99";
                if (this.getSa_10() <= 0.4 && this.getSa_10() > 0.2)
                    color = "7fffcc00";
                if (this.getSa_10() <= 0.6 && this.getSa_10() > 0.4)
                    color = "7fcc3300";
                if (this.getSa_10() <= 0.8 && this.getSa_10() > 0.6)
                    color = "7f00aaff";
                if (this.getSa_10() > 0.8)
                    color = "7f0000ff";
            }
        }

        return color;
    }

    public PolyStyle getPgaKMLStyle(float biggest){
        return KmlFactory.createPolyStyle().withColor(getColorPga(biggest));
    }

    public PolyStyle getSa10KMLStyle(float biggest){
        return KmlFactory.createPolyStyle().withColor(getColorSa10(biggest));
    }

    public PolyStyle getSa3KMLStyle(float biggest){
        return KmlFactory.createPolyStyle().withColor(getColorSa3(biggest));
    }

    public String getGeounit() {
        return geounit;
    }

    public void setGeounit(String geounit) {
        this.geounit = geounit;
    }

    public float getPga() {
        return pga;
    }

    public float getSa_3() {
        return sa_3;
    }

    public float getSa_10() {
        return sa_10;
    }

    public float getSoilPga() {
        return soilPga;
    }

    public float getSoilSa_3() {
        return soilSa_3;
    }

    public float getSoilSa_10() {
        return soilSa_10;
    }
}

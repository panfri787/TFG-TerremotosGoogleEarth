package pvr3.tfg.domain;

import de.micromata.opengis.kml.v_2_2_0.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pablo on 30/06/2015.
 */
public class Shakecenter {
    private String geounit;
    private float pga;
    private float sa_3;
    private float sa_10;

    public Shakecenter(String geounit, float pga, float sa_3, float sa_10) {
        this.geounit = geounit;
        this.pga = pga;
        this.sa_3 = sa_3;
        this.sa_10 = sa_10;
    }

    public static float findBiggestPgaAcceleration(List<Shakecenter> shakecenterList){
        float biggest = -1;
        for(Shakecenter s : shakecenterList){
            if(s.getPga() > biggest){
                biggest = s.getPga();
            }
        }

        return biggest;
    }

    public static float findBiggestSa3Acceleration(List<Shakecenter> shakecenterList){
        float biggest = -1;
        for(Shakecenter s : shakecenterList){
            if(s.getSa_3() > biggest){
                biggest = s.getSa_3();
            }
        }

        return biggest;
    }

    public static float findBiggestSa10Acceleration(List<Shakecenter> shakecenterList){
        float biggest = -1;
        for(Shakecenter s : shakecenterList){
            if(s.getSa_10() > biggest){
                biggest = s.getSa_10();
            }
        }

        return biggest;
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

    public static File generateKmlFile(ArrayList<Shakecenter> shakecenters, InputStream polytractFile, String additionalData) {
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
        Folder shakeFolder = new Folder().withName("shakecenter");

        for(int i = 0; i < polyFolder.getFeature().size() && i < shakecenters.size(); i++){
            if(polyFolder.getFeature().get(i) instanceof Placemark){
                Placemark placemark = (Placemark) polyFolder.getFeature().get(i);
                float biggest;
                if(placemark.getName().equals(shakecenters.get(i).getGeounit())) {
                    switch(additionalData){
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

    public String getGeounit() {
        return geounit;
    }

    public void setGeounit(String geounit) {
        this.geounit = geounit;
    }

    public float getPga() {
        return pga;
    }

    public void setPga(float pga) {
        this.pga = pga;
    }

    public float getSa_3() {
        return sa_3;
    }

    public void setSa_3(float sa_3) {
        this.sa_3 = sa_3;
    }

    public float getSa_10() {
        return sa_10;
    }

    public void setSa_10(float sa_10) {
        this.sa_10 = sa_10;
    }


}

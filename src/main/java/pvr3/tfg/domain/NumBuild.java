package pvr3.tfg.domain;

import de.micromata.opengis.kml.v_2_2_0.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Pablo on 16/07/2015.
 */
public class NumBuild {
    private String geounit;
    private float totalBuilds;
    private float selectedBuild;

    public NumBuild(String geounit) {
        this.geounit = geounit;
        this.totalBuilds = 0;
        this.selectedBuild = 0;
    }

    public float getPercentageOfBuilds(){
        if(this.getTotalBuilds() == 0){
            return 0;
        } else {
            return (this.getSelectedBuild()/this.getTotalBuilds()) * 100;
        }
    }

    private String getColorBuiltArea(){
        if(this.getPercentageOfBuilds() <= 20)
            return "7f66ff99";

        if(this.getPercentageOfBuilds() <= 40 && this.getPercentageOfBuilds() > 20)
            return "7fffcc00";

        if(this.getPercentageOfBuilds() <= 60 && this.getPercentageOfBuilds() > 40)
            return "7fcc3300";

        if(this.getPercentageOfBuilds() <= 80 && this.getPercentageOfBuilds() > 60)
            return "7f00aaff";

        if(this.getPercentageOfBuilds() > 80)
            return "7f0000ff";

        //0% - 20% default color set.
        return "7f66ff99";
    }

    public PolyStyle getKmlStyle(){
        return KmlFactory.createPolyStyle().withColor(this.getColorBuiltArea());
    }

    public static File generateKmlFile(List<NumBuild> numBuilds, InputStream polytractFile){
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
        Folder soilFolder = new Folder().withName("numbuilds");

        for(int i = 0; i < polyFolder.getFeature().size() && i < numBuilds.size(); i++){

            if(polyFolder.getFeature().get(i) instanceof Placemark){
                Placemark placemark = (Placemark) polyFolder.getFeature().get(i);
                if(placemark.getName().equals(numBuilds.get(i).getGeounit())) {
                    placemark.createAndAddStyle().withPolyStyle(numBuilds.get(i).getKmlStyle());
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

    public float getTotalBuilds() {
        return totalBuilds;
    }

    public void setTotalBuilds(float totalBuilds) {
        this.totalBuilds = totalBuilds;
    }

    public float getSelectedBuild() {
        return selectedBuild;
    }

    public void setSelectedBuild(float selectedBuild) {
        this.selectedBuild = selectedBuild;
    }

    public String getGeounit() {
        return geounit;
    }
}

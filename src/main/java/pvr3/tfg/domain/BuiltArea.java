package pvr3.tfg.domain;

import de.micromata.opengis.kml.v_2_2_0.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Pablo on 30/06/2015.
 */
public class BuiltArea {

    private String geounit;
    private float totalBuiltArea;
    private float selectedBuiltArea;

    public BuiltArea(String geounit) {
        this.geounit = geounit;
        this.totalBuiltArea = 0;
        this.selectedBuiltArea = 0;
    }

    public float getPercentageOfBuiltArea(){
        if(this.getTotalBuiltArea() == 0){
            return 0;
        } else {
            return (this.getSelectedBuiltArea()/this.getTotalBuiltArea()) * 100;
        }
    }

    private String getColorBuiltArea(){
        if(this.getPercentageOfBuiltArea() <= 20)
            return "7f66ff99";

        if(this.getPercentageOfBuiltArea() <= 40 && this.getPercentageOfBuiltArea() > 20)
            return "7fffcc00";

        if(this.getPercentageOfBuiltArea() <= 60 && this.getPercentageOfBuiltArea() > 40)
            return "7fcc3300";

        if(this.getPercentageOfBuiltArea() <= 80 && this.getPercentageOfBuiltArea() > 60)
            return "7f00aaff";

        if(this.getPercentageOfBuiltArea() > 80)
            return "7f0000ff";

        //0% - 20% default color set.
        return "7f66ff99";
    }

    public PolyStyle getKmlStyle(){
        return KmlFactory.createPolyStyle().withColor(this.getColorBuiltArea());
    }

    public static File generateKmlFile(List<BuiltArea> builtAreas, InputStream polytractFile){
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
        Folder soilFolder = new Folder().withName("builtarea");

        for(int i = 0; i < polyFolder.getFeature().size() && i < builtAreas.size(); i++){

            if(polyFolder.getFeature().get(i) instanceof Placemark){
                Placemark placemark = (Placemark) polyFolder.getFeature().get(i);
                if(placemark.getName().equals(builtAreas.get(i).getGeounit())) {
                    placemark.createAndAddStyle().withPolyStyle(builtAreas.get(i).getKmlStyle());
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

    public float getTotalBuiltArea() {
        return totalBuiltArea;
    }

    public void setTotalBuiltArea(float totalBuiltArea) {
        this.totalBuiltArea = totalBuiltArea;
    }

    public float getSelectedBuiltArea() {
        return selectedBuiltArea;
    }

    public void setSelectedBuiltArea(float selectedBuiltArea) {
        this.selectedBuiltArea = selectedBuiltArea;
    }
}

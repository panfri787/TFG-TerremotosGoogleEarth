package pvr3.tfg.domain;

import de.micromata.opengis.kml.v_2_2_0.*;
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

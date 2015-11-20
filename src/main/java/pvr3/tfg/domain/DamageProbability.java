package pvr3.tfg.domain;

import de.micromata.opengis.kml.v_2_2_0.*;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Pablo on 20/11/2015.
 */
public class DamageProbability {
    private String geounit;
    private float none;
    private float slight;
    private float moderate;
    private float extensive;
    private float complete;
    private double highAcumulated;

    public DamageProbability(String geounit, float none, float slight, float moderate, float extensive, float complete) {
        this.geounit = geounit;
        this.none = none;
        this.slight = slight;
        this.moderate = moderate;
        this.extensive = extensive;
        this.complete = complete;
        this.highAcumulated = 0;
    }

    public List<Placemark> getKmlRepresentation(Coordinate coordinate, float maxValue){
        ArrayList<Placemark> placemarkList = new ArrayList<>();
        for(int i=5; i>0; i--){
            Placemark placemark = new Placemark();
            String name = null;
            PolyStyle polyStyle = null;
            ArrayList<CoordinateKml> poligonCoordinates=null;
            switch (i){
                case 5:
                    if(getNone() > 0) {
                        name = "Geounit:" + getGeounit() + ", None:"+ getNone()*100+"%.";
                        polyStyle = KmlFactory.createPolyStyle().withColor("ff66ff99").withOutline(true);
                        poligonCoordinates = this.calculateKmlCoordinates(coordinate, getNone(),i, maxValue);
                        break;
                    } else {
                        name = "Geounit:" + getGeounit() + ", None:100%.";
                        polyStyle = KmlFactory.createPolyStyle().withColor("ff66ff99").withOutline(true);
                        poligonCoordinates = this.calculateKmlCoordinates(coordinate, 50,i, maxValue);
                        break;
                    }
                case 4:
                    name = "Geounit:" + getGeounit() + ", Slight:"+ getSlight()*100+"%.";
                    polyStyle = KmlFactory.createPolyStyle().withColor("ffffcc00").withOutline(true);
                    poligonCoordinates = this.calculateKmlCoordinates(coordinate, getSlight(),i, maxValue);
                    break;
                case 3:
                    name = "Geounit:" + getGeounit() + ", Moderate:"+ getModerate()*100+"%.";
                    polyStyle = KmlFactory.createPolyStyle().withColor("ffcc3300").withOutline(true);
                    poligonCoordinates = this.calculateKmlCoordinates(coordinate, getModerate(),i, maxValue);
                    break;
                case 2:
                    name = "Geounit:" + getGeounit() + ", Extensive:"+ getExtensive()*100+"%.";
                    polyStyle = KmlFactory.createPolyStyle().withColor("ff00aaff").withOutline(true);
                    poligonCoordinates = this.calculateKmlCoordinates(coordinate, getExtensive(),i, maxValue);
                    break;
                case 1:
                    name = "Geounit:" + getGeounit() + ", Complete:"+ getComplete()*100+"%.";
                    polyStyle = KmlFactory.createPolyStyle().withColor("ff0000ff").withOutline(true);
                    poligonCoordinates = this.calculateKmlCoordinates(coordinate, getComplete(),i, maxValue);
                    break;
            }
            placemark.setName(name);
            placemark.createAndAddStyle().withPolyStyle(polyStyle);
            Polygon bar = placemark.createAndSetPolygon();
            bar.setAltitudeMode(AltitudeMode.RELATIVE_TO_GROUND);
            bar.setExtrude(true);
            Boundary boundary = new Boundary();
            LinearRing linearRing = boundary.createAndSetLinearRing();
            for(CoordinateKml ck : poligonCoordinates){
                linearRing.addToCoordinates(ck.getLongitude(),ck.getLatitude(),ck.getHigh());
            }
            bar.setOuterBoundaryIs(boundary);
            placemarkList.add(placemark);
        }

        return placemarkList;
    }

    private ArrayList<CoordinateKml> calculateKmlCoordinates(Coordinate coordinate, float value, int iDec, float maxValue) {
        double step = 0.002;
        double stepX = step * 0.5;
        double stepY = step;
        double highUnit;

        if(maxValue <= 0){
            highUnit = 0.01;
        } else {
            highUnit = maxValue/2500d;
        }

        double factorReductionX = (step/50) * iDec;
        double factorReductionY = factorReductionX * 0.5;

        if(coordinate.getLatitude() < 0){
            factorReductionX = factorReductionX * -1;
            stepX = stepX * -1;
        }
        if(coordinate.getLongitude() < 0){
            factorReductionY = factorReductionY * -1;
            stepY = stepY * -1;
        }

        if(value < 0){
            value = 0;
        }

        double nextHigh = this.getHighAcumulated() + value/highUnit;
        this.setHighAcumulated(nextHigh);
        double high = this.getHighAcumulated();

        ArrayList<CoordinateKml> coordinateKmlList = new ArrayList<>();
        double x = 0,y = 0;
        CoordinateKml originPoint = null;

        for(int j=0;j<4;j++){
            switch (j){
                case 0:
                    x = coordinate.getLatitude() - stepX - factorReductionX;
                    y = coordinate.getLongitude();
                    break;
                case 1:
                    x = coordinate.getLatitude();
                    y = coordinate.getLongitude() + stepY + factorReductionY;
                    break;
                case 2:
                    x = coordinate.getLatitude() + stepX + factorReductionX;
                    y = coordinate.getLongitude();
                    break;
                case 3:
                    x = coordinate.getLatitude();
                    y = coordinate.getLongitude() - stepY - factorReductionY;
                    break;
            }
            if(j == 0){
                originPoint = new CoordinateKml(x,y,high);
            }
            coordinateKmlList.add(new CoordinateKml(x,y,high));
        }
        coordinateKmlList.add(originPoint);

        return coordinateKmlList;
    }

    public static float getMaxValueFromList(ArrayList<DamageProbability> damageProbabilities){
        float maxValue = 0;
        for(DamageProbability da : damageProbabilities){
            if(da.getMaxValue() > maxValue){
                maxValue = da.getMaxValue();
            }
        }
        return maxValue;
    }

    private float getMaxValue(){
        float[] values = {getNone(), getSlight(), getModerate(), getExtensive(), getComplete()};

        List valuesList = Arrays.asList(ArrayUtils.toObject(values));
        return (float) Collections.max(valuesList);
    }

    public String getGeounit() {
        return geounit;
    }

    public float getNone() {
        return none;
    }

    public float getSlight() {
        return slight;
    }

    public float getModerate() {
        return moderate;
    }

    public float getExtensive() {
        return extensive;
    }

    public float getComplete() {
        return complete;
    }

    public double getHighAcumulated() {
        return highAcumulated;
    }

    public void setHighAcumulated(double highAcumulated) {
        this.highAcumulated = highAcumulated;
    }
}
